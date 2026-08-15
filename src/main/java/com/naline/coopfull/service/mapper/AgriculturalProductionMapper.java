package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.AgriculturalActivity;
import com.naline.coopfull.domain.AgriculturalProduction;
import com.naline.coopfull.domain.Crop;
import com.naline.coopfull.domain.CropVariety;
import com.naline.coopfull.service.dto.AgriculturalActivityDTO;
import com.naline.coopfull.service.dto.AgriculturalProductionDTO;
import com.naline.coopfull.service.dto.CropDTO;
import com.naline.coopfull.service.dto.CropVarietyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgriculturalProduction} and its DTO {@link AgriculturalProductionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgriculturalProductionMapper extends EntityMapper<AgriculturalProductionDTO, AgriculturalProduction> {
    @Mapping(target = "agriculturalActivity", source = "agriculturalActivity", qualifiedByName = "agriculturalActivityId")
    @Mapping(target = "crop", source = "crop", qualifiedByName = "cropName")
    @Mapping(target = "cropVariety", source = "cropVariety", qualifiedByName = "cropVarietyName")
    AgriculturalProductionDTO toDto(AgriculturalProduction s);

    @Named("agriculturalActivityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgriculturalActivityDTO toDtoAgriculturalActivityId(AgriculturalActivity agriculturalActivity);

    @Named("cropName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CropDTO toDtoCropName(Crop crop);

    @Named("cropVarietyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CropVarietyDTO toDtoCropVarietyName(CropVariety cropVariety);
}
