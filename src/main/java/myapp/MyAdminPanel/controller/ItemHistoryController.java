package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Basket;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

public class ItemHistoryController {

    private MyItemRepository myItemRepository;

    private Basket basket;

    @Autowired
    public ItemHistoryController(MyItemRepository myItemRepository, Basket basket){
        this.myItemRepository = myItemRepository;
        this.basket = basket;
    }

    @RequestMapping(value = {"/itemhistory"}, params = "itemHistory", method = RequestMethod.POST)
    public ModelAndView addToBasket(@RequestParam(name = "checkedItemId") int itemId,
                                    ModelAndView modelAndView) {
        modelAndView.setViewName("itemhistory");
        return modelAndView;
    }
}
