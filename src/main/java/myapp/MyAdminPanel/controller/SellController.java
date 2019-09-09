package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Basket;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.service.DBAction;
import myapp.MyAdminPanel.service.ItemsNameFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class SellController {

    @Autowired
    private MyItemRepository myItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private Basket basket;

    @Autowired
    private DBAction dbAction;

    @Autowired
    private ItemsNameFiller itemsNameFiller;

    @RequestMapping(value = {"/sell"}, params = "sell", method = RequestMethod.POST)
    public ModelAndView sellItem(@RequestParam(name = "sellPriceInput", required = true) Double sellPrice,
                                 @RequestParam(name = "commissionInput", defaultValue = "0") int commission,
                                 @RequestParam(name = "cashOnDelivery", defaultValue = "0") Double cashOnDelivery,
                                 @RequestParam(name = "note", defaultValue = "") String note,
                                 @RequestParam(name = "checkedItemId") int itemId,
                                 ModelAndView modelAndView) {
        Optional<MyItem> myItemFromDB = myItemRepository.findById(itemId);
        if (saveSellToDb(myItemFromDB.get(), commission, sellPrice, note, cashOnDelivery)) {
            modelAndView.addObject("SellInfo", "Updated sell price for:" + myItemFromDB.get().getName());
        } else modelAndView.addObject("SellInfo", "ERROR:  Item doesn't exist");
        modelAndView.addObject("SellInfo", "Sprzedano za: " + myItemFromDB.get().getSellPrice());
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
    }

    public boolean saveSellToDb(MyItem myItemFromDB, int commission, Double sellPrice, String note, Double cashOnDelivery) {
        dbAction.setSellPrice(myItemFromDB, sellPrice - (sellPrice * commission * 0.01));
        dbAction.setNote(myItemFromDB, note);
        dbAction.setDeliveredToPolStatus(myItemFromDB, 3);
        if (cashOnDelivery != null && cashOnDelivery != 0) {
            dbAction.setCashOnDelivery(myItemFromDB, cashOnDelivery);
        }
        return true;
    }

    @RequestMapping(value = {"/sell"}, params = "add", method = RequestMethod.POST)
    public ModelAndView addToBasket(@RequestParam(name = "sellPriceInput", required = true) Double sellPrice,
                                    @RequestParam(name = "commissionInput", defaultValue = "0") int commission,
                                    @RequestParam(name = "cashOnDelivery", defaultValue = "0") Double cashOnDelivery,
                                    @RequestParam(name = "note", defaultValue = "") String note,
                                    @RequestParam(name = "checkedItemId") int itemId,
                                    ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        Optional<MyItem> item = myItemRepository.findById(itemId);
        if (saveSellToDb(item.get(), commission, sellPrice, note, cashOnDelivery)) {
            basket.add(item);
            itemsNameFiller.getItemsNames(basket.getMyItemList());
        } else modelAndView.addObject("SellInfo", "ERROR:  Item doesn't exist");
        redirectAttributes.addFlashAttribute("SellInfo", "Dodano do koszyka: " + item.get().getName());
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
    }

    @RequestMapping(value = {"/remove/{itemId}"}, params = "remove", method = RequestMethod.POST)
    public ModelAndView removeItemFromBasket(@PathVariable int itemId, ModelAndView modelAndView) {
        for (int i = 0; i < basket.getMyItemList().size(); i++) {
            if (basket.getMyItemList().get(i).getId() == itemId) {
                dbAction.clearSellPriceAndDate(basket.getMyItemList().get(i));
                basket.getMyItemList().remove(i);
            }
        }
        modelAndView.setViewName("redirect:/basket");
        return modelAndView;
    }

    @RequestMapping(value = {"/basket"}, method = RequestMethod.GET)
    public ModelAndView getBasket(ModelAndView modelAndView) {
        modelAndView.addObject("myBasket", basket.getMyItemList());
        if (basket.getMyItemList().size() > 0) {
            modelAndView.addObject("cashOnDelivery", basket.getMyItemList().get(0).getCashOnDelivery());
            modelAndView.addObject("note", basket.getMyItemList().get(0).getNotes());
        }
        modelAndView.setViewName("basket");
        basket.addInfoAboutBasketSize(modelAndView);
        return modelAndView;
    }

    @RequestMapping(value = "/basket", params = "sell", method = RequestMethod.POST)
    public ModelAndView sellItemsInBasket(@RequestParam(name = "note", defaultValue = "") String note,
                                          @RequestParam(name = "cashOnDelivery", defaultValue = "") Double cashOnDelivery,
                                          ModelAndView modelAndView) {
        for (MyItem item : basket.getMyItemList()) {
            saveSellToDb(item, 0, item.getSellPrice(), note, cashOnDelivery);
        }
        basket.getMyItemList().clear();
        modelAndView.setViewName("redirect:/basket");
        return modelAndView;
    }

    @RequestMapping(value = "/basket", params = "clearBasket", method = RequestMethod.POST)
    public ModelAndView clearBasket() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/index");
        for (int i = 0; i < basket.getMyItemList().size(); i++) {
            dbAction.clearSellPriceAndDate(basket.getMyItemList().get(i));
        }
        basket.getMyItemList().clear();
        return modelAndView;
    }
}
