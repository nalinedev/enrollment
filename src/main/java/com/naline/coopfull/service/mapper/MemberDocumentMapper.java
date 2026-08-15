package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.AppUser;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.domain.MemberDocument;
import com.naline.coopfull.service.dto.AppUserDTO;
import com.naline.coopfull.service.dto.MemberDTO;
import com.naline.coopfull.service.dto.MemberDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MemberDocument} and its DTO {@link MemberDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface MemberDocumentMapper extends EntityMapper<MemberDocumentDTO, MemberDocument> {
    @Mapping(target = "member", source = "member", qualifiedByName = "memberMemberNumber")
    @Mapping(target = "uploadedBy", source = "uploadedBy", qualifiedByName = "appUserId")
    MemberDocumentDTO toDto(MemberDocument s);

    @Named("memberMemberNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberNumber", source = "memberNumber")
    MemberDTO toDtoMemberMemberNumber(Member member);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);
}
