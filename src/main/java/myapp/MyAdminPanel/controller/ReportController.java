package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Item;
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
import java.util.List;

@Controller
public class ReportController {

    private MyItemRepository myItemRepository;
    private ItemRepository itemRepository;
    private MyItemSoldSum myItemSoldSum;

    @Autowired
    public ReportController(MyItemRepository myItemRepository, ItemRepository itemRepository, MyItemSoldSum myItemSoldSum) {
        this.myItemRepository = myItemRepository;
        this.itemRepository = itemRepository;
        this.myItemSoldSum = myItemSoldSum;
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public ModelAndView getReport(ModelAndView modelAndView,
                                  @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                  @RequestParam(value = "stopDate", defaultValue = "") String stopDate) {
        List<Item> items = itemRepository.findAll();
        List<MyItemSoldSum> myItemSoldSums = new ArrayList<>();
        for (Item item : items){
            myItemSoldSums.add(new MyItemSoldSum());
            myItemSoldSums.get(myItemSoldSums.size()-1).setItemId(item.getId());
            myItemSoldSums.get(myItemSoldSums.size()-1).setName(item.getName());
        }
        modelAndView.addObject("myItems", myItemSoldSums);
        modelAndView.setViewName("report");
        return modelAndView;
    }

    public List<Double> getProfitSum(List<Item> items, String startDate, String stopDate, ModelAndView modelAndView) {
        if (startDate.equals("")) startDate = this.getYesterDay();
        if (stopDate.equals("")) stopDate = this.getToday();
        List<Double> myItems = new ArrayList<>();
        for (Item item : items) {
            List<MyItem> myItemsTemp = myItemRepository.findAllItemsWhereSellDateBetween(startDate, stopDate, item.getId());
            double profit = myItemsTemp.stream().mapToDouble(x -> x.getSellPrice()).sum() - myItemsTemp.stream().mapToDouble(x -> x.getBuyPrice()).sum();
            myItems.add(myItemsTemp.stream().mapToDouble(x -> x.getSellPrice()).sum() - myItemsTemp.stream().mapToDouble(x -> x.getBuyPrice()).sum());
        }
        return myItems;
    }

    private String getYesterDay() {
        return DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDateTime.now().minusDays(1));
    }

    private String getToday() {
        return DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDateTime.now());
    }
}
