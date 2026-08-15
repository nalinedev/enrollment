package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.AppUser;
import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.domain.CooperativeRole;
import com.naline.coopfull.domain.CooperativeUser;
import com.naline.coopfull.service.dto.AppUserDTO;
import com.naline.coopfull.service.dto.CooperativeDTO;
import com.naline.coopfull.service.dto.CooperativeRoleDTO;
import com.naline.coopfull.service.dto.CooperativeUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CooperativeUser} and its DTO {@link CooperativeUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface CooperativeUserMapper extends EntityMapper<CooperativeUserDTO, CooperativeUser> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "appUserId")
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeName")
    @Mapping(target = "role", source = "role", qualifiedByName = "cooperativeRoleName")
    CooperativeUserDTO toDto(CooperativeUser s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);

    @Named("cooperativeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CooperativeDTO toDtoCooperativeName(Cooperative cooperative);

    @Named("cooperativeRoleName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CooperativeRoleDTO toDtoCooperativeRoleName(CooperativeRole cooperativeRole);
}
