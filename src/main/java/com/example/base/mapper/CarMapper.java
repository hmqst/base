package com.example.base.mapper;

import com.example.base.dto.CarDTO;
import com.example.base.enums.TypeEnum;
import com.example.base.po.CarPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author benben
 * @date 2021-03-18 15:38
 */
@Mapper(componentModel = "spring", imports = {TypeEnum.class, Math.class})
public interface CarMapper {
    @Mapping(target = "name", ignore = true)
    @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "number", target = "count")
    @Mapping(source = "type.display", target = "type")
    @Mapping(target = "defaultStr", defaultValue = "默认值")
    CarDTO carToCarDto(CarPO car);


    @Mapping(target = "number", expression = "java(Math.round(carDto.getCount()))")
    @Mapping(target = "type", expression = "java(TypeEnum.valueOfByDisplay(carDto.getType()))")
    CarPO carDtoToCar(CarDTO carDto);
}
