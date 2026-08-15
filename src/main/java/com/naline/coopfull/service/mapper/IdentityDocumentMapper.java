package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.IdentityDocument;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.service.dto.IdentityDocumentDTO;
import com.naline.coopfull.service.dto.MemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IdentityDocument} and its DTO {@link IdentityDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface IdentityDocumentMapper extends EntityMapper<IdentityDocumentDTO, IdentityDocument> {
    @Mapping(target = "member", source = "member", qualifiedByName = "memberMemberNumber")
    IdentityDocumentDTO toDto(IdentityDocument s);

    @Named("memberMemberNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberNumber", source = "memberNumber")
    MemberDTO toDtoMemberMemberNumber(Member member);
}
