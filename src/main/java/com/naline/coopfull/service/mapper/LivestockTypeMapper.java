package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.LivestockType;
import com.naline.coopfull.service.dto.LivestockTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LivestockType} and its DTO {@link LivestockTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivestockTypeMapper extends EntityMapper<LivestockTypeDTO, LivestockType> {}
