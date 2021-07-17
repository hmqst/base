package com.example.base.controller;

import com.example.base.bo.PeopleBO;
import com.example.base.po.ClassPO;
import com.example.base.service.ClassService;
import com.example.base.utils.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * (ClassPO)表控制层
 *
 * @author benben
 * @since 2021-04-06 09:26:19
 */
@RestController
@RequestMapping("class")
@Api(tags = "数据库对象存储相关")
public class ClassController {
    /**
     * 服务对象
     */
    @Resource
    private ClassService classService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping("selectAll")
    @ApiOperation("查询所有Class")
    public ResultBean selectAll() {
        return ResultBean.success(classService.listAll(null));
    }

    /**
     * 新增数据
     *
     * @return 信息
     */
    @PostMapping("insert")
    @ApiOperation("新增Class")
    public ResultBean insert(@RequestBody(required = false) ClassPO class1) {
        if (class1 == null){
            return ResultBean.success(
                    classService.insert(new ClassPO("name", new ArrayList<PeopleBO>() {{
                        add(new PeopleBO("people1", 5));
                        add(new PeopleBO("people2", 6));
                        add(new PeopleBO("people3", 7));
                        add(new PeopleBO("people4", 8));
                        add(new PeopleBO("people5", 9));
                    }}))
            );
        }
        if (class1.getName() == null){
            return ResultBean.fail("name不可为空");
        }else if (class1.getPeople() == null){
            return ResultBean.fail("people不可为空");
        }
        int insert = classService.insert(class1);
        if (insert < 0){
            ResultBean.fail("新增Class失败");
        }
        return ResultBean.success("新增Class成功");
    }

}
