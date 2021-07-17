package com.example.base.config.excel.convertor;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.example.base.enums.TypeEnum;

public class TypeEnumConvertor implements Converter<TypeEnum> {
    @Override
    public Class supportJavaTypeKey() {
        return TypeEnum.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public TypeEnum convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //读取到的excel的值
        String excelValue = cellData.getStringValue();
        if (excelValue != null) {
            if (TypeEnum.ONE.getDisplay().equals(excelValue)) {
                return TypeEnum.ONE;
            } else if (TypeEnum.TWO.getDisplay().equals(excelValue)) {
                return TypeEnum.TWO;
            } else if (TypeEnum.THREE.getDisplay().equals(excelValue)) {
                return TypeEnum.THREE;
            } else if (TypeEnum.FOUR.getDisplay().equals(excelValue)) {
                return TypeEnum.FOUR;
            } else if (TypeEnum.FIVE.getDisplay().equals(excelValue)) {
                return TypeEnum.FIVE;
            } else if (TypeEnum.SIX.getDisplay().equals(excelValue)) {
                return TypeEnum.SIX;
            } else if (TypeEnum.SEVEN.getDisplay().equals(excelValue)) {
                return TypeEnum.SEVEN;
            }
        }
        return null;
    }

    @Override
    public CellData convertToExcelData(TypeEnum type, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (type != null) {
            switch (type) {
                case ONE:
                    return new CellData(TypeEnum.ONE.getDisplay());
                case TWO:
                    return new CellData(TypeEnum.TWO.getDisplay());
                case THREE:
                    return new CellData(TypeEnum.THREE.getDisplay());
                case FOUR:
                    return new CellData(TypeEnum.FOUR.getDisplay());
                case FIVE:
                    return new CellData(TypeEnum.FIVE.getDisplay());
                case SIX:
                    return new CellData(TypeEnum.SIX.getDisplay());
                case SEVEN:
                    return new CellData(TypeEnum.SEVEN.getDisplay());
            }
        }
        return new CellData(TypeEnum.ONE.getDisplay());
    }
}
