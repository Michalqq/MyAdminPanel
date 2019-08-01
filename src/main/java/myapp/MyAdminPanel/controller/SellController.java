package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Basket;
import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class SellController {

    @Autowired
    private MyItemRepository myItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private Basket basket;

    @RequestMapping(value = {"/sell"}, params = "sell", method = RequestMethod.POST)
    public ModelAndView sellItem(@RequestParam(name = "sellPriceInput", required = true) Double sellPrice,
                                 @RequestParam(name = "commissionInput", defaultValue = "0") int commission,
                                 @RequestParam(name = "cashOnDelivery", defaultValue = "0") Double cashOnDelivery,
                                 @RequestParam(name = "note", defaultValue = "") String note,
                                 @RequestParam(name = "checkedItemId") int itemId) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<MyItem> myItemFromDB = myItemRepository.findById(itemId);
        if (saveSellToDb(myItemFromDB, commission, sellPrice, note, cashOnDelivery)) {
            modelAndView.addObject("SellInfo", "Updated sell price for:" + myItemFromDB.get().getName());
        } else modelAndView.addObject("SellInfo", "ERROR:  Item doesn't exist");
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
    }

    public boolean saveSellToDb(Optional<MyItem> myItemFromDB, int commission, Double sellPrice, String note, Double cashOnDelivery) {
        if (myItemFromDB.isPresent()) {
            myItemFromDB.get().setSellPrice(sellPrice - (sellPrice * commission * 0.01));
            myItemFromDB.get().setSellDate(DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDate.now()));
            myItemFromDB.get().setLastActionDate(LocalDateTime.now().plusHours(2));
            myItemFromDB.get().setNotes(note);
            myItemFromDB.get().setDeliveredToPoland(3);
            if (cashOnDelivery != null && cashOnDelivery != 0) {
                myItemFromDB.get().setCashOnDelivery(cashOnDelivery);
                myItemFromDB.get().setDeliveredToPoland(2);
                myItemFromDB.get().setIfCashOnDelivery(1);
            }
            myItemRepository.save(myItemFromDB.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean saveSellToDb(MyItem myItemFromDB, int commission, Double sellPrice, String note, Double cashOnDelivery) {
        myItemFromDB.setSellPrice(sellPrice - (sellPrice * commission * 0.01));
        myItemFromDB.setSellDate(DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDate.now()));
        myItemFromDB.setLastActionDate(LocalDateTime.now().plusHours(2));
        myItemFromDB.setNotes(note);
        myItemFromDB.setDeliveredToPoland(3);
        if (cashOnDelivery != null && cashOnDelivery != 0) {
            myItemFromDB.setCashOnDelivery(cashOnDelivery);
            myItemFromDB.setDeliveredToPoland(2);
            myItemFromDB.setIfCashOnDelivery(1);
        }
        myItemRepository.save(myItemFromDB);
        return true;
    }

    public boolean setDeliveredStatus(MyItem myItemFromDB, int status) {
        myItemFromDB.setDeliveredToPoland(status);
        myItemRepository.save(myItemFromDB);
        return true;
    }

    public boolean clearSellPriceAndDate(MyItem myItemFromDB) {
        myItemFromDB.setDeliveredToPoland(1);
        myItemFromDB.setSellDate(null);
        myItemFromDB.setSellPrice(null);
        myItemRepository.save(myItemFromDB);
        return true;
    }

    @RequestMapping(value = {"/sell"}, params = "add", method = RequestMethod.POST)
    public ModelAndView addToBasket(@RequestParam(name = "sellPriceInput", required = true) Double sellPrice,
                                    @RequestParam(name = "commissionInput", defaultValue = "0") int commission,
                                    @RequestParam(name = "cashOnDelivery", defaultValue = "0") Double cashOnDelivery,
                                    @RequestParam(name = "note", defaultValue = "") String note,
                                    @RequestParam(name = "checkedItemId") int itemId) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<MyItem> item = myItemRepository.findById(itemId);
        if (saveSellToDb(item, commission, sellPrice, note, cashOnDelivery)) {
            basket.add(item);
            getItemsNames(basket.getMyItemList(), getItemsMap());
        } else modelAndView.addObject("SellInfo", "ERROR:  Item doesn't exist");
        modelAndView.addObject("SellInfo", basket.getMyItemList().size());
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
    }

    @RequestMapping(value = {"/remove"}, params = "remove", method = RequestMethod.POST)
    public ModelAndView removeItemFromBasket(@RequestParam(name = "removeId") int itemId) {
        ModelAndView modelAndView = new ModelAndView();
        for (int i = 0; i < basket.getMyItemList().size(); i++) {
            if (basket.getMyItemList().get(i).getId() == itemId) {
                clearSellPriceAndDate(basket.getMyItemList().get(i));
                basket.getMyItemList().remove(i);
            }
        }
        modelAndView.setViewName("redirect:/basket");
        return modelAndView;
    }

    @RequestMapping(value = {"/basket"}, method = RequestMethod.GET)
    public ModelAndView getBasket() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("myBasket", basket.getMyItemList());
        if (basket.getMyItemList().size() > 0) {
            modelAndView.addObject("cashOnDelivery", basket.getMyItemList().get(0).getCashOnDelivery());
            modelAndView.addObject("note", basket.getMyItemList().get(0).getNotes());
        }
        modelAndView.setViewName("basket");
        return modelAndView;
    }

    @RequestMapping(value = "/basket", params = "sell", method = RequestMethod.POST)
    public ModelAndView sellItemsInBasket(@RequestParam(name = "note", defaultValue = "") String note,
                                          @RequestParam(name = "cashOnDelivery", defaultValue = "") Double cashOnDelivery) {
        ModelAndView modelAndView = new ModelAndView();
        for (MyItem item : basket.getMyItemList()) {
            saveSellToDb(item, 0, item.getSellPrice(), note, cashOnDelivery);
        }
        modelAndView.setViewName("redirect:/basket");
        return modelAndView;

    }

    @RequestMapping(value = "/basket", params = "clearBasket", method = RequestMethod.POST)
    public ModelAndView clearBasket() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/index");
        for (int i = 0; i < basket.getMyItemList().size(); i++) {
            clearSellPriceAndDate(basket.getMyItemList().get(i));
        }
        basket.getMyItemList().clear();
        return modelAndView;
    }


    @RequestMapping(value = {"/delivery"}, params = "confirmDelivery", method = RequestMethod.POST)
    public ModelAndView setQuantityOfDelivered(@RequestParam(name = "checkedItemId") int itemId,
                                               @RequestParam(name = "quantityDelivered") int quantity) {
        ModelAndView modelAndView = new ModelAndView();
        List<MyItem> items = myItemRepository.findItemInTransportByItemId(itemId);
        for (int i = 0; i < quantity; i++) {
            setDeliveredStatus(items.get(i), 1);
        }
        modelAndView.setViewName("redirect:/delivery");
        return modelAndView;
    }

    public Map<Integer, String> getItemsMap() {
        List<Item> items = itemRepository.findAll();
        Map<Integer, String> itemMap = new HashMap<>();
        for (Item item : items) {
            itemMap.put(item.getId(), item.getName());
        }
        return itemMap;
    }

    public static List<MyItem> getItemsNames(List<MyItem> itemList, Map<Integer, String> itemMap) {
        for (MyItem myItem : itemList) {
            myItem.setName(itemMap.get(myItem.getItemId()));
        }
        return itemList;
    }

}
