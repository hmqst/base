package com.example.base.dao;

import com.example.base.po.ExcelPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (ExcelPO)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-12 10:19:15
 */
public interface ExcelDAO {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ExcelPO getById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param excel 实例对象
     * @return 对象列表
     */
    List<ExcelPO> listAll(ExcelPO excel);

    /**
     * 新增数据
     *
     * @param excel 实例对象
     * @return 影响行数
     */
    int insert(ExcelPO excel);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ExcelPO> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ExcelPO> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ExcelPO> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<ExcelPO> entities);

    /**
     * 修改数据
     *
     * @param excel 实例对象
     * @return 影响行数
     */
    int update(ExcelPO excel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

