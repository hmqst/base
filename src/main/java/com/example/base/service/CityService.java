package com.example.base.service;

import com.example.base.po.CityPO;

import java.util.List;

/**
 * (CityPO)表服务接口
 *
 * @author makejava
 * @since 2021-03-09 16:42:46
 */
public interface CityService {

    /**
     * 通过ID查询单条数据
     *
     * @param name 主键
     * @return 实例对象
     */
    CityPO getById(String name);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<CityPO> listAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param city 实例对象
     * @return 实例对象
     */
    CityPO insert(CityPO city);

    /**
     * 修改数据
     *
     * @param city 实例对象
     * @return 实例对象
     */
    CityPO update(CityPO city);

    /**
     * 通过主键删除数据
     *
     * @param name 主键
     * @return 是否成功
     */
    boolean deleteById(String name);

}
