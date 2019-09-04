package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Basket;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.service.DBAction;
import myapp.MyAdminPanel.service.ItemsNameFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DeliveryController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MyItemRepository myItemRepository;

    @Autowired
    private DBAction dbAction;

    @Autowired
    private Basket basket;

    @Autowired
    private ItemsNameFiller itemsNameFiller;

    @RequestMapping(value = {"/delivery"}, params = "confirmDelivery", method = RequestMethod.POST)
    public ModelAndView setQuantityOfDelivered(@RequestParam(name = "checkedItemId") int itemId,
                                               @RequestParam(name = "quantityDelivered") int quantity,
                                               ModelAndView modelAndView) {
        List<MyItem> items = myItemRepository.findItemInTransportByItemId(itemId);
        for (int i = 0; i < quantity; i++) {
            dbAction.setDeliveredToPolStatus(items.get(i), 1);
        }
        modelAndView.setViewName("redirect:/delivery");
        return modelAndView;
    }

    @RequestMapping(value = {"/delivery"}, params = "details", method = RequestMethod.POST)
    public ModelAndView getListOfItem(@RequestParam(value = "id", required = true) int id,
                                      ModelAndView modelAndView) {
        List<MyItem> myItems = myItemRepository.findItemInTransportByItemId(id);
        itemsNameFiller.getItemsNames(myItems);
        for (MyItem item : myItems) {
            item.setQuantity(1);
        }
        modelAndView.addObject("basketsize", "Basket (" + basket.getMyItemList().size() + ")");
        modelAndView.addObject("myItems", myItems);
        modelAndView.setViewName("delivery");
        return modelAndView;
    }
}
