package com.example.base.service;

import com.example.base.po.ClassPO;

import java.util.List;

/**
 * (ClassPO)表服务接口
 *
 * @author benben
 * @since 2021-04-06 09:26:19
 */
public interface ClassService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ClassPO getById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ClassPO> listAllByLimit(int offset, int limit);

    /**
     * 查询多条数据
     *
     * @return 对象列表
     */
    List<ClassPO> listAll(ClassPO class1);

    /**
     * 新增数据
     *
     * @param class1 实例对象
     * @return 0 1
     */
    int insert(ClassPO class1);

    /**
     * 修改数据
     *
     * @param class1 实例对象
     * @return 实例对象
     */
    int update(ClassPO class1);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
