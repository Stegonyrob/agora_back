package de.stella.agora_web.attendee.mapper;

import org.springframework.stereotype.Component;

import de.stella.agora_web.attendee.controller.dto.AttendeeDTO;
import de.stella.agora_web.attendee.model.Attendee;

@Component
public class AttendeeMapper {

    public AttendeeDTO toDto(Attendee attendee) {
        if (attendee == null) {
            return null;
        }

        AttendeeDTO dto = new AttendeeDTO();
        dto.setId(attendee.getId());
        dto.setName(attendee.getName());
        dto.setPhone(attendee.getPhone());
        dto.setEmail(attendee.getEmail());
        return dto;
    }

    public Attendee toEntity(AttendeeDTO dto) {
        if (dto == null) {
            return null;
        }

        Attendee attendee = new Attendee();
        attendee.setId(dto.getId());
        attendee.setName(dto.getName());
        attendee.setPhone(dto.getPhone());
        attendee.setEmail(dto.getEmail());
        // El event se asignará por separado en el servicio
        return attendee;
    }
}
