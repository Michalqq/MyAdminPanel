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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

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

    @RequestMapping("/")
    public ModelAndView findByName(@RequestParam(value = "searchItem", required = false, defaultValue = "") String name, ModelAndView modelAndView) {
        List<Item> items = itemRepository.findAll(); //TODO let's refactor below code
        List<MyItem> myItems = myItemRepository.findBySellPriceIsNullAndDeliveredToPolandIs(1);
        List<MyItem> myItems2 = new ArrayList<>();
        int checkpoint = 0;
        for (MyItem myItem : myItems) {
            checkpoint = 0;
            myItem.setQuantity(1);
            for (Item item : items) {
                if (item.getId() == myItem.getItemId() && item.getName().contains(name)) myItem.setName(item.getName());
            }
            for (MyItem uniqueItems : myItems2) {
                if (uniqueItems.getItemId() == myItem.getItemId()) {
                    checkpoint = 1;
                    uniqueItems.addQuantity();
                }
            }
            if (checkpoint == 0 && myItem.getName() != null) {
                myItems2.add(myItem);
            }
        }
        modelAndView = new ModelAndView();
        modelAndView.addObject("myItems", myItems2);
        modelAndView.setViewName("index");
        return modelAndView;
    }


    @RequestMapping(value = {"/", "/login", "/index"}, method = RequestMethod.GET)
    public ModelAndView home(@RequestParam(value = "searchItem", required = false, defaultValue = "") String name) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if (user == null) modelAndView.setViewName("login");
        else {
            List<Item> items = itemRepository.findAll(); //TODO let's refactor below code
            Map<Integer, String> itemMap = new HashMap<>();
            for(Item item : items){
                itemMap.put(item.getId(), item.getName());
            }
            List<MyItem> myItems = myItemRepository.findBySellPriceIsNullAndDeliveredToPolandIs(1);
            myItems = getItemsNames(myItems, itemMap);
            List<MyItem> myItems2 = new ArrayList<>();
            int checkpoint = 0;
            for (MyItem myItem : myItems) {
                checkpoint = 0;
                myItem.setQuantity(1);
                for (MyItem uniqueItems : myItems2) {
                    if (uniqueItems.getItemId() == myItem.getItemId()) {
                        checkpoint = 1;
                        uniqueItems.addQuantity();
                    }
                }
                if (checkpoint == 0) {
                    if (name == null) myItems2.add(myItem);
                    else if (myItem.getName().toLowerCase().contains(name.toLowerCase())){
                        System.out.println(name);
                        myItems2.add(myItem);
                    }
                }
            }
            modelAndView.addObject("myItems", myItems2);
            //modelAndView.addObject("items", itemRepository.findByIdLessThan(10));
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }
    @RequestMapping(value = {"delivery"}, method = RequestMethod.GET)
    public ModelAndView delivery(@RequestParam(value = "searchItem", required = false, defaultValue = "") String name) {
        ModelAndView modelAndView = new ModelAndView();
//        List<Item> items = itemRepository.findAll(); //TODO let's refactor below code
//        Map<Integer, String> itemMap = new HashMap<>();
//        for(Item item : items){
//            itemMap.put(item.getId(), item.getName());
//        }
//        List<MyItem> itemsInTransport = myItemRepository.findBySellPriceIsNullAndDeliveredToPolandIsNull();
//        itemsInTransport = getItemsNames(itemsInTransport, itemMap);
//
//        modelAndView.addObject("itemsInTransport", itemsInTransport);
        return modelAndView;
    }

    public static List<MyItem> getItemsNames(List<MyItem> itemList, Map<Integer, String> itemMap){
        for (MyItem myItem:itemList) {
            myItem.setName(itemMap.get(myItem.getItemId()));
        }
        return itemList;
    }



}
