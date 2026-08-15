package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.Crop;
import com.naline.coopfull.domain.CropVariety;
import com.naline.coopfull.service.dto.CropDTO;
import com.naline.coopfull.service.dto.CropVarietyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CropVariety} and its DTO {@link CropVarietyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CropVarietyMapper extends EntityMapper<CropVarietyDTO, CropVariety> {
    @Mapping(target = "crop", source = "crop", qualifiedByName = "cropName")
    CropVarietyDTO toDto(CropVariety s);

    @Named("cropName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CropDTO toDtoCropName(Crop crop);
}
