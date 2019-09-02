package myapp.MyAdminPanel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class test {

    public static void main(String[] args) {
        System.out.println(silnia(0));
    }

    public static int silnia(int liczba) {
        return liczba <= 1 ? 1 : silnia(liczba - 1) * liczba;
    }
}
