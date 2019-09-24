package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Basket;
import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.model.Seller;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.repository.SellersRepository;
import myapp.MyAdminPanel.service.DBAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AddItemController {

    @Autowired
    private MyItemRepository myItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SellersRepository sellersRepository;

    @Autowired
    private Basket basket;

    @Autowired
    private DBAction dbAction;

    @RequestMapping(value = "/additem", method = RequestMethod.GET)
    ModelAndView getAddItem(ModelAndView modelAndView,
                            @RequestParam(value = "info", defaultValue = "") String info) {
        List<Item> items = itemRepository.findAll();
        modelAndView.addObject("Items", items);
        List<Seller> sellers = sellersRepository.findAll();
        modelAndView.addObject("sellers", sellers);
        modelAndView.addObject("addInfo", info);
        modelAndView.addObject("newItems", this.itemList(200));
//        List<Item> tempItems = new ArrayList<>();
//        tempItems.add(new Item(1, "  "));
//        modelAndView.addObject("newItems", tempItems);
        modelAndView.setViewName("additem");
        basket.addInfoAboutBasketSize(modelAndView);
        return modelAndView;
    }

    @RequestMapping(value = "/additem", method = RequestMethod.POST)
    String addNewItem(@RequestParam(value = "itemId", defaultValue = "0") int itemId,
                      @RequestParam(value = "buyPrice", defaultValue = "0.0") double buyPrice,
                      @RequestParam(value = "quantity", defaultValue = "0") int quantity,
                      @RequestParam(value = "note", defaultValue = "") String note,
                      @RequestParam(value = "sellerId", defaultValue = "0") int sellerId,
                      @RequestParam(value = "action", required = true) String actionValue,
                      @RequestParam(value = "newItemId", defaultValue = "0") int newItemId,
                      @RequestParam(value = "itemName", defaultValue = "") String newItemName) {
        if (actionValue.equals("addItem")) {
            for (int i = 0; i < quantity; i++) {
                dbAction.createNewItem(buyPrice / quantity, itemId, sellerId, note);
            }
        }
        if (actionValue.equals("addNewItem")) {
            if (!itemRepository.findById(newItemId).isPresent()) {
                itemRepository.save(new Item(newItemId, newItemName));
            } else {
                String info = "To id jest używane";
                return ("/additem?info=" + info);
            }
        }
        return ("/additem");
    }

    public List<Item> itemList(int maxId) {
        List<Integer> newIdList = new ArrayList<>();
        for (int i = 1; i < maxId; i++) {
            newIdList.add(i);
        }
        List<Item> itemList = new ArrayList<>();
        for (Integer id : newIdList) {
            Optional<Item> item = itemRepository.findById(id);
            if (item.isPresent()) itemList.add(item.get());
            else itemList.add(new Item(id, " "));
        }
        return itemList;
    }
}
