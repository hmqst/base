package com.example.base.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

import java.io.Serializable;

/**
 * (ProvincialPO)实体类   PO
 *
 * @author benben
 * @since 2021-03-09 16:42:43
 */
@ApiModel(value = "ProvincialPO", description = "省份")
@ToString
public class ProvincialPO implements Serializable {
    private static final long serialVersionUID = 701610868082393637L;

    private Integer id;
    @ApiModelProperty("省份名称")
    private String name;


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

}
