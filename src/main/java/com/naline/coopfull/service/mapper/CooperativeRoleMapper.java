package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.CooperativeRole;
import com.naline.coopfull.domain.Permission;
import com.naline.coopfull.service.dto.CooperativeRoleDTO;
import com.naline.coopfull.service.dto.PermissionDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CooperativeRole} and its DTO {@link CooperativeRoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface CooperativeRoleMapper extends EntityMapper<CooperativeRoleDTO, CooperativeRole> {
    @Mapping(target = "permissionses", source = "permissionses", qualifiedByName = "permissionCodeSet")
    CooperativeRoleDTO toDto(CooperativeRole s);

    @Mapping(target = "removePermissions", ignore = true)
    CooperativeRole toEntity(CooperativeRoleDTO cooperativeRoleDTO);

    @Named("permissionCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    PermissionDTO toDtoPermissionCode(Permission permission);

    @Named("permissionCodeSet")
    default Set<PermissionDTO> toDtoPermissionCodeSet(Set<Permission> permission) {
        return permission.stream().map(this::toDtoPermissionCode).collect(Collectors.toSet());
    }
}
