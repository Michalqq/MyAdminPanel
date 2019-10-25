package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Basket;
import myapp.MyAdminPanel.model.ItemToReport;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.service.CountProfitForMyItems;
import myapp.MyAdminPanel.service.DBAction;
import myapp.MyAdminPanel.service.ItemsNameFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class HistoryController {

    private MyItemRepository myItemRepository;
    private ItemRepository itemRepository;
    private Basket basket;
    private DBAction dbAction;
    private ItemsNameFiller itemsNameFiller;
    private CountProfitForMyItems countProfitForMyItems;

    @Autowired
    public HistoryController(MyItemRepository myItemRepository, ItemRepository itemRepository, Basket basket,
                             DBAction dbAction, ItemsNameFiller itemsNameFiller, CountProfitForMyItems countProfitForMyItems){
        this.myItemRepository = myItemRepository;
        this.itemsNameFiller = itemsNameFiller;
        this.itemRepository = itemRepository;
        this.basket = basket;
        this.dbAction = dbAction;
        this.countProfitForMyItems = countProfitForMyItems;
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ModelAndView getHistory(@RequestParam(value = "histStatus", defaultValue = "4") int status,
                                   @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                   @RequestParam(value = "stopDate", defaultValue = "") String stopDate,
                                   @RequestParam(value = "showPobrania", defaultValue = "") String test,
                                   ModelAndView modelAndView) {
        System.out.println(test);
        List<MyItem> myItems = getHistoryByStatus(status, startDate, stopDate, modelAndView);
        myItems = countProfitForMyItems.countProfit(myItems);
        itemsNameFiller.getItemsNames(myItems);
        modelAndView.addObject("myItems", myItems);
        basket.addInfoAboutBasketSize(modelAndView);
        modelAndView.setViewName("history");
        return modelAndView;
    }

    @RequestMapping(value="/history", params = "showPobrania", method = RequestMethod.GET)
    public ModelAndView getPobrania(ModelAndView modelAndView){
        List<MyItem> myItems = myItemRepository.findAllByDeliveredToPoland(2);
        myItems = countProfitForMyItems.countProfit(myItems);
        itemsNameFiller.getItemsNames(myItems);
        modelAndView.addObject("myItems", myItems);
        basket.addInfoAboutBasketSize(modelAndView);
        modelAndView.setViewName("history");
        return modelAndView;
    }

    @RequestMapping(value = "/confirmCashOnDelivery", method = RequestMethod.POST)
    public ModelAndView confirmCashOnDelivery(@RequestParam(value = "id", required = true) int itemId,
                                              ModelAndView modelAndView, HttpServletRequest request) {
        Optional<MyItem> myItem = myItemRepository.findById(itemId);
        if (myItem.isPresent()) {
            dbAction.setDeliveredToPolStatus(myItem.get(), 3);
        }
        String referer = request.getHeader("Referer");
        modelAndView.setViewName("redirect:" + referer);
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

    private List<MyItem> getHistoryByStatus(int status, String startDate, String stopDate, ModelAndView modelAndView) {
        List<MyItem> myItems;
        if (startDate.equals("")) startDate = this.getYesterDay();
        if (stopDate.equals("")) stopDate = this.getToday();
        if (status == 4) {
            myItems = myItemRepository.findAllByLastActionDateBetween(getFullStartDate(startDate), getFullStopDate(stopDate));
        } else if (status == 0) {
            myItems = myItemRepository.findAllByLastActionDateBetweenAndDeliveredToPolandIsNull(getFullStartDate(startDate), getFullStopDate(stopDate));
        } else if (status == 5) {
            myItems = myItemRepository.getAllWhereBuyDateIsBetween(getFullStartDate(startDate), getFullStopDate(stopDate));
        } else {
            myItems = myItemRepository.findAllByLastActionDateBetweenAndDeliveredToPolandEquals(getFullStartDate(startDate), getFullStopDate(stopDate), status);
        }
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("stopDate", stopDate);
        modelAndView.addObject("totalBuyPrice", Math.round(myItems.stream().mapToDouble(x -> x.getBuyPrice()).sum()));
        return myItems;
    }
}