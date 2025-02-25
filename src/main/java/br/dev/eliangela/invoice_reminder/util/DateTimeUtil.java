package br.dev.eliangela.invoice_reminder.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static ZonedDateTime getCurrentDateTime() {
        ZoneId zone = ZoneId.of(ZoneId.SHORT_IDS.get("BET"));
        return Instant.now().atZone(zone);
    }

    public static LocalDate getLocalDateFromDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static String getCurrentDateTimeAsString() {
        ZonedDateTime currentDateTime = getCurrentDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm:ss Z");
        return currentDateTime.format(formatter);

    }
}
