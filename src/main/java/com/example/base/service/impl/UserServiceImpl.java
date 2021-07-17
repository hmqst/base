package com.example.base.service.impl;

import com.example.base.po.UserPO;
import com.example.base.dao.UserDAO;
import com.example.base.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (UserPO)表服务实现类
 *
 * @author makejava
 * @since 2021-03-10 11:02:45
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDAO userDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserPO getById(Integer id) {
        return this.userDao.getById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<UserPO> listAllByLimit(int offset, int limit) {
        return this.userDao.listAllByLimit(offset, limit);
    }

    /**
     * 根据条件查询多条数据
     * @param user 查询条件
     * @return
     */
    @Override
    public List<UserPO> listAll(UserPO user) {
        return this.userDao.listAll(user);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public UserPO insert(UserPO user) {
        this.userDao.insert(user);
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public UserPO update(UserPO user) {
        this.userDao.update(user);
        return this.getById(user.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.userDao.deleteById(id) > 0;
    }
}
