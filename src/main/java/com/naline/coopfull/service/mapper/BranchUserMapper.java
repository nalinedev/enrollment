package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.AppUser;
import com.naline.coopfull.domain.BranchUser;
import com.naline.coopfull.domain.CooperativeBranch;
import com.naline.coopfull.domain.CooperativeRole;
import com.naline.coopfull.service.dto.AppUserDTO;
import com.naline.coopfull.service.dto.BranchUserDTO;
import com.naline.coopfull.service.dto.CooperativeBranchDTO;
import com.naline.coopfull.service.dto.CooperativeRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchUser} and its DTO {@link BranchUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface BranchUserMapper extends EntityMapper<BranchUserDTO, BranchUser> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "appUserId")
    @Mapping(target = "branch", source = "branch", qualifiedByName = "cooperativeBranchName")
    @Mapping(target = "role", source = "role", qualifiedByName = "cooperativeRoleName")
    BranchUserDTO toDto(BranchUser s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);

    @Named("cooperativeBranchName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CooperativeBranchDTO toDtoCooperativeBranchName(CooperativeBranch cooperativeBranch);

    @Named("cooperativeRoleName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CooperativeRoleDTO toDtoCooperativeRoleName(CooperativeRole cooperativeRole);
}
