package myapp.MyAdminPanel.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjuster;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.temporal.TemporalAdjusters;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountProfitByMonth {
    private int month;

    @Autowired
    private MyItemRepository myItemRepository;

    public int getProfit(String month) {
        String startDate = LocalDate.now().getYear() + "-" + month + "-01";
        String stopDate = getDateWithLastDayOfMonth(month);
        return this.countProfitByDate(startDate, stopDate);
    }

    public String getMonthName(String month){
        return Month.of(Integer.parseInt(month)).getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("pl-PL"));
    }

    private int countProfitByDate(String startDate, String stopDate){
        int profit, tempValue = 0;
        profit = (myItemRepository.getSellPriceSumWhereSellDateBetween(startDate, stopDate));
        tempValue = (myItemRepository.getBuyPriceSumWhereSellDateBetween(startDate, stopDate));
        return profit - tempValue;
    }

    private static String getDateWithLastDayOfMonth(String month){
        String date = "1/" + month + "/" + LocalDate.now().getYear();
        LocalDate lastDayOfMonth = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy")).with(TemporalAdjusters.lastDayOfMonth());
        return lastDayOfMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
