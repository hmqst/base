package com.example.base.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum TypeEnum {
    ONE(1, "类型一"),
    TWO(2, "类型二"),
    THREE(3, "类型三"),
    FOUR(4, "类型四"),
    FIVE(5, "类型五"),
    SIX(6, "类型六"),
    SEVEN(7, "类型七");

    private Integer code;
    private String display;

    TypeEnum(Integer code, String display) {
        this.code = code;
        this.display = display;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }


    private static final Map<Integer, TypeEnum> codeToEnum = new HashMap<>();
    private static final Map<String, TypeEnum> displayToEnum = new HashMap<>();

    //静态代码块初始化map
    static {
        for (TypeEnum type : TypeEnum.values()) {
            codeToEnum.put(type.getCode(), type);
            displayToEnum.put(type.getDisplay(), type);
        }
    }

    public static TypeEnum valueOfByCode(Integer code) {
        return codeToEnum.get(code);
    }

    public static TypeEnum valueOfByDisplay(String display) {
        return displayToEnum.get(display);
    }

    @JsonCreator
    static TypeEnum findValue(@JsonProperty("code") Integer code) {
        return valueOfByCode(code);
    }
}
