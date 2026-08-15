package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.LivestockActivity;
import com.naline.coopfull.domain.LivestockType;
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.service.dto.LivestockActivityDTO;
import com.naline.coopfull.service.dto.LivestockTypeDTO;
import com.naline.coopfull.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LivestockActivity} and its DTO {@link LivestockActivityDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivestockActivityMapper extends EntityMapper<LivestockActivityDTO, LivestockActivity> {
    @Mapping(target = "location", source = "location", qualifiedByName = "locationName")
    @Mapping(target = "livestockType", source = "livestockType", qualifiedByName = "livestockTypeName")
    LivestockActivityDTO toDto(LivestockActivity s);

    @Named("locationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDTO toDtoLocationName(Location location);

    @Named("livestockTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LivestockTypeDTO toDtoLivestockTypeName(LivestockType livestockType);
}
