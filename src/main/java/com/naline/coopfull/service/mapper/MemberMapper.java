package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.domain.CooperativeBranch;
import com.naline.coopfull.domain.IndividualMember;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.domain.OrganizationMember;
import com.naline.coopfull.domain.ProfessionalProfile;
import com.naline.coopfull.domain.SocialProfile;
import com.naline.coopfull.service.dto.CooperativeBranchDTO;
import com.naline.coopfull.service.dto.CooperativeDTO;
import com.naline.coopfull.service.dto.IndividualMemberDTO;
import com.naline.coopfull.service.dto.MemberDTO;
import com.naline.coopfull.service.dto.OrganizationMemberDTO;
import com.naline.coopfull.service.dto.ProfessionalProfileDTO;
import com.naline.coopfull.service.dto.SocialProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Member} and its DTO {@link MemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {
    @Mapping(target = "individualMember", source = "individualMember", qualifiedByName = "individualMemberId")
    @Mapping(target = "organizationMember", source = "organizationMember", qualifiedByName = "organizationMemberId")
    @Mapping(target = "socialProfile", source = "socialProfile", qualifiedByName = "socialProfileId")
    @Mapping(target = "professionalProfile", source = "professionalProfile", qualifiedByName = "professionalProfileId")
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeName")
    @Mapping(target = "branch", source = "branch", qualifiedByName = "cooperativeBranchName")
    MemberDTO toDto(Member s);

    @Named("individualMemberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IndividualMemberDTO toDtoIndividualMemberId(IndividualMember individualMember);

    @Named("organizationMemberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrganizationMemberDTO toDtoOrganizationMemberId(OrganizationMember organizationMember);

    @Named("socialProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SocialProfileDTO toDtoSocialProfileId(SocialProfile socialProfile);

    @Named("professionalProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfessionalProfileDTO toDtoProfessionalProfileId(ProfessionalProfile professionalProfile);

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
