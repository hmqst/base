package com.example.base.config.excel.convertor;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.example.base.enums.SexEnum;


public class SexEnumConvertor implements Converter<SexEnum> {

    /**
     * 转换的类型
     *
     * @return
     */
    @Override
    public Class supportJavaTypeKey() {
        return SexEnum.class;
    }

    /**
     * 接受的类型
     *
     * @return
     */
    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 读excel时的转换器
     *
     * @param cellData
     * @param contentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public SexEnum convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //读取到的excel的值
        String excelValue = cellData.getStringValue();
        if (excelValue != null) {
            if (excelValue.equals(SexEnum.MAN.getDisplay())) {
                return SexEnum.MAN;
            } else if (excelValue.equals(SexEnum.WOMAN.getDisplay())) {
                return SexEnum.WOMAN;
            }
        }
        return null;
    }

    /**
     * 写excel的转换器
     *
     * @param sex
     * @param contentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public CellData convertToExcelData(SexEnum sex, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (sex != null) {
            switch (sex) {
                case MAN:
                    return new CellData(SexEnum.MAN.getDisplay());
                case WOMAN:
                    return new CellData(SexEnum.WOMAN.getDisplay());
            }
        }
        return new CellData("女");
    }
}
