package com.example.base.dao;

import com.example.base.po.CityPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (CityPO)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-09 16:42:46
 */
public interface CityDAO {

    /**
     * 通过ID查询单条数据
     *
     * @param name 主键
     * @return 实例对象
     */
    CityPO getById(String name);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<CityPO> listAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param city 实例对象
     * @return 对象列表
     */
    List<CityPO> listAll(CityPO city);

    /**
     * 新增数据
     *
     * @param city 实例对象
     * @return 影响行数
     */
    int insert(CityPO city);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CityPO> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CityPO> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CityPO> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<CityPO> entities);

    /**
     * 修改数据
     *
     * @param city 实例对象
     * @return 影响行数
     */
    int update(CityPO city);

    /**
     * 通过主键删除数据
     *
     * @param name 主键
     * @return 影响行数
     */
    int deleteById(String name);

}

