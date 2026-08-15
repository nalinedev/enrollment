package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.AgriculturalActivity;
import com.naline.coopfull.domain.AquacultureActivity;
import com.naline.coopfull.domain.EconomicActivity;
import com.naline.coopfull.domain.EconomicActivityType;
import com.naline.coopfull.domain.LivestockActivity;
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.service.dto.AgriculturalActivityDTO;
import com.naline.coopfull.service.dto.AquacultureActivityDTO;
import com.naline.coopfull.service.dto.EconomicActivityDTO;
import com.naline.coopfull.service.dto.EconomicActivityTypeDTO;
import com.naline.coopfull.service.dto.LivestockActivityDTO;
import com.naline.coopfull.service.dto.LocationDTO;
import com.naline.coopfull.service.dto.MemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EconomicActivity} and its DTO {@link EconomicActivityDTO}.
 */
@Mapper(componentModel = "spring")
public interface EconomicActivityMapper extends EntityMapper<EconomicActivityDTO, EconomicActivity> {
    @Mapping(target = "agriculturalActivity", source = "agriculturalActivity", qualifiedByName = "agriculturalActivityId")
    @Mapping(target = "livestockActivity", source = "livestockActivity", qualifiedByName = "livestockActivityId")
    @Mapping(target = "aquacultureActivity", source = "aquacultureActivity", qualifiedByName = "aquacultureActivityId")
    @Mapping(target = "member", source = "member", qualifiedByName = "memberMemberNumber")
    @Mapping(target = "activityType", source = "activityType", qualifiedByName = "economicActivityTypeName")
    @Mapping(target = "location", source = "location", qualifiedByName = "locationName")
    EconomicActivityDTO toDto(EconomicActivity s);

    @Named("agriculturalActivityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgriculturalActivityDTO toDtoAgriculturalActivityId(AgriculturalActivity agriculturalActivity);

    @Named("livestockActivityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LivestockActivityDTO toDtoLivestockActivityId(LivestockActivity livestockActivity);

    @Named("aquacultureActivityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AquacultureActivityDTO toDtoAquacultureActivityId(AquacultureActivity aquacultureActivity);

    @Named("memberMemberNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberNumber", source = "memberNumber")
    MemberDTO toDtoMemberMemberNumber(Member member);

    @Named("economicActivityTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    EconomicActivityTypeDTO toDtoEconomicActivityTypeName(EconomicActivityType economicActivityType);

    @Named("locationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDTO toDtoLocationName(Location location);
}
