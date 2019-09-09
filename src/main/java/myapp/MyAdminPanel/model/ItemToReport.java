package myapp.MyAdminPanel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ItemToReport implements Comparable<ItemToReport> {
    private Double sellPrice = 0.0;
    private String sellDate;
    private String name;
    private int itemId;
    private int quantity = 0;
    private double average;

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
        setAverageAutomatickly();
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
        setAverageAutomatickly();
    }
    @Override
    public int compareTo(ItemToReport o) {
        return getSellPrice().compareTo(o.getSellPrice());
    }

    public void setAverageAutomatickly(){
        if (this.quantity > 0 && this.sellPrice>0){
            this.average = Math.round(this.sellPrice / this.quantity);
        }
    }
}
