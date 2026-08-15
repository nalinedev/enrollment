package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.LivestockActivity;
import com.naline.coopfull.domain.LivestockProduction;
import com.naline.coopfull.service.dto.LivestockActivityDTO;
import com.naline.coopfull.service.dto.LivestockProductionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LivestockProduction} and its DTO {@link LivestockProductionDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivestockProductionMapper extends EntityMapper<LivestockProductionDTO, LivestockProduction> {
    @Mapping(target = "livestockActivity", source = "livestockActivity", qualifiedByName = "livestockActivityId")
    LivestockProductionDTO toDto(LivestockProduction s);

    @Named("livestockActivityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LivestockActivityDTO toDtoLivestockActivityId(LivestockActivity livestockActivity);
}
