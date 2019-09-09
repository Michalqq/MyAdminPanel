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
    private int quantity;

    @Override
    public int compareTo(ItemToReport o) {
        return getSellPrice().compareTo(o.getSellPrice());
    }
}
