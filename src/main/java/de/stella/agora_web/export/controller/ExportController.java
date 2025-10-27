package de.stella.agora_web.export.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stella.agora_web.export.service.IExportService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/export")
public class ExportController {

    private IExportService exportService;

    public ExportController(IExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/events/{eventId}/attendees")
    public void exportAttendeesToExcel(@PathVariable Long eventId, HttpServletResponse response) throws IOException {
        exportService.exportAttendeesToExcel(eventId, response);
    }
}
