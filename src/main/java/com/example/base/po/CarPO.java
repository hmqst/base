package com.example.base.po;

import com.example.base.enums.TypeEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author benben
 * @program base
 * @Description MapStruct测试用PO
 * @date 2021-03-18 15:34
 */
@Data
@ToString
public class CarPO implements Serializable {
    private String make;
    private int number;
    private TypeEnum type;
    private String defaultStr;
    private Date date;

    public CarPO() {

    }

    public CarPO(String make, int number, TypeEnum type, Date date) {
        this.make = make;
        this.number = number;
        this.type = type;
        this.date = date;
    }
}
