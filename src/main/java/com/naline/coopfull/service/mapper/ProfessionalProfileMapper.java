package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.ProfessionalProfile;
import com.naline.coopfull.service.dto.ProfessionalProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalProfile} and its DTO {@link ProfessionalProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalProfileMapper extends EntityMapper<ProfessionalProfileDTO, ProfessionalProfile> {}
