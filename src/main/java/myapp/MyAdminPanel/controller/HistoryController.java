package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    MyItemRepository myItemRepository;

    @RequestMapping(value = "/history/{status}", method = RequestMethod.GET)
    public ModelAndView getHistory(@PathVariable int status) {
        ModelAndView modelAndView = new ModelAndView();
        List<MyItem> myItems = myItemRepository.findAllByLastActionDateBetween("2019-08-02 23:00:00", "2019-08-01 00:00:00");
        for (MyItem item:myItems){
            if (item.getSellPrice() > 0) {
                item.setProfit((item.getSellPrice() - item.getBuyPrice()) / (item.getBuyPrice() * 0.01));
            } else item.setProfit(0.0);
        }
        System.out.println(myItems.size());
        modelAndView.addObject("myItems", myItems);
        modelAndView.setViewName("history");
        return modelAndView;
    }
}