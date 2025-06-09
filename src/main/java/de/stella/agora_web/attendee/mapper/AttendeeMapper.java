package de.stella.agora_web.attendee.mapper;

import org.mapstruct.Mapper;

import de.stella.agora_web.attendee.controller.dto.AttendeeDTO;
import de.stella.agora_web.attendee.model.Attendee;

@Mapper(componentModel = "spring")
public interface AttendeeMapper {

    AttendeeDTO toDto(Attendee attendee);

    Attendee toEntity(AttendeeDTO dto);
}
