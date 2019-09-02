package myapp.MyAdminPanel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyItemSoldSum {
    private Double sellPrice;
    private String sellDate;
}
