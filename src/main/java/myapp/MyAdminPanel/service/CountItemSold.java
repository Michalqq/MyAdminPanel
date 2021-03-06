package myapp.MyAdminPanel.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountItemSold {

    @Autowired
    private MyItemRepository myItemRepository;

    public int countItemSoldByMonth(String month, String year) {
        String startDate = DateGenerator.getFirstDayOfMonth(month, year);
        String stopDate = DateGenerator.getLastDayOfMonth(month, year);
        return myItemRepository.countBySellPriceIsNotNullAndSellDateIsBetween(startDate, stopDate);
    }

    public List<Double> getLastSoldSumData(int quantityOfDay, List<String> date) {
        List<Double> soldByDayList = new ArrayList<>();
        for (int i = 0; i < quantityOfDay; i++) {
            soldByDayList.add(myItemRepository.sumSellPriceWhereSellDateIs(date.get(i)));
        }
        return soldByDayList;
    }
}
