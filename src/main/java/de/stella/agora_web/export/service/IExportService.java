package de.stella.agora_web.export.service;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public interface IExportService {

    void exportAttendeesToExcel(Long eventId, HttpServletResponse response) throws IOException;
}
