package de.stella.agora_web.attendee.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import de.stella.agora_web.attendee.controller.dto.AttendeeDTO;
import de.stella.agora_web.attendee.model.Attendee;
import de.stella.agora_web.attendee.repository.AttendeeRepository;
import de.stella.agora_web.attendee.service.IAttendeeService;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.repository.EventRepository;

@Service
public class AttendeeServiceImpl implements IAttendeeService {

    @Autowired
    private AttendeeRepository attendeeRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public AttendeeDTO registerAttendee(Long eventId, AttendeeDTO attendeeDTO) {
        if (attendeeRepository.existsByEventIdAndEmail(eventId, attendeeDTO.getEmail())
                || attendeeRepository.existsByEventIdAndPhone(eventId, attendeeDTO.getPhone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un registro con ese correo o teléfono para este evento.");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Comprobación de aforo
        int capacity = event.getCapacity();
        int currentAttendees = attendeeRepository.countByEventId(eventId);
        if (currentAttendees >= capacity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay plazas libres para este evento.");
        }

        Attendee attendee = new Attendee();
        attendee.setName(attendeeDTO.getName());
        attendee.setPhone(attendeeDTO.getPhone());
        attendee.setEmail(attendeeDTO.getEmail());
        attendee.setEvent(event);

        Attendee savedAttendee = attendeeRepository.save(attendee);
        attendeeDTO.setId(savedAttendee.getId());
        return attendeeDTO;
    }

    @Override
    public void deleteAttendee(Long eventId, Long attendeeId) {
        Attendee attendee = attendeeRepository.findByIdAndEventId(attendeeId, eventId)
                .orElseThrow(() -> new RuntimeException("Attendee not found"));
        attendeeRepository.delete(attendee);
    }

    @Override
    public AttendeeDTO updateAttendee(Long eventId, Long attendeeId, AttendeeDTO attendeeDTO) {
        Attendee attendee = attendeeRepository.findByIdAndEventId(attendeeId, eventId)
                .orElseThrow(() -> new RuntimeException("Attendee not found"));

        attendee.setName(attendeeDTO.getName());
        attendee.setPhone(attendeeDTO.getPhone());
        attendee.setEmail(attendeeDTO.getEmail());

        Attendee savedAttendee = attendeeRepository.save(attendee);
        attendeeDTO.setId(savedAttendee.getId());
        return attendeeDTO;
    }

    @Override
    public AttendeeDTO findById(Long eventId, Long attendeeId) {
        Attendee attendee = attendeeRepository.findByIdAndEventId(attendeeId, eventId)
                .orElseThrow(() -> new RuntimeException("Attendee not found"));
        return convertToDTO(attendee);
    }

    @Override
    public List<AttendeeDTO> findAllByEvent(Long eventId) {
        List<Attendee> attendees = attendeeRepository.findByEventId(eventId);
        return attendees.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private AttendeeDTO convertToDTO(Attendee attendee) {
        AttendeeDTO attendeeDTO = new AttendeeDTO();
        attendeeDTO.setId(attendee.getId());
        attendeeDTO.setName(attendee.getName());
        attendeeDTO.setPhone(attendee.getPhone());
        attendeeDTO.setEmail(attendee.getEmail());
        return attendeeDTO;
    }
}
