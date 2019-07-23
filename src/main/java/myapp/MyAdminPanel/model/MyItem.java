package myapp.MyAdminPanel.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "wskazniki")
public class MyItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    @Transient
    private String name;

    @Column(name = "Item_ID")
    private Integer itemId;

    @Column(name = "Buy_price")
    private Double buyPrice;

    @Column(name = "Sell_price")
    private Double sellPrice;

    @Column(name = "Buy_date")
    private Date buyDate;

    @Column(name = "Sell_date")
    private Date sellDate;

    @Column(name = "Cash_on_delivery")
    private Integer cashOnDelivery;

    @Column(name = "If_cash_on_delivery")
    private Integer ifCashOnDelivery;

    @Column(name = "delivered_to_Poland")
    private Integer deliveredToPoland;

    @Column(name = "Last_action_date")
    private Date lastActionDate;

    @Column(name = "Notes")
    private String notes;

    public MyItem(){
    }

    public MyItem(Double buyPrice, Double sellPrice, Date buyDate, Date sellDate, Integer cashOnDelivery, Integer ifCashOnDelivery, Integer deliveredToPoland, Date lastActionDate, String notes) {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
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

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

    public Integer getCashOnDelivery() {
        return cashOnDelivery;
    }

    public void setCashOnDelivery(Integer cashOnDelivery) {
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

    public Date getLastActionDate() {
        return lastActionDate;
    }

    public void setLastActionDate(Date lastActionDate) {
        this.lastActionDate = lastActionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        notes = notes;
    }
}
