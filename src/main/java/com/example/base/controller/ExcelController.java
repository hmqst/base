package com.example.base.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.example.base.config.excel.listener.ReadListener;
import com.example.base.dao.ExcelDAO;
import com.example.base.dao.ExcelErrorDAO;
import com.example.base.po.ExcelPO;
import com.example.base.utils.ResultBean;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("excel")
@Api(tags = "Excel相关")
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ExcelController {

    @Resource
    private ExcelDAO excelDao;

    @Resource
    private ExcelErrorDAO excelErrorDao;

    @ApiOperation("查询正确")
    @GetMapping("select")
    public ResultBean select(Integer id) {
        if (id != null) {
            return ResultBean.success(excelDao.getById(id));
        }
        return ResultBean.success(excelDao.listAll(null));
    }

    @ApiOperation("查询错误")
    @GetMapping("selectError")
    public ResultBean selectError(Integer id) {
        if (id != null) {
            return ResultBean.success(excelErrorDao.getById(id));
        }
        return ResultBean.success(excelErrorDao.listAll(null));
    }

    @ApiOperation("读取（导入）")
    @PostMapping("read")
    public ResultBean read(@RequestPart MultipartFile file) {
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
            ReadListener<ExcelPO> readListener = new ReadListener<>();
            EasyExcel.read(file.getInputStream(), ExcelPO.class, readListener).headRowNumber(1).sheet().doRead();
            List<ExcelPO> list = readListener.getList();
            List<ExcelPO> errorList = new ArrayList<>();
            List<HashMap<String, String>> errorListIndex = readListener.getErrorListIndex();
            // 判断数据合法合规
            Iterator<ExcelPO> iterator = list.iterator();
            while (iterator.hasNext()) {
                ExcelPO excel = iterator.next();
                if (excel.getType() == null || excel.getDate() == null
                        || excel.getDates() == null || excel.getName() == null
                        || excel.getNumber() == null || excel.getSex() == null) {
                    iterator.remove();
                    errorList.add(excel);
                }
            }
            if (list.size() > 0) {
                if (excelDao.insertBatch(list) <= 0) {
                    log.error("插入excel表失败");
                }
            }
            if (errorList.size() > 0) {
                if (excelErrorDao.insertBatch(errorList) <= 0) {
                    log.error("插入excel_error表失败");
                }
            }
            return ResultBean.success(
                    "读取到的正确数据：" + JSON.toJSONString(list)
                            + "\t\n读取到的异常数据：" + JSON.toJSONString(errorListIndex)
                            + "\t\n读取到的错误数据：" + JSON.toJSONString(errorList)
            );
        } catch (IOException e) {
            log.error("插入excel表失败，错误原因：" + e.getMessage());
            return ResultBean.fail(e.getMessage());
        }
    }

    @ApiOperation("写入（导出）")
    @GetMapping("write")
    public void write(String header, String excelName, HttpServletResponse response) throws IOException {
        List<ExcelPO> excels = excelDao.listAll(null);
        List<List<String>> head = new ArrayList<>();
        head.add(new ArrayList<String>() {{
            add(StringUtil.isEmpty(header) ? "测试表头" : header);
        }});
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        excelName = URLEncoder.encode(StringUtil.isEmpty(excelName) ? "测试表格" : excelName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + excelName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ExcelPO.class).head(head).sheet(0).doWrite(excels);
    }
}
