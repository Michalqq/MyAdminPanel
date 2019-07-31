package myapp.MyAdminPanel.controller;


import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.model.User;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private MyItemRepository myItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value = "/chart")
    public ModelAndView chart() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("chart");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("register");

        }
        return modelAndView;
    }

    @RequestMapping(value = {"/", "/index", "/sell"}, method = RequestMethod.GET)
    public ModelAndView home(@RequestParam(value = "searchItem", required = false, defaultValue = "") String name) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if (user == null) modelAndView.setViewName("login");
        else {
            List<MyItem> myItems = myItemRepository.findItemsOnStockGroupByItemId(1);
            myItems = getItemsNames(myItems, this.getItemsMap());
            if (name != null && name != "") {
                myItems = getByName(myItems, name);
            }
            this.getQuantityOfItems(myItems);
            modelAndView.addObject("myItems", myItems);
            modelAndView.addObject("test", myItems.get(0).getName());
            System.out.println(myItems.get(0).getName());
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }

    @RequestMapping(value = {"/delivery"}, method = RequestMethod.GET)
    public ModelAndView delivery(@RequestParam(value = "searchItem", required = false, defaultValue = "") String
                                         name) {
        ModelAndView modelAndView = new ModelAndView();
        List<MyItem> myItems = myItemRepository.findItemsInTransport();
        myItems = getItemsNames(myItems, this.getItemsMap());
        if (name != null && name != "") {
            myItems = getByName(myItems, name);
        }
        for (MyItem myItem : myItems) {
            myItem.setQuantity(myItemRepository.countItemIdBySellPriceIsNullAndDeliveredToPolandIsNullAndItemId(myItem.getItemId()));
        }
        modelAndView.addObject("myItems", myItems);
        modelAndView.setViewName("delivery");
        return modelAndView;
    }

    public Map<Integer, String> getItemsMap(){
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

    public static List<MyItem> getByName(List<MyItem> itemList, String name) {
        return itemList.stream().filter(x -> x.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
    }
    public void getQuantityOfItems(List<MyItem> myItems){
        for (MyItem myItem : myItems) {
            myItem.setQuantity(myItemRepository.countItemIdBySellPriceIsNullAndDeliveredToPolandIsAndItemId(1, myItem.getItemId()));
            myItem.setQuantInTransport(myItemRepository.countItemIdBySellPriceIsNullAndDeliveredToPolandIsNullAndItemId(myItem.getItemId()));
        }
    }


}
