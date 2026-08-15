package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.FamilyMember;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.service.dto.FamilyMemberDTO;
import com.naline.coopfull.service.dto.MemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FamilyMember} and its DTO {@link FamilyMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface FamilyMemberMapper extends EntityMapper<FamilyMemberDTO, FamilyMember> {
    @Mapping(target = "member", source = "member", qualifiedByName = "memberMemberNumber")
    FamilyMemberDTO toDto(FamilyMember s);

    @Named("memberMemberNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberNumber", source = "memberNumber")
    MemberDTO toDtoMemberMemberNumber(Member member);
}
