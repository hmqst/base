package com.example.base.service.impl;

import com.example.base.po.ClassPO;
import com.example.base.dao.ClassDAO;
import com.example.base.service.ClassService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ClassPO)表服务实现类
 *
 * @author benben
 * @since 2021-04-06 09:26:19
 */
@Service("classService")
public class ClassServiceImpl implements ClassService {
    @Resource
    private ClassDAO classDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ClassPO getById(Integer id) {
        return this.classDao.getById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<ClassPO> listAllByLimit(int offset, int limit) {
        return this.classDao.listAllByLimit(offset, limit);
    }

    @Override
    public List<ClassPO> listAll(ClassPO class1) {
        return this.classDao.listAll(class1);
    }

    /**
     * 新增数据
     *
     * @param class1 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(ClassPO class1) {
        return this.classDao.insert(class1);
    }

    /**
     * 修改数据
     *
     * @param class1 实例对象
     * @return 实例对象
     */
    @Override
    public int update(ClassPO class1) {
        return classDao.update(class1);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.classDao.deleteById(id) > 0;
    }
}
