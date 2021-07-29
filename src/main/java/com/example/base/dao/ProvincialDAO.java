package com.example.base.dao;

import com.example.base.po.ProvincialPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (ProvincialPO)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-09 16:42:44
 */
public interface ProvincialDAO {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ProvincialPO getById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ProvincialPO> listAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param provincial 实例对象
     * @return 对象列表
     */
    List<ProvincialPO> listAll(ProvincialPO provincial);

    /**
     * 新增数据
     *
     * @param provincial 实例对象
     * @return 影响行数
     */
    int insert(ProvincialPO provincial);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ProvincialPO> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ProvincialPO> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ProvincialPO> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<ProvincialPO> entities);

    /**
     * 修改数据
     *
     * @param provincial 实例对象
     * @return 影响行数
     */
    int update(ProvincialPO provincial);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

