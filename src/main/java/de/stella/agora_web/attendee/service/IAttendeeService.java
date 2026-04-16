package de.stella.agora_web.attendee.service;

import java.util.List;

import de.stella.agora_web.attendee.controller.dto.AttendeeDTO;

public interface IAttendeeService {
    AttendeeDTO registerAttendee(Long eventId, AttendeeDTO attendeeDTO);

    public void deleteAttendee(Long eventId, Long attendeeId);

    public AttendeeDTO updateAttendee(Long eventId, Long attendeeId, AttendeeDTO attendeeDTO);

    public AttendeeDTO findById(Long eventId, Long attendeeId);

    public List<AttendeeDTO> findAllByEvent(Long eventId);
}