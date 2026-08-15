package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.AppUser;
import com.naline.coopfull.domain.User;
import com.naline.coopfull.service.dto.AppUserDTO;
import com.naline.coopfull.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    AppUserDTO toDto(AppUser s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
