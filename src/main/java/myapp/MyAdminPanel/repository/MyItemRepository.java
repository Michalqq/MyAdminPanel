package myapp.MyAdminPanel.repository;

import myapp.MyAdminPanel.model.MyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MyItemRepository extends JpaRepository<MyItem, Integer> {
    List<MyItem> findBySellPriceIsNull();

    List<MyItem> findBySellPriceIsNullAndDeliveredToPolandIs(int deliveryStatus);

    @Query(value = "FROM MyItem "+
            "WHERE deliveredToPoland is null AND itemId = :itemId " +
            "ORDER BY lastActionDate ASC")
    List<MyItem> findItemInTransportByItemId(@Param("itemId") int itemId);

    List<MyItem> findAllByLastActionDateBetweenAndDeliveredToPolandEquals(String startDate, String stopDate, int deliveryStatus);

    List<MyItem> findAllByLastActionDateBetweenAndDeliveredToPolandIsNull(String startDate, String stopDate);

    List<MyItem> findAllByLastActionDateBetween(String startDate, String stopDate);

//   @Query(value = "FROM MyItem JOIN Item WHERE sellPrice is null and MyItem.itemId = Item.id AND deliveredToPoland = :deliveredToPoland " +
//            "GROUP BY itemId")
//    List<MyItem> findBySellPriceInNullWithItemNames(@Param("deliveredToPoland") int deliveredToPoland);

    @Query(value = "FROM MyItem "+
            "WHERE sellPrice is null AND deliveredToPoland = :deliveredToPoland " +
            "GROUP BY itemId")
    List<MyItem>  findItemsOnStockGroupByItemId(@Param("deliveredToPoland") int deliveredToPoland);

    @Query(value = "FROM MyItem "+
            "WHERE sellPrice is null AND deliveredToPoland is null " +
            "GROUP BY itemId")
    List<MyItem>  findItemsInTransport();

    int countItemIdBySellPriceIsNullAndDeliveredToPolandIsAndItemId(@Param("deliveryToPoland") int deliveryToPoland, @Param("itemId") int itemId);
    int countItemIdBySellPriceIsNullAndDeliveredToPolandIsNullAndItemId(@Param("itemId") int itemId);

    @Query(value = "SELECT MAX(id) FROM MyItem")
    int getMaxId();

    Optional<MyItem> findById(Integer ID);

    MyItem save(MyItem myItem);

}