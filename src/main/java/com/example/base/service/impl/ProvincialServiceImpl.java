package com.example.base.service.impl;

import com.example.base.po.ProvincialPO;
import com.example.base.dao.ProvincialDAO;
import com.example.base.service.ProvincialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ProvincialPO)表服务实现类
 *
 * @author makejava
 * @since 2021-03-09 16:42:45
 */
@Service("provincialService")
public class ProvincialServiceImpl implements ProvincialService {
    @Resource
    private ProvincialDAO provincialDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ProvincialPO getById(Integer id) {
        return this.provincialDao.getById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<ProvincialPO> listAllByLimit(int offset, int limit) {
        return this.provincialDao.listAllByLimit(offset, limit);
    }

    /**
     * 根据实体条件查询
     *
     * @param provincial 查询实体
     * @return 对象列表
     */
    @Override
    public List<ProvincialPO> listAll(ProvincialPO provincial) {
        return this.provincialDao.listAll(provincial);
    }


    /**
     * 新增数据
     *
     * @param provincial 实例对象
     * @return 实例对象
     */
    @Override
    public ProvincialPO insert(ProvincialPO provincial) {
        this.provincialDao.insert(provincial);
        return provincial;
    }

    /**
     * 修改数据
     *
     * @param provincial 实例对象
     * @return 实例对象
     */
    @Override
    public ProvincialPO update(ProvincialPO provincial) {
        this.provincialDao.update(provincial);
        return this.getById(provincial.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.provincialDao.deleteById(id) > 0;
    }
}
