package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Basket;
import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.model.User;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.service.CountProfitByMonth;
import myapp.MyAdminPanel.service.DBAction;
import myapp.MyAdminPanel.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
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

    @Autowired
    private DBAction dbAction;

    @Autowired
    private CountProfitByMonth countProfitByMonth;

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value = "/chart")
    public ModelAndView chart(ModelAndView modelAndView) {
        modelAndView.setViewName("chart");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration(ModelAndView modelAndView) {
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult, ModelAndView modelAndView) {
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
            dbAction.setBuyPrice(myItem.get(), buyPrice, buyDate);
            dbAction.setDeliveredToPolStatus(myItem.get(), deliveredToPoland);
            dbAction.setSellPrice(myItem.get(), sellPrice);
            dbAction.setCashOnDelivery(myItem.get(), cashOnDelivery);
            if (note.length() > 0) myItem.get().setNotes(myItem.get().getNotes() + "; " + note);
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
                myItems = (getByNameContains(myItems, name));
            }
            this.getQuantityOfItems(myItems);
            modelAndView.addObject("basketsize", "Basket (" + basket.getMyItemList().size() + ")");
            modelAndView.addObject("myItems", myItems);
            modelAndView.addObject("dataToChart", getProfitLast6Month());
            modelAndView.addObject("monthNameToChart", getNameOfLast6Month());
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }

    @RequestMapping(value = {"/delivery"}, method = RequestMethod.GET)
    public ModelAndView delivery(ModelAndView modelAndView, @RequestParam(value = "searchItem", required = false, defaultValue = "") String name) {
        List<MyItem> myItems = myItemRepository.findItemsInTransport();
        myItems = getItemsNames(myItems, this.getItemsMap());
        if (name != null && !name.equals("")) {
            myItems = getByNameContains(myItems, name);
        }
        for (MyItem myItem : myItems) {
            myItem.setQuantity(myItemRepository.countItemIdBySellPriceIsNullAndDeliveredToPolandIsNullAndItemId(myItem.getItemId()));
        }
        modelAndView.addObject("myItems", myItems);
        modelAndView.addObject("basketsize", "Basket (" + basket.getMyItemList().size() + ")");
        modelAndView.setViewName("delivery");
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

    public static List<MyItem> getByNameContains(List<MyItem> itemList, String name) {
        if (name != null) {
            return itemList.stream().filter(x -> x.getName() != null).filter(x -> x.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
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

    public List<Integer> getProfitLast6Month() {
        List<Integer> profitList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            profitList.add(countProfitByMonth.getProfit(DateTimeFormatter.ofPattern("MM").format(LocalDate.now().getMonth().minus(i))));
        }
        Collections.reverse(profitList);
        return profitList;
    }
    public List<String> getNameOfLast6Month() {
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            nameList.add(countProfitByMonth.getMonthName(DateTimeFormatter.ofPattern("MM").format(LocalDate.now().getMonth().minus(i))));
        }
        Collections.reverse(nameList);
        return nameList;
    }

}
