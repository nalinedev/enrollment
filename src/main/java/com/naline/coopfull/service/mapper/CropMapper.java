package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.Crop;
import com.naline.coopfull.service.dto.CropDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Crop} and its DTO {@link CropDTO}.
 */
@Mapper(componentModel = "spring")
public interface CropMapper extends EntityMapper<CropDTO, Crop> {}
