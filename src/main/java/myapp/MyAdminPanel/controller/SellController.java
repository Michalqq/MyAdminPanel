package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Controller
public class SellController {

    @Autowired
    MyItemRepository myItemRepository;

    @RequestMapping(value = {"/sell"}, method = RequestMethod.GET)
    public String home(@RequestParam(name = "sellPriceInput", required = true) Double sellPrice,
                       @RequestParam(name = "commissionInput", defaultValue = "0") int commission,
                       @RequestParam(name = "cashOnDelivery") Double cashOnDelivery,
                       @RequestParam(name = "note") String note) {
        Optional<MyItem> myItemFromDB = myItemRepository.findById(322);
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
        return "index";
    }

}
