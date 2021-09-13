package myapp.MyAdminPanel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProfitCounter {

    @Autowired
    private CountProfitByMonth countProfitByMonth;

    @Autowired
    private CountItemSold countItemSold;

    public List<Double> getProfitLastMonth(int quantityOfMonth) {
        List<Double> profitList = new ArrayList<>();
        for (int i = 0; i < quantityOfMonth; i++) {
            String month = DateTimeFormatter.ofPattern("MM").format(LocalDate.now().getMonth().minus(i));
            String year = DateTimeFormatter.ofPattern("YYYY").format(LocalDate.now().minusMonths(i));
            profitList.add(countProfitByMonth.getProfit(month, year));
        }
        Collections.reverse(profitList);
        return profitList;
    }

    public List<Integer> getSoldItemByLastMonth(int quantityOfMonth) {
        List<Integer> soldItem = new ArrayList<>();
        if (quantityOfMonth < 0) {
            soldItem.add(0);
            return soldItem;
        }
        for (int i = 0; i < quantityOfMonth; i++) {
            String month = DateTimeFormatter.ofPattern("MM").format(LocalDate.now().getMonth().minus(i));
            String year = DateTimeFormatter.ofPattern("YYYY").format(LocalDate.now().minusMonths(i));
            soldItem.add(countItemSold.countItemSoldByMonth(month, year));
        }
        Collections.reverse(soldItem);
        return soldItem;
    }
}
