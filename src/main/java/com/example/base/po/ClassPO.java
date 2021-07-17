package com.example.base.po;

import com.example.base.bo.PeopleBO;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * (ClassPO)实体类
 *
 * @author benben
 * @since 2021-04-06 09:26:07
 */
@ToString
public class ClassPO implements Serializable {
    private static final long serialVersionUID = -74743442502163158L;

    private Integer id;

    private String name;

    private List<PeopleBO> people;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ClassPO() {
    }

    public ClassPO(String name, List<PeopleBO> people) {
        this.name = name;
        this.people = people;
    }

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

    public List<PeopleBO> getPeople() {
        return people;
    }

    public void setPeople(List<PeopleBO> people) {
        this.people = people;
    }
}
