package myapp.MyAdminPanel.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

public class DateGenerator {

    public static String getLastDayOfMonth(String month) {
        String date = "1/" + month + "/" + LocalDate.now().getYear();
        LocalDate lastDayOfMonth = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy")).with(TemporalAdjusters.lastDayOfMonth());
        return lastDayOfMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String getMonthName(String month) {
        return Month.of(Integer.parseInt(month)).getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("pl-PL"));
    }

    public static String getFirstDayOfMonth(String month) {
        return LocalDate.now().getYear() + "-" + month + "-01";
    }
}
