package com.example.base.controller;

import com.example.base.po.UserPO;
import com.example.base.service.UserService;
import com.example.base.utils.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (UserPO)表控制层
 *
 * @author makejava
 * @since 2021-03-10 11:02:45
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户相关")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("查询单个用户")
    @GetMapping("selectOne")
    public ResultBean selectOne(Integer id) {
        return ResultBean.success(userService.getById(id));
    }

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("selectLogin")
    public ResultBean selectLogin() {
        String account = SecurityContextHolder.getContext().getAuthentication().getName();
        if (account == null) {
            return ResultBean.fail("获取失败");
        }
        List<UserPO> user = userService.listAll(new UserPO(account, true));
        if (user == null || user.size() != 1) {
            return ResultBean.fail("获取失败，用户名不唯一。");
        }
        return ResultBean.success(user.get(0));
    }

}
