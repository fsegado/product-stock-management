package com.rindus.mappers;

import org.mapstruct.Mapper;

import com.rindus.dto.ReserveDto;
import com.rindus.model.ReserveProduct;

@Mapper(componentModel = "spring")
public interface ReserveMapper {

	ReserveProduct toReserveProduct(ReserveDto reserveDto);

}
