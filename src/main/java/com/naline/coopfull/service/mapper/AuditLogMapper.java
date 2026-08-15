package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.AppUser;
import com.naline.coopfull.domain.AuditLog;
import com.naline.coopfull.service.dto.AppUserDTO;
import com.naline.coopfull.service.dto.AuditLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AuditLog} and its DTO {@link AuditLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuditLogMapper extends EntityMapper<AuditLogDTO, AuditLog> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "appUserId")
    AuditLogDTO toDto(AuditLog s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);
}
