package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.*;
import myapp.MyAdminPanel.model.Currency;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.validation.Valid;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    private UserService userService;
    private MyItemRepository myItemRepository;
    private ItemRepository itemRepository;
    private Basket basket;
    private DBAction dbAction;
    private CountProfitByMonth countProfitByMonth;
    private CountItemSold countItemSold;
    private ItemsNameFiller itemsNameFiller;

    @Autowired
    public LoginController(UserService userService, MyItemRepository myItemRepository, ItemRepository itemRepository,
                           Basket basket, DBAction dbAction, CountProfitByMonth countProfitByMonth, CountItemSold countItemSold,
                           ItemsNameFiller itemsNameFiller) {
        this.userService = userService;
        this.myItemRepository = myItemRepository;
        this.itemRepository = itemRepository;
        this.basket = basket;
        this.dbAction = dbAction;
        this.countProfitByMonth = countProfitByMonth;
        this.countItemSold = countItemSold;
        this.itemsNameFiller = itemsNameFiller;
    }

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

    @RequestMapping(value = {"/details"}, params = "details", method = RequestMethod.POST)
    public ModelAndView getListOfItem(@RequestParam(value = "id", required = true) int id,
                                      ModelAndView modelAndView) {
        List<MyItem> myItems = myItemRepository.findItemInStockByItemId(id);
        itemsNameFiller.getItemsNames(myItems);
        for (MyItem item : myItems) {
            item.setQuantity(1);
        }
        basket.addInfoAboutBasketSize(modelAndView);
        modelAndView.addObject("myItems", myItems);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = {"/edit"}, params = "edit", method = RequestMethod.POST)
    public ModelAndView editItem(ModelAndView modelAndView,
                                 @RequestParam(name = "id", required = true) int itemId) {
        Optional<MyItem> myItem = myItemRepository.findById(itemId);
        if (myItem.isPresent()) {
            myItem.get().setName(itemsNameFiller.getItemsMap().get(myItem.get().getItemId()));
            modelAndView.addObject("myItem", myItem.get());
        }
        basket.addInfoAboutBasketSize(modelAndView);
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
        basket.addInfoAboutBasketSize(modelAndView);
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
        basket.addInfoAboutBasketSize(modelAndView);
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
    }

    @RequestMapping(value = {"/edit"}, params = "restore", method = RequestMethod.POST)
    public ModelAndView restoreToShop(ModelAndView modelAndView,
                                      @RequestParam(name = "id", required = true) int itemId) {
        Optional<MyItem> myItem = myItemRepository.findById(itemId);
        if (myItem.isPresent()) {
            dbAction.clearSellPriceAndDate(myItem.get());
            dbAction.setNote(myItem.get(), "towar po zwrocie");
        }
        basket.addInfoAboutBasketSize(modelAndView);
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
            myItems = countItemOnStock(myItems);
            myItems = (itemsNameFiller.getItemsNames(myItems));
            if (name != null && !name.equals("")) {
                myItems = (getByNameContains(myItems, name));
            }
            this.getQuantityOfItems(myItems);
            basket.addInfoAboutBasketSize(modelAndView);
            modelAndView.addObject("myItems", myItems);
            modelAndView.addObject("currencies", getCurrencyList());
            this.chartDataCreator(modelAndView);
            this.addInfoToFront(modelAndView);
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }

    @RequestMapping(value = {"/delivery"}, method = RequestMethod.GET)
    public ModelAndView delivery(ModelAndView modelAndView, @RequestParam(value = "searchItem", required = false, defaultValue = "") String name) {
        List<MyItem> myItems = myItemRepository.findItemsInTransport();
        myItems = countItemOnStock(myItems);
        myItems = itemsNameFiller.getItemsNames(myItems);
        if (name != null && !name.equals("")) {
            myItems = getByNameContains(myItems, name);
        }
        for (MyItem myItem : myItems) {
            myItem.setQuantity(myItemRepository.countItemIdBySellPriceIsNullAndDeliveredToPolandIsNullAndItemId(myItem.getItemId()));
        }
        modelAndView.addObject("myItems", myItems);
        basket.addInfoAboutBasketSize(modelAndView);
        modelAndView.setViewName("delivery");
        return modelAndView;
    }

    public ModelAndView chartDataCreator(ModelAndView modelAndView) {
        modelAndView.addObject("dataToChart", getProfitLast6Month());
        modelAndView.addObject("monthNameToChart", getNameOfLastMonth(6));
        modelAndView.addObject("dataToItemSold", getSoldItemByLastMonth(6));
        modelAndView.addObject("totalProfit", getProfitLast6Month().stream().mapToInt(Integer::intValue).sum());
        modelAndView.addObject("totalItemSold", myItemRepository.countBySellPriceIsNotNull());
        this.getSoldSumByLastDays(modelAndView, 30);
        return modelAndView;
    }

    public ModelAndView getSoldSumByLastDays(ModelAndView modelAndView, int quantityOfDay) {
        List<String> dataByDay = DateGenerator.getLastDate(30);
        List<Double> soldByDayList = countItemSold.getLastSoldSumData(30, dataByDay);
        Collections.reverse(soldByDayList);
        Collections.reverse(dataByDay);
        modelAndView.addObject("dataToEarningByDays", soldByDayList);
        modelAndView.addObject("labelToEarningByDays", dataByDay);
        modelAndView.addObject("totalEarningLastDays", soldByDayList.stream().mapToDouble(Double::intValue).sum());
        return modelAndView;
    }

    public List<MyItem> countItemOnStock(List<MyItem> myItems1) {
        List<MyItem> myItems = new ArrayList<>();
        int temp = 0;
        for (MyItem item : myItems1) {
            temp = 0;
            for (int i = 0; i < myItems.size(); i++) {
                if (myItems.get(i).getItemId() == item.getItemId()) temp = 1;
            }
            if (temp == 0) myItems.add(item);
        }
        return myItems;
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

    public List<Integer> getSoldItemByLastMonth(int quantityOfMonth) {
        List<Integer> soldItem = new ArrayList<>();
        if (quantityOfMonth < 0) {
            soldItem.add(0);
            return soldItem;
        }
        for (int i = 0; i < quantityOfMonth; i++) {
            soldItem.add(countItemSold.countItemSoldByMonth(DateTimeFormatter.ofPattern("MM").format(LocalDate.now().getMonth().minus(i))));
        }
        Collections.reverse(soldItem);
        return soldItem;
    }

    public List<Integer> getProfitLast6Month() {
        List<Integer> profitList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            profitList.add(countProfitByMonth.getProfit(DateTimeFormatter.ofPattern("MM").format(LocalDate.now().getMonth().minus(i))));
        }
        Collections.reverse(profitList);
        return profitList;
    }

    public List<String> getNameOfLastMonth(int quantityOfMonth) {
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < quantityOfMonth; i++) {
            nameList.add(DateGenerator.getMonthName(DateTimeFormatter.ofPattern("MM").format(LocalDate.now().getMonth().minus(i))));
        }
        Collections.reverse(nameList);
        return nameList;
    }

    public void addInfoToFront(ModelAndView modelAndView) {
        modelAndView.addObject("totalItemsOnStock", "Liczba przedmiotów w magazynie: " + myItemRepository.countBySellPriceIsNull());
        double valueOnStock =myItemRepository.getSumBuyPriceWhereSellPriceIsNull();
        double monthlyExpenses = myItemRepository.sumBuyPriceWhereBuyDateIsBetween(DateGenerator.getFirstDayOfMonth(DateTimeFormatter.ofPattern("MM").format(LocalDate.now()))
                ,DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDate.now()));
        DecimalFormat df1=new DecimalFormat("###,###,###.##");
        modelAndView.addObject("totalValueOnStock", "Wartość magazynu: " + df1.format(valueOnStock) + " PLN");
        modelAndView.addObject("totalMonthlyExpenses", "Wydatki w tym miesiącu: " + df1.format(monthlyExpenses) + " PLN");
    }

    public List<Currency> getCurrencyList(){
        List<Currency> currencies  = new ArrayList<>();
        currencies.add(getCurrency("USD"));
        currencies.add(getCurrency("EUR"));
        return currencies;
    }

    public Currency getCurrency(String code){
        String apiPath = "http://api.nbp.pl/api/exchangerates/rates/A/" + code + "/?format=json";
        return new RestTemplate().getForObject(apiPath,Currency.class);
    }

}
