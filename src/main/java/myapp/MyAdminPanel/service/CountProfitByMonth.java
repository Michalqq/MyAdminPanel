package myapp.MyAdminPanel.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
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
        int profit = 0;
        int tempValue = 0;
        String startDate = LocalDate.now().getYear() + "-" + month + "-01";
        String date = "1/" + month + "/" + LocalDate.now().getYear();
        LocalDate lastDayOfMonth = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy")).with(TemporalAdjusters.lastDayOfMonth());
        String stopDate = lastDayOfMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println(startDate);
        System.out.println(stopDate);
        System.out.println("++++++++++++++++++++++");
        profit = (myItemRepository.getSellPriceSumWhereSellDateBetween(startDate, stopDate));
        tempValue = (myItemRepository.getBuyPriceSumWhereSellDateBetween(startDate, stopDate));
        profit = profit - tempValue;
        System.out.println(profit);

        return profit;
    }
}
