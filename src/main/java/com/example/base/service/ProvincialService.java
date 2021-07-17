package com.example.base.service;

import com.example.base.po.ProvincialPO;

import java.util.List;

/**
 * (ProvincialPO)表服务接口
 *
 * @author makejava
 * @since 2021-03-09 16:42:44
 */
public interface ProvincialService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ProvincialPO getById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ProvincialPO> listAllByLimit(int offset, int limit);


    /**
     * 根据实体查询
     *
     * @param provincial 查询实体
     * @return 对象列表
     */
    List<ProvincialPO> listAll(ProvincialPO provincial);


    /**
     * 新增数据
     *
     * @param provincial 实例对象
     * @return 实例对象
     */
    ProvincialPO insert(ProvincialPO provincial);

    /**
     * 修改数据
     *
     * @param provincial 实例对象
     * @return 实例对象
     */
    ProvincialPO update(ProvincialPO provincial);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
