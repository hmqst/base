package com.example.base.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * (CityPO)实体类  PO
 *
 * @author benben
 * @since 2021-03-09 16:42:46
 */
@ApiModel(value = "CityPO", description = "城市")
@ToString
public class CityPO implements Serializable {
    private static final long serialVersionUID = -96648265766873098L;

    private Integer id;
    @ApiModelProperty("城市名称")
    private String name;
    @ApiModelProperty("省份外键")
    private Integer pid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
