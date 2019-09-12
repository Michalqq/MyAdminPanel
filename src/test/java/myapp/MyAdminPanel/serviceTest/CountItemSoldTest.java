package myapp.MyAdminPanel.serviceTest;

import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.service.CountItemSold;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CountItemSoldTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MyItemRepository myItemRepository;

    @Test
    public void countItemSoldByMonthTest() {
        myItemRepositoryFiller();
        CountItemSold countItemSold = new CountItemSold();
        countItemSold.setMyItemRepository(myItemRepository);
        assertEquals(countItemSold.countItemSoldByMonth("01"), 4);
        assertEquals(countItemSold.countItemSoldByMonth("02"), 0);
        assertEquals(countItemSold.countItemSoldByMonth("08"), 2);
        assertEquals(countItemSold.countItemSoldByMonth("09"), 3);
    }

    public void myItemRepositoryFiller() {
        String actualYear = String.valueOf(LocalDate.now().getYear());
        List<String> sellData = Arrays.asList("09-12", "09-02", "09-10",
                "08-01", "08-31",
                "01-01", "01-02", "01-10", "01-30",
                "05-05");
        for (int i = 0; i < 10; i++) {
            MyItem myItem = new MyItem();
            myItem.setId(i + 1);
            myItem.setSellDate(actualYear + "-" + sellData.get(i));
            myItem.setSellPrice(10.0);
            entityManager.persist(myItem);
            entityManager.flush();
            entityManager.clear();
        }
    }

}
