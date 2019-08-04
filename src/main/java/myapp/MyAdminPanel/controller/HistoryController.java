package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    MyItemRepository myItemRepository;

    @RequestMapping(value = "/history/{status}", method = RequestMethod.GET)
    public ModelAndView getHistory(@PathVariable int status,
                                   @RequestParam(value="startDate", defaultValue = "") String startDate,
                                   @RequestParam(value="stopDate", defaultValue = "") String stopDate){
        ModelAndView modelAndView = new ModelAndView();
        List<MyItem> myItems = new ArrayList<>();
        System.out.println(startDate);
        System.out.println(stopDate);
        if (status == 5) {
            myItems = myItemRepository.findAllByLastActionDateBetween("2019-08-01 00:00:01", "2019-08-04 23:59:59");
        } else if (status == 0) {
            myItems = myItemRepository.findAllByLastActionDateBetweenAndDeliveredToPolandIsNull("2019-08-01 00:00:01", "2019-08-04 23:59:59");
        } else{
            myItems = myItemRepository.findAllByLastActionDateBetweenAndDeliveredToPolandEquals("2019-08-01 00:00:01", "2019-08-04 23:59:59", status);
        }
        for (MyItem item : myItems) {
            if (item.getSellPrice() > 0) {
                double profit = Math.round(item.getSellPrice() - item.getBuyPrice()) / (item.getBuyPrice() * 0.01);
                item.setProfit(profit);
            } else item.setProfit(0.0);
        }
        modelAndView.addObject("myItems", myItems);
        modelAndView.setViewName("history");
        return modelAndView;
    }
}