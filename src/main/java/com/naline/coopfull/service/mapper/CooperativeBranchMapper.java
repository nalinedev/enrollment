package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.domain.CooperativeBranch;
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.service.dto.CooperativeBranchDTO;
import com.naline.coopfull.service.dto.CooperativeDTO;
import com.naline.coopfull.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CooperativeBranch} and its DTO {@link CooperativeBranchDTO}.
 */
@Mapper(componentModel = "spring")
public interface CooperativeBranchMapper extends EntityMapper<CooperativeBranchDTO, CooperativeBranch> {
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeName")
    @Mapping(target = "location", source = "location", qualifiedByName = "locationName")
    CooperativeBranchDTO toDto(CooperativeBranch s);

    @Named("cooperativeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CooperativeDTO toDtoCooperativeName(Cooperative cooperative);

    @Named("locationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDTO toDtoLocationName(Location location);
}
