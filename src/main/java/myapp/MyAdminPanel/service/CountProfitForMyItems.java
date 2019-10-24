package myapp.MyAdminPanel.service;

import myapp.MyAdminPanel.model.MyItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountProfitForMyItems {

    public List<MyItem> countProfit(List<MyItem> myItems) {
        for (MyItem item : myItems) {
            if (item.getSellPrice() == null || item.getBuyPrice() == null || item.getBuyPrice() == 0) continue;
            double profit = Math.round((item.getSellPrice() - item.getBuyPrice()) / (item.getBuyPrice() * 0.01));
            item.setProfit(profit);
        }
        return myItems;
    }

}
