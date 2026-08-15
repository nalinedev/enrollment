package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.domain.CooperativeBranch;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.domain.MembershipApplication;
import com.naline.coopfull.service.dto.CooperativeBranchDTO;
import com.naline.coopfull.service.dto.CooperativeDTO;
import com.naline.coopfull.service.dto.MemberDTO;
import com.naline.coopfull.service.dto.MembershipApplicationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipApplication} and its DTO {@link MembershipApplicationDTO}.
 */
@Mapper(componentModel = "spring")
public interface MembershipApplicationMapper extends EntityMapper<MembershipApplicationDTO, MembershipApplication> {
    @Mapping(target = "member", source = "member", qualifiedByName = "memberMemberNumber")
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeName")
    @Mapping(target = "branch", source = "branch", qualifiedByName = "cooperativeBranchName")
    MembershipApplicationDTO toDto(MembershipApplication s);

    @Named("memberMemberNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberNumber", source = "memberNumber")
    MemberDTO toDtoMemberMemberNumber(Member member);

    @Named("cooperativeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CooperativeDTO toDtoCooperativeName(Cooperative cooperative);

    @Named("cooperativeBranchName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CooperativeBranchDTO toDtoCooperativeBranchName(CooperativeBranch cooperativeBranch);
}
