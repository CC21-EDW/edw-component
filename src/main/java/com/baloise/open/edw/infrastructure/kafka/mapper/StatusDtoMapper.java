package com.baloise.open.edw.infrastructure.kafka.mapper;

import com.baloise.open.edw.domain.kafka.Status;
import com.baloise.open.edw.infrastructure.kafka.model.StatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatusDtoMapper {
    StatusDtoMapper INSTANCE = Mappers.getMapper(StatusDtoMapper.class);

    StatusDto map(Status activity);

    default String map(Status.EventType eventType) {
        return eventType.name();
    }
}
