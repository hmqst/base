package com.example.base.service;

import com.example.base.po.UserPO;

import java.util.List;

/**
 * (UserPO)表服务接口
 *
 * @author makejava
 * @since 2021-03-10 11:02:45
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserPO getById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<UserPO> listAllByLimit(int offset, int limit);

    /**
     * 查询多条数据
     *
     * @param user 查询条件
     * @return 对象列表
     */
    List<UserPO> listAll(UserPO user);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    UserPO insert(UserPO user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    UserPO update(UserPO user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
