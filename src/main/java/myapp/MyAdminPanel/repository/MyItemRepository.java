package myapp.MyAdminPanel.repository;

import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.model.MyItemSoldSum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MyItemRepository extends JpaRepository<MyItem, Integer> {
    List<MyItem> findBySellPriceIsNull();

    List<MyItem> findBySellPriceIsNullAndDeliveredToPolandIs(int deliveryStatus);

    int countBySellPriceIsNotNull();

    int countBySellPriceIsNotNullAndSellDateIsBetween(String startDate, String stopDate);

    @Query(value = "SELECT new myapp.MyAdminPanel.model.MyItemSoldSum(SUM(sellPrice), sellDate) FROM MyItem WHERE sellPrice IS NOT NULL GROUP BY sellDate ORDER BY sellDate DESC")
    List<MyItemSoldSum> sumSellPriceByDays();

    @Query(value = "SELECT new myapp.MyAdminPanel.model.MyItemSoldSum(COALESCE(SUM(sellPrice),0), sellDate) FROM MyItem WHERE sellPrice IS NOT NULL AND sellDate = :sellDate")
    MyItemSoldSum sumSellPriceWhereSellDateIs(String sellDate);

    @Query(value = "FROM MyItem " +
            "WHERE deliveredToPoland is null AND itemId = :itemId " +
            "ORDER BY lastActionDate ASC")
    List<MyItem> findItemInTransportByItemId(@Param("itemId") int itemId);

    @Query(value = "FROM MyItem " +
            "WHERE lastActionDate BETWEEN :startDate AND :stopDate and deliveredToPoland = :deliveryStatus" +
            " ORDER BY lastActionDate DESC")
    List<MyItem> findAllByLastActionDateBetweenAndDeliveredToPolandEquals(String startDate, String stopDate, int deliveryStatus);

    @Query(value = "FROM MyItem " +
            "WHERE lastActionDate BETWEEN :startDate  AND :stopDate AND deliveredToPoland is null" +
            " ORDER BY lastActionDate DESC")
    List<MyItem> findAllByLastActionDateBetweenAndDeliveredToPolandIsNull(String startDate, String stopDate);

    @Query(value = "FROM MyItem " +
            "WHERE lastActionDate BETWEEN :startDate  AND :stopDate" +
            " ORDER BY lastActionDate DESC")
    List<MyItem> findAllByLastActionDateBetween(String startDate, String stopDate);

//    @Query(value = "FROM MyItem " +
//            "WHERE sellPrice is null AND deliveredToPoland = :deliveredToPoland " +
//            "GROUP BY itemId")
//    List<MyItem> findItemsOnStockGroupByItemId(@Param("deliveredToPoland") int deliveredToPoland);

    @Query(value = "FROM MyItem " +
            "WHERE sellPrice is null and deliveredToPoland = 1")
    List<MyItem> findItemsOnStockGroupByItemId();

    @Query(value = "FROM MyItem " +
            "WHERE sellPrice is null AND deliveredToPoland is null " +
            "GROUP BY itemId")
    List<MyItem> findItemsInTransport();

    int countItemIdBySellPriceIsNullAndDeliveredToPolandIsAndItemId(@Param("deliveryToPoland") int deliveryToPoland, @Param("itemId") int itemId);

    int countItemIdBySellPriceIsNullAndDeliveredToPolandIsNullAndItemId(@Param("itemId") int itemId);

    @Query(value = "SELECT MAX(id) FROM MyItem")
    int getMaxId();

    @Query(value = "SELECT COALESCE(SUM(sellPrice), 0) FROM MyItem WHERE sellDate BETWEEN :startDate AND :stopDate")
    int getSellPriceSumWhereSellDateBetween(String startDate, String stopDate);

    @Query(value = "SELECT COALESCE(SUM(buyPrice), 0) FROM MyItem WHERE sellDate BETWEEN :startDate AND :stopDate")
    int getBuyPriceSumWhereSellDateBetween(String startDate, String stopDate);

    Optional<MyItem> findById(Integer ID);

    MyItem save(MyItem myItem);

}