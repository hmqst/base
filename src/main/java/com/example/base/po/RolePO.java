package com.example.base.po;


import lombok.ToString;

import java.io.Serializable;
/**
 * (RolePO)实体类  PO
 *
 * @author benben
 * @since 2021-03-10 11:02:45
 */
@ToString
public class RolePO implements Serializable {
    private Integer id;
    private String name;
    private String nameZh;

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

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }
}
