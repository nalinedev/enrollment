package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.Location;
import com.naline.coopfull.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "locationName")
    LocationDTO toDto(Location s);

    @Named("locationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDTO toDtoLocationName(Location location);
}
