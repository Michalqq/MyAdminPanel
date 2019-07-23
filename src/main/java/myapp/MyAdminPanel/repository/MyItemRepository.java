package myapp.MyAdminPanel.repository;

import myapp.MyAdminPanel.model.MyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MyItemRepository extends JpaRepository<MyItem, Integer> {
    List<MyItem> findBySellPriceIsNull();

    List<MyItem> findBySellPriceIsNullAndDeliveredToPolandIs(int deliveryStatus);
    List<MyItem> findBySellPriceIsNullAndDeliveredToPolandIsNull();
}
