package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.EconomicActivityType;
import com.naline.coopfull.service.dto.EconomicActivityTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EconomicActivityType} and its DTO {@link EconomicActivityTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EconomicActivityTypeMapper extends EntityMapper<EconomicActivityTypeDTO, EconomicActivityType> {}
