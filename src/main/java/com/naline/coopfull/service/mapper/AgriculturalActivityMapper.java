package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.AgriculturalActivity;
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.service.dto.AgriculturalActivityDTO;
import com.naline.coopfull.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgriculturalActivity} and its DTO {@link AgriculturalActivityDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgriculturalActivityMapper extends EntityMapper<AgriculturalActivityDTO, AgriculturalActivity> {
    @Mapping(target = "location", source = "location", qualifiedByName = "locationName")
    AgriculturalActivityDTO toDto(AgriculturalActivity s);

    @Named("locationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDTO toDtoLocationName(Location location);
}
