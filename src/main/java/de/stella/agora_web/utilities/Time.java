package de.stella.agora_web.utilities;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class Time {

    private static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);

    public String checkCurrentTime() {
        return simpleDateFormat.format(new Date());
    }

    public static String now() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }
}
