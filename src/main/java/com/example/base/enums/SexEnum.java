package com.example.base.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum SexEnum {
    MAN(0, "男"),
    WOMAN(1, "女");

    private Integer code;
    private String display;

    SexEnum(Integer code, String display) {
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

    private static final Map<Integer, SexEnum> codeToEnum = new HashMap<>();
    private static final Map<String, SexEnum> displayToEnum = new HashMap<>();

    //静态代码块初始化map
    static {
        for (SexEnum type : SexEnum.values()) {
            codeToEnum.put(type.getCode(), type);
            displayToEnum.put(type.getDisplay(), type);
        }
    }

    public static SexEnum valueOfByCode(Integer code) {
        return codeToEnum.get(code);
    }

    public static SexEnum valueOfByDisplay(String display) {
        return displayToEnum.get(display);
    }

    @JsonCreator
    static SexEnum findValue(@JsonProperty("code") Integer code) {
        return valueOfByCode(code);
    }
}
