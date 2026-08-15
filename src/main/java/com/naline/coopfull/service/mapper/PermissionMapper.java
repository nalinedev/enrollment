package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.CooperativeRole;
import com.naline.coopfull.domain.Permission;
import com.naline.coopfull.service.dto.CooperativeRoleDTO;
import com.naline.coopfull.service.dto.PermissionDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Permission} and its DTO {@link PermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {
    @Mapping(target = "roleses", source = "roleses", qualifiedByName = "cooperativeRoleIdSet")
    PermissionDTO toDto(Permission s);

    @Mapping(target = "roleses", ignore = true)
    @Mapping(target = "removeRoles", ignore = true)
    Permission toEntity(PermissionDTO permissionDTO);

    @Named("cooperativeRoleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CooperativeRoleDTO toDtoCooperativeRoleId(CooperativeRole cooperativeRole);

    @Named("cooperativeRoleIdSet")
    default Set<CooperativeRoleDTO> toDtoCooperativeRoleIdSet(Set<CooperativeRole> cooperativeRole) {
        return cooperativeRole.stream().map(this::toDtoCooperativeRoleId).collect(Collectors.toSet());
    }
}
