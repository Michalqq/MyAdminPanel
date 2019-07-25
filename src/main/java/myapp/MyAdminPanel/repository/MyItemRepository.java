package myapp.MyAdminPanel.repository;

import myapp.MyAdminPanel.model.MyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MyItemRepository extends JpaRepository<MyItem, Integer> {
    List<MyItem> findBySellPriceIsNull();

    List<MyItem> findBySellPriceIsNullAndDeliveredToPolandIs(int deliveryStatus);

    @Query(value = "FROM MyItem "+
            "WHERE sellPrice is null AND deliveredToPoland = :deliveredToPoland " +
            "GROUP BY itemId")
    List<MyItem>  findItemsOnStock(@Param("deliveredToPoland") int deliveredToPoland);

    @Query(value = "FROM MyItem "+
            "WHERE sellPrice is null AND deliveredToPoland is null " +
            "GROUP BY itemId")
    List<MyItem>  findItemsInTransport();

    int countItemIdBySellPriceIsNullAndDeliveredToPolandIsAndItemId(@Param("deliveryToPoland") int deliveryToPoland, @Param("itemId") int itemId);
    int countItemIdBySellPriceIsNullAndDeliveredToPolandIsNullAndItemId(@Param("itemId") int itemId);

    Optional<MyItem> findById(Integer ID);

    MyItem save(MyItem myItem);

}