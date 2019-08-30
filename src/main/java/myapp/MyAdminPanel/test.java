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
        System.out.println(LocalDate.now().getMonth().minus(9));
    }
    public static boolean isOdd(int i) {
        return i % 2 == 1;
    }
}
