package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.AquaticSpecies;
import com.naline.coopfull.service.dto.AquaticSpeciesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AquaticSpecies} and its DTO {@link AquaticSpeciesDTO}.
 */
@Mapper(componentModel = "spring")
public interface AquaticSpeciesMapper extends EntityMapper<AquaticSpeciesDTO, AquaticSpecies> {}
