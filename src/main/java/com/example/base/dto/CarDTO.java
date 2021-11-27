package com.example.base.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * MapStruct测试用DTO
 * @author benben
 * @date 2021-03-18 15:35
 */

@Data
@ToString
public class CarDTO implements Serializable {
    private String name = "不映射";
    private String make;
    private Float count;
    private String type;
    private String defaultStr;
    private String date;

    public CarDTO() {
    }

    public CarDTO(String make, Float count, String type, String defaultStr, String date) {
        this.make = make;
        this.count = count;
        this.type = type;
        this.defaultStr = defaultStr;
        this.date = date;
    }
}
