package myapp.MyAdminPanel;

import myapp.MyAdminPanel.service.DateGenerator;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDateGenerator {


    @Test
    public void testGetLastDateOfMonth() {
        assertEquals(LocalDate.now().getYear() + "-SEPTEMBER-01", DateGenerator.getFirstDayOfMonth("SEPTEMBER", Integer.toString(LocalDate.now().getYear())));
        assertEquals(LocalDate.now().getYear() + "-JANUARY-01", DateGenerator.getFirstDayOfMonth("JANUARY", Integer.toString(LocalDate.now().getYear())));
    }

}
