package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.IndividualMember;
import com.naline.coopfull.service.dto.IndividualMemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IndividualMember} and its DTO {@link IndividualMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface IndividualMemberMapper extends EntityMapper<IndividualMemberDTO, IndividualMember> {}
