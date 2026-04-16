package de.stella.agora_web.attendee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.attendee.controller.dto.AttendeeDTO;
import de.stella.agora_web.attendee.service.IAttendeeService;
import jakarta.validation.Valid;

@RestController

@RequestMapping("${api-endpoint}/attendees")
public class AttendeeController {

    @Autowired
    private IAttendeeService attendeeService;

    @PostMapping("/{eventId}")
    public ResponseEntity<AttendeeDTO> registerAttendee(@PathVariable Long eventId,
            @RequestBody @Valid AttendeeDTO attendeeDTO) {

        AttendeeDTO savedAttendee = attendeeService.registerAttendee(eventId, attendeeDTO);
        return ResponseEntity.ok(savedAttendee);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<AttendeeDTO>> findAllByEvent(@PathVariable Long eventId) {
        List<AttendeeDTO> attendeeDTOS = attendeeService.findAllByEvent(eventId);
        return ResponseEntity.ok(attendeeDTOS);
    }

    @GetMapping("/{eventId}/{attendeeId}")
    public ResponseEntity<AttendeeDTO> findById(@PathVariable Long eventId, @PathVariable Long attendeeId) {
        AttendeeDTO attendeeDTO = attendeeService.findById(eventId, attendeeId);
        return ResponseEntity.ok(attendeeDTO);
    }

    @PutMapping("/{eventId}/{attendeeId}")
    public ResponseEntity<AttendeeDTO> updateAttendee(@PathVariable Long eventId, @PathVariable Long attendeeId,
            @RequestBody @Valid AttendeeDTO attendeeDTO) {
        AttendeeDTO updatedAttendee = attendeeService.updateAttendee(eventId, attendeeId, attendeeDTO);
        return ResponseEntity.ok(updatedAttendee);
    }

    @DeleteMapping("/{eventId}/{attendeeId}")
    public ResponseEntity<Void> deleteAttendee(@PathVariable Long eventId, @PathVariable Long attendeeId) {
        attendeeService.deleteAttendee(eventId, attendeeId);
        return ResponseEntity.noContent().build();
    }
}
