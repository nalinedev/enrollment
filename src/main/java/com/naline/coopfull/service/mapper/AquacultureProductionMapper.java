package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.AquacultureActivity;
import com.naline.coopfull.domain.AquacultureProduction;
import com.naline.coopfull.service.dto.AquacultureActivityDTO;
import com.naline.coopfull.service.dto.AquacultureProductionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AquacultureProduction} and its DTO {@link AquacultureProductionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AquacultureProductionMapper extends EntityMapper<AquacultureProductionDTO, AquacultureProduction> {
    @Mapping(target = "aquacultureActivity", source = "aquacultureActivity", qualifiedByName = "aquacultureActivityId")
    AquacultureProductionDTO toDto(AquacultureProduction s);

    @Named("aquacultureActivityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AquacultureActivityDTO toDtoAquacultureActivityId(AquacultureActivity aquacultureActivity);
}
