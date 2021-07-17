package com.example.base.controller;

import com.example.base.service.CityService;
import com.example.base.utils.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (CityPO)表控制层
 *
 * @author makejava
 * @since 2021-03-09 16:42:46
 */
@RestController
@RequestMapping("city")
@Api(tags = "城市相关")
public class CityController {

    /**
     * 服务对象
     */
    @Resource
    private CityService cityService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("查询单个城市")
    @GetMapping("selectOne")
    public ResultBean selectOne(String id) {
        return ResultBean.success(cityService.getById(id));
    }

}
