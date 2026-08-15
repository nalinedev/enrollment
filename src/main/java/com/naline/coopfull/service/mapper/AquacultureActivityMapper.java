package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.AquacultureActivity;
import com.naline.coopfull.domain.AquaticSpecies;
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.service.dto.AquacultureActivityDTO;
import com.naline.coopfull.service.dto.AquaticSpeciesDTO;
import com.naline.coopfull.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AquacultureActivity} and its DTO {@link AquacultureActivityDTO}.
 */
@Mapper(componentModel = "spring")
public interface AquacultureActivityMapper extends EntityMapper<AquacultureActivityDTO, AquacultureActivity> {
    @Mapping(target = "location", source = "location", qualifiedByName = "locationName")
    @Mapping(target = "aquaticSpecies", source = "aquaticSpecies", qualifiedByName = "aquaticSpeciesName")
    AquacultureActivityDTO toDto(AquacultureActivity s);

    @Named("locationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDTO toDtoLocationName(Location location);

    @Named("aquaticSpeciesName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AquaticSpeciesDTO toDtoAquaticSpeciesName(AquaticSpecies aquaticSpecies);
}
