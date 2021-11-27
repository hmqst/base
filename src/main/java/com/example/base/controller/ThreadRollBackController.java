package com.example.base.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.example.base.config.excel.listener.ReadListener;
import com.example.base.dao.ExcelDAO;
import com.example.base.dao.ExcelErrorDAO;
import com.example.base.po.ExcelPO;
import com.example.base.config.excel.RollBack;
import com.example.base.utils.ResultBean;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author benben
 * @date 2021-03-19 10:34
 */
@RestController
@RequestMapping("thread")
@Api(tags = "多线程事务相关")
@Slf4j
public class ThreadRollBackController {
    /**
     * 公平锁机制（谁等待时间越长谁先获得）
     */
    private final ReentrantLock reentrantLock = new ReentrantLock(true);

    @Resource
    private DataSourceTransactionManager transactionManager;

    @Resource
    private ExcelDAO excelDao;

    @Resource
    private ExcelErrorDAO excelErrorDao;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Transactional
    @ApiOperation("多线程插入数据库（ExcelPO）")
    @PostMapping("readSync")
    public ResultBean readSync(MultipartFile file) {
        // 公平锁 加锁 防止高并发情况下 数据库连接池最大连接数量不够用（事务开启后不提交或回滚会保持连接）
        reentrantLock.lock();
        try {
            // 判断文件
            if (file == null) {
                return ResultBean.fail("请选择文件进行上传");
            }
            // 获得文件名
            String filename = file.getOriginalFilename();
            // 判断文件名是否合法
            if (filename != null && !filename.matches("^.+\\.(?i)(xls)$") && !filename.matches("^.+\\.(?i)(xlsx)$")) {
                return ResultBean.fail("文件格式不正确");
            }
            // 读Excel数据
            ReadListener<ExcelPO> readListener = new ReadListener<>();
            EasyExcel.read(file.getInputStream(), ExcelPO.class, readListener).headRowNumber(1).sheet().doRead();
            // 正确解析到的数据
            List<ExcelPO> list = readListener.getList();
            // 解析异常的数据
            List<HashMap<String, String>> errorListIndex = readListener.getErrorListIndex();
            log.info("读取到的异常数据：" + JSON.toJSONString(errorListIndex));
            // 数据量过大导致线程池或数据库连接池不够用
            if (list.size() + errorListIndex.size() > 500) {
                return ResultBean.fail("Excel数据不可超过500");
            }

            // 多线程处理插入事务 每50条启动一个线程
            List<List<ExcelPO>> lists = Lists.partition(list, 50);
            // 子线程等待主线程判断是否需要回滚
            CountDownLatch mainLatch = new CountDownLatch(1);
            // 主线程等待子线程数据插入完成（判断是否需要回滚后子线程执行回滚或提交）
            CountDownLatch threadLatch = new CountDownLatch(lists.size());
            // 存储任务的返回结果，返回true表示需要回滚
            List<Boolean> transactionStatuses = new CopyOnWriteArrayList<>();
            // 多线程操作必须使用对象（原子类 AtomicBoolean）
            RollBack rollBack = new RollBack(false);
            // 存储子线程结束后返回的结果值
            List<Future<Object>> futures = new ArrayList<>();
            // 开启多线程插入数据库数据
            lists.forEach(excels -> {
                Future<Object> submit = threadPoolExecutor.submit(() -> {
                    log.info(Thread.currentThread().getName() + "--开始执行");
                    // 插入操作出错异常
                    Exception flag = null;
                    // 复制List，保证之后修改不会报错（子线程调用主线程数据时自动转换为final类型）
                    ArrayList<ExcelPO> noFinalExcels = Lists.newArrayList(excels);
                    //子线程事务定义,开始事务
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    TransactionStatus status = transactionManager.getTransaction(def);
                    List<ExcelPO> errorExcels = null;
                    // 定义线程安全的set，将重复数据剔除到errorList
                    CopyOnWriteArraySet<ExcelPO> set = new CopyOnWriteArraySet<>();
                    try {
                        // 判断数据合法性 并拆分数据（集合可能size为0）
                        errorExcels = checkExcel(noFinalExcels, set);
                        log.info(Thread.currentThread().getName() + "---读取到的正确数据：" + JSON.toJSONString(excels));
                        log.info(Thread.currentThread().getName() + "---读取到的错误数据：" + JSON.toJSONString(errorExcels));
                        // 插入数据库
                        if (noFinalExcels.size() > 0) {
                            excelDao.insertBatch(noFinalExcels);
                        }
                        if (errorExcels.size() > 0) {
                            excelErrorDao.insertBatch(errorExcels);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 需要回滚
                        transactionStatuses.add(true);
                        // 存储异常信息
                        flag = e;
                    } finally {
                        transactionStatuses.add(false);
                        // 子线程结束，所有子线程结束后主线程运行
                        threadLatch.countDown();
                        // 等待主线程检查是否需要回滚
                        try {
                            log.info("等待主线程判断是否回滚" + threadLatch.getCount());
                            mainLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (rollBack.isRollBack()) {
                            log.info(Thread.currentThread().getName() + "子线程回滚");
                            transactionManager.rollback(status);
                        } else {
                            log.info(Thread.currentThread().getName() + "子线程提交");
                            transactionManager.commit(status);
                        }
                    }
                    log.info(Thread.currentThread().getName() + "--执行结束");
                    if (flag != null) {
                        return Thread.currentThread().getName() + "执行结束，出错，错误信息为：" + flag;
                    } else {
                        return Thread.currentThread().getName() + "执行结束，插入数据量大小为：正确数据" + noFinalExcels.size() + "条，错误数据：" + errorExcels.size() + "条";
                    }

                });
                futures.add(submit);
            });
            // 等待子线程结束
            try {
                log.info("主线程等待");
                threadLatch.await();
                log.info("主线程执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 主线程结束 子线程运行
                mainLatch.countDown();
                log.info("主线程放开");
            }
            // 判断是否需要回魂 如果需要 通知子线程回滚
            if (transactionStatuses.size() > 0) {
                transactionStatuses.forEach(aBoolean -> {
                    if (aBoolean != null && aBoolean) {
                        rollBack.setRollBack(true);
                    }
                });
            }
            log.info("等待子线程事务回滚或提交结束");
            // 等待子线程事务回滚或提交结束
            try {
                HashMap<Object, Object> map = Maps.newHashMap();
                for (int i = 0; i < futures.size(); i++) {
                    // 此处为阻塞方法
                    Future future = futures.get(i);
                    log.info("子线程返回结果：" + future.get());
                    map.put("子线程返回结果" + i, future.get());
                }
                return ResultBean.success(map);
            } catch (Exception e) {
                log.error("多线程插入数据库失败,错误原因：" + e.getMessage());
                return ResultBean.fail("多线程插入数据库失败");
            }
        } catch (IOException e) {
            log.error("多线程插入数据库失败,错误原因：" + e.getMessage());
            // 出错回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultBean.fail("多线程插入数据库失败");
        } finally {
            // 公平锁 解锁
            reentrantLock.unlock();
        }
    }

    private List<ExcelPO> checkExcel(@NonNull List<ExcelPO> list, @NonNull CopyOnWriteArraySet<ExcelPO> set) {
        // 判断数据合法合规
        List<ExcelPO> errorList = new ArrayList<>();
        Iterator<ExcelPO> iterator = list.iterator();
        while (iterator.hasNext()) {
            ExcelPO excel = iterator.next();
            boolean add = set.add(excel);
            if (!add) {
                // 重复数据直接存入errorList
                iterator.remove();
                errorList.add(excel);
            } else {
                if (excel.getType() == null || excel.getDate() == null
                        || excel.getDates() == null || excel.getName() == null
                        || excel.getNumber() == null || excel.getSex() == null) {
                    iterator.remove();
                    errorList.add(excel);
                }
            }
        }
        return errorList;
    }
}
