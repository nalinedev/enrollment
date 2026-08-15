package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.service.dto.CooperativeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cooperative} and its DTO {@link CooperativeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CooperativeMapper extends EntityMapper<CooperativeDTO, Cooperative> {}
