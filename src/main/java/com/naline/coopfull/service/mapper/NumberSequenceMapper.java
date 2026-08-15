package com.naline.coopfull.service.mapper;

import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.domain.NumberSequence;
import com.naline.coopfull.service.dto.CooperativeDTO;
import com.naline.coopfull.service.dto.NumberSequenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NumberSequence} and its DTO {@link NumberSequenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface NumberSequenceMapper extends EntityMapper<NumberSequenceDTO, NumberSequence> {
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeName")
    NumberSequenceDTO toDto(NumberSequence s);

    @Named("cooperativeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CooperativeDTO toDtoCooperativeName(Cooperative cooperative);
}
