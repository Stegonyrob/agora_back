package de.stella.agora_web.export.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.attendee.model.Attendee;
import de.stella.agora_web.events.model.Event;
import de.stella.agora_web.events.repository.EventRepository;
import de.stella.agora_web.export.service.IExportService;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ExportServiceImpl implements IExportService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void exportAttendeesToExcel(Long eventId, HttpServletResponse response) throws IOException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        List<Attendee> attendees = event.getAttendees();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendees");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Name");
        header.createCell(1).setCellValue("Phone");
        header.createCell(2).setCellValue("Email");

        int rowNum = 1;
        for (Attendee attendee : attendees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(attendee.getName());
            row.createCell(1).setCellValue(attendee.getPhone());
            row.createCell(2).setCellValue(attendee.getEmail());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=attendees.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
