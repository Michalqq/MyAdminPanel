package myapp.MyAdminPanel.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountProfitByMonth {
    private int month;

    @Autowired
    private MyItemRepository myItemRepository;

    public int getProfit(String month, String year) {
        String startDate = DateGenerator.getFirstDayOfMonth(month, year);
        String stopDate = DateGenerator.getLastDayOfMonth(month, year);
        return this.countProfitByDate(startDate, stopDate);
    }

    private int countProfitByDate(String startDate, String stopDate) {
        int profit, tempValue = 0;
        profit = (myItemRepository.getSellPriceSumWhereSellDateBetween(startDate, stopDate));
        tempValue = (myItemRepository.getBuyPriceSumWhereSellDateBetween(startDate, stopDate));
        return profit - tempValue;
    }

}
