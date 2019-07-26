package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.function.DoublePredicate;

@Controller
public class SellController {

    @Autowired
    private MyItemRepository myItemRepository;

    @RequestMapping(value = {"/sell"},params = "sell", method = RequestMethod.POST)
    public ModelAndView home(@RequestParam(name = "sellPriceInput", required = true) Double sellPrice,
                             @RequestParam(name = "commissionInput", defaultValue = "0") int commission,
                             @RequestParam(name = "cashOnDelivery", defaultValue = "0") Double cashOnDelivery,
                             @RequestParam(name = "note", defaultValue = "") String note) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<MyItem> myItemFromDB = myItemRepository.findById(343);
        if (saveSellToDb(myItemFromDB, commission, sellPrice, note, cashOnDelivery)) {
            modelAndView.addObject("SellInfo", "Updated sell price for:" + myItemFromDB.get().getName());
        } else modelAndView.addObject("SellInfo", "ERROR:  Item doesn't exist");
        modelAndView.setViewName("index");
        return modelAndView;
    }

    public boolean saveSellToDb(Optional<MyItem> myItemFromDB, int commission, Double sellPrice, String note, Double cashOnDelivery) {
        if (myItemFromDB.isPresent()) {
            myItemFromDB.get().setSellPrice(sellPrice - (sellPrice * commission * 0.01));
            myItemFromDB.get().setSellDate(DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDate.now()));
            myItemFromDB.get().setLastActionDate(LocalDateTime.now().plusHours(2));
            myItemFromDB.get().setNotes(note);
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

}
