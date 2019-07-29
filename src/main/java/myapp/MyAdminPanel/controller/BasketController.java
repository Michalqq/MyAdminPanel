package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Basket;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BasketController {
    @Autowired
    private MyItemRepository myItemRepository;

    @Autowired
    private Basket basket;

    @RequestMapping(value = {"/sell"}, params = "add", method = RequestMethod.POST)
    public ModelAndView home(@RequestParam(name = "sellPriceInput", required = true) Double sellPrice,
                             @RequestParam(name = "commissionInput", defaultValue = "0") int commission,
                             @RequestParam(name = "cashOnDelivery", defaultValue = "0") Double cashOnDelivery,
                             @RequestParam(name = "note", defaultValue = "") String note) {
        ModelAndView modelAndView = new ModelAndView();
        basket.setName(basket.getName() + " " + "test");
        basket.add(new MyItem());
        modelAndView.addObject("SellInfo", basket.getMyItemList().size());
        modelAndView.setViewName("index");
        return modelAndView;
    }
}

