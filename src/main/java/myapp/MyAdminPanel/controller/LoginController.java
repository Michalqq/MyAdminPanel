package myapp.MyAdminPanel.controller;


import myapp.MyAdminPanel.model.Basket;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private Basket basket;

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

    @RequestMapping(value = {"/edit"}, params = "edit", method = RequestMethod.POST)
    public ModelAndView editItem(ModelAndView modelAndView,
                                 @RequestParam(name = "id", required = true) int itemId) {
        Optional<MyItem> myItem = myItemRepository.findById(itemId);
        if (myItem.isPresent()) {
            myItem.get().setName(getItemsMap().get(myItem.get().getItemId()));
            modelAndView.addObject("myItem", myItem.get());
        }
        modelAndView.setViewName("edit");
        return modelAndView;
    }

    @RequestMapping(value = {"/edit"}, params = "apply", method = RequestMethod.POST)
    public ModelAndView editItemApply(ModelAndView modelAndView,
                                      @RequestParam(name = "id", required = true) int itemId,
                                      @RequestParam(name = "buyPrice", defaultValue = "0") double buyPrice,
                                      @RequestParam(name = "buyDate") String buyDate,
                                      @RequestParam(name = "sellPrice", defaultValue = "0") double sellPrice,
                                      @RequestParam(name = "sellDate") String sellDate,
                                      @RequestParam(name = "cashOnDelivery", defaultValue = "0") double cashOnDelivery,
                                      @RequestParam(name = "note") String note,
                                      @RequestParam(name = "deliveredToPoland") int deliveredToPoland) {

        Optional<MyItem> myItem = myItemRepository.findById(itemId);
        if (myItem.isPresent()) {
            myItem.get().setBuyPrice(buyPrice);
            myItem.get().setBuyDate(buyDate);
            myItem.get().setDeliveredToPoland(deliveredToPoland);
            myItem.get().setLastActionDate(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            if (sellPrice > 0) {
                myItem.get().setSellPrice(sellPrice);
                myItem.get().setSellDate(sellDate);
            }
            if (cashOnDelivery > 0) {
                myItem.get().setCashOnDelivery(cashOnDelivery);
                myItem.get().setIfCashOnDelivery(1);
                myItem.get().setDeliveredToPoland(2);
            }
            if (note.length() > 0) myItem.get().setNotes(myItem.get().getNotes() + "; " + note);
            myItemRepository.save(myItem.get());
        }
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
    }

    @RequestMapping(value = {"/edit"}, params = "delete", method = RequestMethod.POST)
    public ModelAndView editItemDelete(ModelAndView modelAndView,
                                       @RequestParam(name = "id", required = true) int itemId) {

        Optional<MyItem> myItem = myItemRepository.findById(itemId);
        if (myItem.isPresent()) {
            myItemRepository.delete(myItem.get());
        }
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
    }

    @RequestMapping(value = {"/", "/index", "/sell"}, method = RequestMethod.GET)
    public ModelAndView home(@RequestParam(value = "searchItem", required = false, defaultValue = "") String name, ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if (user == null) modelAndView.setViewName("login");
        else {
            List<MyItem> myItems = myItemRepository.findItemsOnStockGroupByItemId(1);
            myItems = (getItemsNames(myItems, this.getItemsMap()));
            if (name != null && !name.equals("")) {
                myItems = (getByName(myItems, name));
            }
            this.getQuantityOfItems(myItems);
            modelAndView.addObject("basketsize", "Basket (" + basket.getMyItemList().size() + ")");
            modelAndView.addObject("myItems", myItems);
            modelAndView.addObject("test", myItems.get(0).getName());
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }

    @RequestMapping(value = {"/delivery"}, method = RequestMethod.GET)
    public ModelAndView delivery(@RequestParam(value = "searchItem", required = false, defaultValue = "") String name) {
        ModelAndView modelAndView = new ModelAndView();
        List<MyItem> myItems = myItemRepository.findItemsInTransport();
        myItems = getItemsNames(myItems, this.getItemsMap());
        if (name != null && !name.equals("")) {
            myItems = getByName(myItems, name);
        }
        for (MyItem myItem : myItems) {
            myItem.setQuantity(myItemRepository.countItemIdBySellPriceIsNullAndDeliveredToPolandIsNullAndItemId(myItem.getItemId()));
        }
        modelAndView.addObject("myItems", myItems);
        modelAndView.addObject("basketsize", "Basket (" + basket.getMyItemList().size() + ")");
        modelAndView.setViewName("delivery");
        return modelAndView;
    }

//    @RequestMapping(value = "/delivery", params = "confirmDelivery", method = RequestMethod.POST)
//    public ModelAndView setQuantityDelivered(@RequestParam(value = "checkedItemId", required = true, defaultValue = "") int itemId) {
//        ModelAndView modelAndView = new ModelAndView();
//        List<MyItem> myItems = myItemRepository.findItemInTransportByItemId(itemId);
//        myItems.stream().forEach(x-> System.out.println(x.getLastActionDate()));
//        modelAndView.setViewName("redirect:/delivery");
//        return modelAndView;
//    }

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

    public static List<MyItem> getByName(List<MyItem> itemList, String name) {
        if (name != null) {
            return itemList.stream().filter(x -> x.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
        } else {
            return itemList;
        }
    }

    public void getQuantityOfItems(List<MyItem> myItems) {
        for (MyItem myItem : myItems) {
            myItem.setQuantity(myItemRepository.countItemIdBySellPriceIsNullAndDeliveredToPolandIsAndItemId(1, myItem.getItemId()));
            myItem.setQuantInTransport(myItemRepository.countItemIdBySellPriceIsNullAndDeliveredToPolandIsNullAndItemId(myItem.getItemId()));
        }
    }


}
