package myapp.MyAdminPanel.service;

import myapp.MyAdminPanel.model.MyItem;

public interface DBAction {
    boolean setCashOnDelivery(MyItem myItem, double cashOnDelivery);

    boolean setSellPrice(MyItem myItem, double sellPrice, String sellDate);

    boolean setLastActionDateNowDate(MyItem myItem);

    boolean setBuyPrice(MyItem myItem, double buyPrice, String buyDate);

    boolean setDeliveredToPolStatus(MyItem myItem, int deliveredStatus);

    boolean setNote(MyItem myItem, String note);
}
