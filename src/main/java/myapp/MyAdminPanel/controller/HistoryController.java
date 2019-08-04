package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

@Controller
public class HistoryController {

    @Autowired
    MyItemRepository myItemRepository;

    @Autowired
    ItemRepository itemRepository;


    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ModelAndView getHistory(@RequestParam(value = "histStatus", defaultValue = "4") int status,
                                   @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                   @RequestParam(value = "stopDate", defaultValue = "") String stopDate) {
        ModelAndView modelAndView = new ModelAndView();
        List<MyItem> myItems = new ArrayList<>();
        if (startDate.equals("")) startDate = this.getYesterDay();
        if (stopDate.equals("")) stopDate = this.getToday();
        if (status == 4) {
            myItems = myItemRepository.findAllByLastActionDateBetween(getFullStartDate(startDate), getFullStopDate(stopDate));
        } else if (status == 0) {
            myItems = myItemRepository.findAllByLastActionDateBetweenAndDeliveredToPolandIsNull(getFullStartDate(startDate), getFullStopDate(stopDate));
        } else {
            myItems = myItemRepository.findAllByLastActionDateBetweenAndDeliveredToPolandEquals(getFullStartDate(startDate), getFullStopDate(stopDate), status);
        }
        this.countProfit(myItems);
        getItemsNames(myItems, this.getItemsMap());
        modelAndView.addObject("myItems", myItems);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("stopDate", stopDate);
        modelAndView.setViewName("history");
        return modelAndView;
    }

    private String getYesterDay() {
        return DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDateTime.now().minusDays(1));
    }

    private String getToday() {
        return DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDateTime.now());
    }

    private String getFullStopDate(String date) {
        return date += " 23:59:59";
    }

    private String getFullStartDate(String date) {
        return date += " 00:00:01";
    }

    private void countProfit(List<MyItem> myItems) {
        for (MyItem item : myItems) {
            if (item.getSellPrice() == null || item.getBuyPrice() == null || item.getBuyPrice()==0) continue;
            double profit = Math.round((item.getSellPrice() - item.getBuyPrice()) / (item.getBuyPrice() * 0.01));
            item.setProfit(profit);
        }
    }

    public Map<Integer, String> getItemsMap() {
        List<Item> items = itemRepository.findAll();
        Map<Integer, String> itemMap = new HashMap<>();
        for (Item item : items) {
            itemMap.put(item.getId(), item.getName());
        }
        return itemMap;
    }

    public static void getItemsNames(List<MyItem> itemList, Map<Integer, String> itemMap) {
        for (MyItem myItem : itemList) {
            myItem.setName(itemMap.get(myItem.getItemId()));
        }
    }
}