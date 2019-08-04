package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.model.Seller;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.SellersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AddItemController {


    @Autowired
    ItemRepository itemRepository;

    @Autowired
    SellersRepository sellersRepository;

    @RequestMapping("/additem")
    ModelAndView getAddItem(ModelAndView modelAndView){

        List<Item> items = itemRepository.findAll();
        modelAndView.addObject("Items", items);
        List<Seller> sellers = sellersRepository.findAll();
        modelAndView.addObject("sellers", sellers);
        modelAndView.setViewName("additem");
        return modelAndView;
    }

    @RequestMapping(value = "/additem", method = RequestMethod.POST)
    ModelAndView addNewItem(@RequestParam(value = "histStatus", defaultValue = "4") int status,
                            @RequestParam(value = "startDate", defaultValue = "") String startDate,
                            @RequestParam(value = "stopDate", defaultValue = "") String stopDate) {


        return "redirect:/additem";
    }

}
