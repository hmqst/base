package com.example.base.controller;

import com.example.base.service.ProvincialService;
import com.example.base.utils.ResultBean;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ProvincialPO)表控制层
 *
 * @author makejava
 * @since 2021-03-09 16:42:45
 */
@RestController
@RequestMapping("provincial")
@Api(tags = "省份相关")
public class ProvincialController {
    /**
     * 服务对象
     */
    @Resource
    private ProvincialService provincialService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("查询单个省份")
    @GetMapping("selectOne")
    public ResultBean selectOne(Integer id) {
        return ResultBean.success(provincialService.getById(id));
    }

    @ApiOperation("查询全部省份")
    @GetMapping("selectAll")
    public ResultBean selectAll() {
        return ResultBean.success(provincialService.listAll(null));
    }

    @ApiOperation("查询全部省份（分页）")
    @GetMapping("selectAllByLimit")
    public ResultBean selectAllByLimit(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return ResultBean.success(provincialService.listAll(null));
    }

}
