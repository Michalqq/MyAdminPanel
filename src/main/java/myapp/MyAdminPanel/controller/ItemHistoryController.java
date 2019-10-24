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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ItemHistoryController {

    private MyItemRepository myItemRepository;

    private Basket basket;

    @Autowired
    public ItemHistoryController(MyItemRepository myItemRepository, Basket basket) {
        this.myItemRepository = myItemRepository;
        this.basket = basket;
    }

    @RequestMapping(value = {"/itemhistory"}, params = "itemHistory", method = RequestMethod.POST)
    public ModelAndView addToBasket(@RequestParam(name = "checkedItemId", defaultValue = "0") int itemId,
                                    ModelAndView modelAndView,, HttpServletRequest request) {
        if (itemId == 0) {
            String referer = request.getHeader("Referer");
            modelAndView.setViewName("redirect:" + referer);
        } else {
            List<MyItem> items = myItemRepository.findAllByItemId(itemId);
            modelAndView.addObject("items", items);
            modelAndView.setViewName("itemhistory");
        }
        return modelAndView;
    }
}
