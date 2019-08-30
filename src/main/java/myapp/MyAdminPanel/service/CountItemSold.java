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
public class CountItemSold {

    @Autowired
    private MyItemRepository myItemRepository;

    public int countItemSoldByMonth(String month) {
        String startDate = DateGenerator.getFirstDayOfMonth(month);
        String stopDate = DateGenerator.getLastDayOfMonth(month);
        return myItemRepository.countBySellPriceIsNotNullAndSellDateIsBetween(startDate, stopDate);
    }
}
