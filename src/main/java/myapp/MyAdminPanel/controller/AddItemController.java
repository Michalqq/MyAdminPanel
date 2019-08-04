package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.model.Seller;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.repository.SellersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class AddItemController {

    @Autowired
    MyItemRepository myItemRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    SellersRepository sellersRepository;

    @RequestMapping("/additem")
    ModelAndView getAddItem(ModelAndView modelAndView) {

        List<Item> items = itemRepository.findAll();
        modelAndView.addObject("Items", items);
        List<Seller> sellers = sellersRepository.findAll();
        modelAndView.addObject("sellers", sellers);
        modelAndView.setViewName("additem");
        return modelAndView;
    }

    @RequestMapping(value = "/additem", method = RequestMethod.POST)
    String addNewItem(@RequestParam(value = "itemId", defaultValue = "") int itemId,
                      @RequestParam(value = "buyPrice", defaultValue = "") double buyPrice,
                      @RequestParam(value = "quantity", defaultValue = "") int quantity,
                      @RequestParam(value = "sellerId", defaultValue = "")
                              int sellerId) {
        for (int i = 0; i < quantity; i++) {
            MyItem myItem = new MyItem(); //todo check all variables
            myItem.setBuyPrice(buyPrice / quantity);
            myItem.setItemId(itemId);
            myItem.setSellerId(sellerId);
            myItem.setBuyDate(DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDate.now()));
            myItem.setLastActionDate(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));

            myItemRepository.save(myItem);
        }
        return "redirect:/additem";
    }

}
