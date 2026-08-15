package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.OrganizationMember;
import com.naline.coopfull.service.dto.OrganizationMemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationMember} and its DTO {@link OrganizationMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrganizationMemberMapper extends EntityMapper<OrganizationMemberDTO, OrganizationMember> {}
