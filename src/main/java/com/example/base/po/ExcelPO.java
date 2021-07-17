package com.example.base.po;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.example.base.config.excel.convertor.SexEnumConvertor;
import com.example.base.config.excel.convertor.TypeEnumConvertor;
import com.example.base.enums.SexEnum;
import com.example.base.enums.TypeEnum;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ExcelPO {
    //@ExcelProperty(value = "序号")
    @ExcelIgnore
    @ColumnWidth(25)
    private Integer id;
    @ExcelProperty(value = "姓名")
    @ColumnWidth(25)
    private String name;
    @ExcelProperty(value = "日期")
    @ColumnWidth(25)
    private Date date;
    @ExcelProperty(value = "日期（String）", format = "yyyy-MM-dd- HH:mm:ss")
    @ColumnWidth(25)
    private String dates;
    @ExcelProperty(value = "统计")
    @ColumnWidth(25)
    private Double number;
    @ExcelProperty(value = "性别", converter = SexEnumConvertor.class)
    @ColumnWidth(25)
    private SexEnum sex;
    @ExcelProperty(value = "类型", converter = TypeEnumConvertor.class)
    @ColumnWidth(25)
    private TypeEnum type;

    public ExcelPO() {
    }

    public ExcelPO(Integer id, String name, SexEnum sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExcelPO excel = (ExcelPO) o;

        if (name != null ? !name.equals(excel.name) : excel.name != null) return false;
        return sex == excel.sex;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        return result;
    }
}
