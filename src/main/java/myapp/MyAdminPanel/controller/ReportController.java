package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.model.ItemToReport;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.model.MyItemSoldSum;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static jdk.nashorn.internal.objects.NativeMath.round;

@Controller
public class ReportController {

    private MyItemRepository myItemRepository;
    private ItemRepository itemRepository;
    private ItemToReport itemToReport;

    @Autowired
    public ReportController(MyItemRepository myItemRepository, ItemRepository itemRepository, ItemToReport itemToReport) {
        this.myItemRepository = myItemRepository;
        this.itemRepository = itemRepository;
        this.itemToReport = itemToReport;
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public ModelAndView getReport(ModelAndView modelAndView,
                                  @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                  @RequestParam(value = "stopDate", defaultValue = "") String stopDate) {
        if (startDate.equals("")) startDate = this.getYesterDay();
        if (stopDate.equals("")) stopDate = this.getToday();
        List<Item> items = itemRepository.findAll();
        List<ItemToReport> itemsToReport = new ArrayList<>();
        for (Item item : items) {
            itemsToReport.add(new ItemToReport());
            itemsToReport.get(itemsToReport.size() - 1).setItemId(item.getId());
            itemsToReport.get(itemsToReport.size() - 1).setName(item.getName());
        }
        this.getProfitSum(items, startDate, stopDate, itemsToReport);
        Collections.sort(itemsToReport);
        Collections.reverse(itemsToReport);
        modelAndView.addObject("myItems", itemsToReport);
        modelAndView.setViewName("report");
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("stopDate", stopDate);
        return modelAndView;
    }

    public void getProfitSum(List<Item> items, String startDate, String stopDate, List<ItemToReport> itemToReports) {
        int count = 0;
        for (Item item : items) {
            List<MyItem> myItemsTemp = myItemRepository.findAllItemsWhereSellDateBetween(startDate, stopDate, item.getId());
            double profit = round(myItemsTemp.stream().mapToDouble(x -> x.getSellPrice()).sum() - myItemsTemp.stream().mapToDouble(x -> x.getBuyPrice()).sum(), 2);
            if (profit == 0) {
                itemToReports.remove(count);
            } else {
                itemToReports.get(count).setSellPrice(profit);
                count++;
            }
        }
    }

    private String getYesterDay() {
        return DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDateTime.now().minusDays(1));
    }

    private String getToday() {
        return DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDateTime.now());
    }
}
