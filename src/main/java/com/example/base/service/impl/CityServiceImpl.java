package com.example.base.service.impl;

import com.example.base.po.CityPO;
import com.example.base.dao.CityDAO;
import com.example.base.service.CityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (CityPO)表服务实现类
 *
 * @author makejava
 * @since 2021-03-09 16:42:46
 */
@Service("cityService")
public class CityServiceImpl implements CityService {
    @Resource
    private CityDAO cityDao;

    /**
     * 通过ID查询单条数据
     *
     * @param name 主键
     * @return 实例对象
     */
    @Override
    public CityPO getById(String name) {
        return this.cityDao.getById(name);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<CityPO> listAllByLimit(int offset, int limit) {
        return this.cityDao.listAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param city 实例对象
     * @return 实例对象
     */
    @Override
    public CityPO insert(CityPO city) {
        this.cityDao.insert(city);
        return city;
    }

    /**
     * 修改数据
     *
     * @param city 实例对象
     * @return 实例对象
     */
    @Override
    public CityPO update(CityPO city) {
        this.cityDao.update(city);
        return this.getById(city.getName());
    }

    /**
     * 通过主键删除数据
     *
     * @param name 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String name) {
        return this.cityDao.deleteById(name) > 0;
    }
}
