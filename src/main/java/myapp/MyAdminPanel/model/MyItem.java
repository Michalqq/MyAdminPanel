package myapp.MyAdminPanel.model;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "wskazniki")
public class MyItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    @Transient
    private String name;

    @Transient
    private int quantity;

    @Transient
    private int quantInTransport;

    @Column(name = "Item_ID")
    private int itemId;

    @Column(name = "Buy_price")
    private Double buyPrice;

    @Column(name = "Sell_price")
    private Double sellPrice;

    @Column(name = "Buy_date")
    private String buyDate;

    @Column(name = "Sell_date")
    private String sellDate;

    @Column(name = "Cash_on_delivery")
    private Double cashOnDelivery;

    @Column(name = "If_cash_on_delivery")
    private Integer ifCashOnDelivery;

    @Column(name = "delivered_to_Poland")
    private Integer deliveredToPoland;

    @Column(name = "Last_action_date")
    private LocalDateTime lastActionDate;

    @Column(name = "Notes")
    private String notes;

    public MyItem() {
    }

    public MyItem(Integer itemId) {
        this.itemId = itemId;
    }

    public MyItem(int quantity, Double buyPrice, String buyDate, Integer itemId) {
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.buyDate = buyDate;
        this.itemId = itemId;
    }

    public MyItem(Double buyPrice, Double sellPrice, String buyDate, String sellDate, Double cashOnDelivery, Integer ifCashOnDelivery, Integer deliveredToPoland, LocalDateTime lastActionDate, String notes) {
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.buyDate = buyDate;
        this.sellDate = sellDate;
        this.cashOnDelivery = cashOnDelivery;
        this.ifCashOnDelivery = ifCashOnDelivery;
        this.deliveredToPoland = deliveredToPoland;
        this.lastActionDate = lastActionDate;
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public void addQuantity() {
        this.quantity++;
    }
    public int getQuantInTransport() {
        return quantInTransport;
    }

    public void setQuantInTransport(int quantInTransport) {
        this.quantInTransport = quantInTransport;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }

    public Double getCashOnDelivery() {
        return cashOnDelivery;
    }

    public void setCashOnDelivery(Double cashOnDelivery) {
        this.cashOnDelivery = cashOnDelivery;
    }

    public Integer getIfCashOnDelivery() {
        return ifCashOnDelivery;
    }

    public void setIfCashOnDelivery(Integer ifCashOnDelivery) {
        this.ifCashOnDelivery = ifCashOnDelivery;
    }

    public Integer getDeliveredToPoland() {
        return deliveredToPoland;
    }

    public void setDeliveredToPoland(Integer deliveredToPoland) {
        this.deliveredToPoland = deliveredToPoland;
    }

    public LocalDateTime getLastActionDate() {
        return lastActionDate;
    }

    public void setLastActionDate(LocalDateTime lastActionDate) {
        this.lastActionDate = lastActionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {

        this.notes = notes;
    }
}
