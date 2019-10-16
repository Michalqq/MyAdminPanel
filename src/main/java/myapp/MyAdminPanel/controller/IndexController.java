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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    private UserService userService;
    private MyItemRepository myItemRepository;
    private ItemRepository itemRepository;
    private Basket basket;
    private DBAction dbAction;
    private ItemsNameFiller itemsNameFiller;
    private ChartDataGenerator chartDataGenerator;

    @Autowired
    public IndexController(UserService userService, MyItemRepository myItemRepository, ItemRepository itemRepository,
                           Basket basket, DBAction dbAction, ItemsNameFiller itemsNameFiller, ChartDataGenerator chartDataGenerator) {
        this.userService = userService;
        this.myItemRepository = myItemRepository;
        this.itemRepository = itemRepository;
        this.basket = basket;
        this.dbAction = dbAction;
        this.itemsNameFiller = itemsNameFiller;
        this.chartDataGenerator = chartDataGenerator;
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
        if (itemId == 0) {
            modelAndView.setViewName("redirect:/index");
            return modelAndView;
        }
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

    @RequestMapping(value = {"/edit"}, params = "delete" , method = RequestMethod.POST)
    public ModelAndView editItemDelete(ModelAndView modelAndView,
                                       @RequestParam(name = "id", required = true) int itemId) {
        return this.edit(modelAndView, 0, itemId);
    }

    @RequestMapping(value = {"/edit"}, params = "restore", method = RequestMethod.POST)
    public ModelAndView restoreToShop(ModelAndView modelAndView,
                               @RequestParam(name = "id", required = true) int itemId) {
        return this.edit(modelAndView, 1, itemId);
    }

    public ModelAndView edit(ModelAndView modelAndView, int param, int itemId){
        Optional<MyItem> myItem = myItemRepository.findById(itemId);
        if (myItem.isPresent()) {
            switch(param) {
                case 0:
                    myItemRepository.delete(myItem.get());
                    break;
                case 1:
                    dbAction.clearSellPriceAndDate(myItem.get());
                    dbAction.setNote(myItem.get(), "towar po zwrocie");
                    break;
            }
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
            myItems.addAll(getListWithAllItemEmptyData());
            myItems = this.getMyItemsListWithName(myItems, name);
            this.getQuantityOfItems(myItems);
            basket.addInfoAboutBasketSize(modelAndView);
            modelAndView.addObject("myItems", myItems);
            modelAndView.addObject("currencies", getCurrencyList());
            chartDataGenerator.chartDataCreator(modelAndView, 6, 30);
            this.addInfoToFront(modelAndView);
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }

    @RequestMapping(value = {"/delivery"}, method = RequestMethod.GET)
    public ModelAndView delivery(ModelAndView modelAndView, @RequestParam(value = "searchItem", required = false, defaultValue = "") String name) {
        List<MyItem> myItems = myItemRepository.findItemsInTransport();
        myItems = this.getMyItemsListWithName(myItems, name);
        for (MyItem myItem : myItems) {
            myItem.setQuantity(myItemRepository.countItemIdBySellPriceIsNullAndDeliveredToPolandIsNullAndItemId(myItem.getItemId()));
        }
        modelAndView.addObject("myItems", myItems);
        basket.addInfoAboutBasketSize(modelAndView);
        modelAndView.setViewName("delivery");
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

    public List<MyItem> getListWithAllItemEmptyData() {
        List<MyItem> myItems = new ArrayList<>();
        List<Item> items = itemRepository.findAll();
        for (Item item : items) {
            MyItem myItem = new MyItem();
            myItem.setId(0);
            myItem.setItemId(item.getId());
            myItem.setName(item.getName());
            myItem.setQuantity(0);
            myItems.add(myItem);
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

    public void addInfoToFront(ModelAndView modelAndView) {
        modelAndView.addObject("totalItemsOnStock", myItemRepository.countBySellPriceIsNull());
        double valueOnStock = myItemRepository.getSumBuyPriceWhereSellPriceIsNull();
        double monthlyExpenses = myItemRepository.sumBuyPriceWhereBuyDateIsBetween(DateGenerator.getFirstDayOfMonth(DateTimeFormatter.ofPattern("MM").format(LocalDate.now()))
                , DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDate.now()));
        DecimalFormat df1 = new DecimalFormat("###,###,###.##");
        modelAndView.addObject("totalValueOnStock", df1.format(valueOnStock) + " PLN");
        modelAndView.addObject("totalMonthlyExpenses", df1.format(monthlyExpenses) + " PLN");
        modelAndView.addObject("cashOnDelivery", df1.format(myItemRepository.getSumCashOnDeliveryWhereStatusIsInDelivery()) + " PLN");
        modelAndView.addObject("countItemsWithCashOnDelivery", myItemRepository.countByDeliveredToPolandIs(2));
        modelAndView.addObject("countItemsInDeliveryToPL", myItemRepository.countByDeliveredToPolandIsNull());
    }

    public List<Currency> getCurrencyList() {
        List<Currency> currencies = new ArrayList<>();
        currencies.add(getCurrency("USD"));
        currencies.add(getCurrency("EUR"));
        return currencies;
    }

    public Currency getCurrency(String code) {
        String apiPath = "http://api.nbp.pl/api/exchangerates/rates/A/" + code + "/?format=json";
        return new RestTemplate().getForObject(apiPath, Currency.class);
    }

    public List<MyItem> getMyItemsListWithName(List<MyItem> myItems, String name){
        myItems = countItemOnStock(myItems);
        myItems = (itemsNameFiller.getItemsNames(myItems));
        if (name != null && !name.equals("")) {
            myItems = (getByNameContains(myItems, name));
        }
        return myItems;
    }

}
