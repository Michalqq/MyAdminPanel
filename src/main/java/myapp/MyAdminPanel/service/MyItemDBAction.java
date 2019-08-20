package myapp.MyAdminPanel.service;

import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MyItemDBAction implements DBAction {
    @Autowired
    private MyItemRepository myItemRepository;

    @Override
    public boolean setCashOnDelivery(MyItem myItem, double cashOnDelivery) {
        if (cashOnDelivery > 0) {
            myItem.setCashOnDelivery(cashOnDelivery);
            myItem.setIfCashOnDelivery(1);
            myItem.setDeliveredToPoland(2);
            setLastActionDateNowDate(myItem);
            myItemRepository.save(myItem);
            return true;
        } else return false;
    }

    @Override
    public boolean setSellPrice(MyItem myItem, double sellPrice) {
        if (sellPrice > 0) {
            myItem.setSellPrice(sellPrice);
            myItem.setSellDate(DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDate.now()));
            setLastActionDateNowDate(myItem);
            myItemRepository.save(myItem);
            return true;
        } else return false;
    }

    @Override
    public boolean setLastActionDateNowDate(MyItem myItem) {
        myItem.setLastActionDate(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        myItemRepository.save(myItem);
        return true;
    }

    @Override
    public boolean setBuyPrice(MyItem myItem, double buyPrice, String buyDate) {
        if (buyPrice > 0) {
            myItem.setBuyPrice(buyPrice);
            myItem.setBuyDate(buyDate);
            setLastActionDateNowDate(myItem);
            myItemRepository.save(myItem);
            return true;
        } else return false;
    }

    @Override
    public boolean setDeliveredToPolStatus(MyItem myItem, int deliveredStatus) {
        myItem.setDeliveredToPoland(deliveredStatus);
        setLastActionDateNowDate(myItem);
        myItemRepository.save(myItem);
        return true;
    }

    @Override
    public boolean setNote(MyItem myItem, String note) {
        return false;
    }

    @Override
    public boolean createNewItem() {
        MyItem myItem = new MyItem();
        myItem.setId((myItemRepository.getMaxId() + 1));
        myItem.setLastActionDate(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        myItemRepository.save(myItem);
        return true;
    }

    @Override
    public boolean createNewItem(double price, int itemId, int sellerId) {
        MyItem myItem = new MyItem();
        myItem.setId((myItemRepository.getMaxId() + 1));
        myItem.setBuyPrice(price);
        myItem.setItemId(itemId);
        myItem.setSellerId(sellerId);
        myItem.setBuyDate(DateTimeFormatter.ofPattern("yyy-MM-dd").format(LocalDate.now()));
        myItem.setLastActionDate(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        myItemRepository.save(myItem);
        return true;
    }

    @Override
    public boolean clearSellPriceAndDate(MyItem myItem) {
        myItem.setDeliveredToPoland(1);
        myItem.setSellDate(null);
        myItem.setSellPrice(null);
        myItem.setIfCashOnDelivery(null);
        myItem.setCashOnDelivery(null);
        myItemRepository.save(myItem);
        return true;
    }
}
