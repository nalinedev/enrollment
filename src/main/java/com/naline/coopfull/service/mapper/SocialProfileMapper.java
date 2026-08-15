package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.SocialProfile;
import com.naline.coopfull.service.dto.SocialProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialProfile} and its DTO {@link SocialProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocialProfileMapper extends EntityMapper<SocialProfileDTO, SocialProfile> {}
