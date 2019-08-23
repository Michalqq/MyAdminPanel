package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Basket;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.service.DBAction;
import myapp.MyAdminPanel.service.ItemsNameFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class HistoryController {

    @Autowired
    private MyItemRepository myItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private Basket basket;

    @Autowired
    private DBAction dbAction;

    @Autowired
    private ItemsNameFiller itemsNameFiller;

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
        itemsNameFiller.getItemsNames(myItems);
        modelAndView.addObject("myItems", myItems);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("stopDate", stopDate);
        modelAndView.addObject("basketsize", "Basket (" + basket.getMyItemList().size() + ")");
        modelAndView.setViewName("history");
        return modelAndView;
    }
    @RequestMapping(value = "/confirmCashOnDelivery", method = RequestMethod.POST)
    public ModelAndView confirmCashOnDelivery(@RequestParam(value = "id", required = true) int itemId,
                                              ModelAndView modelAndView){
        Optional<MyItem> myItem = myItemRepository.findById(itemId);
        if (myItem.isPresent()){
            dbAction.setDeliveredToPolStatus(myItem.get(), 3);
        }
        modelAndView.setViewName("redirect:/history");
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
}