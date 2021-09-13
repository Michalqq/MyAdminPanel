package myapp.MyAdminPanel.repositoryTest;

import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MyItemRepositoryTest { //todo

    @Autowired
    private MyItemRepository myItemRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void repositoryTest() {
        createTestDB();
        List<MyItem> myItems = myItemRepository.findAll();
        assertThat(myItems.get(0).getId()).isEqualTo(myItemRepository.getMaxId());
        assertThat(myItems.get(0).getBuyPrice()).isEqualTo(10);
        assertThat(myItems.size()).isEqualTo(1);
        double sumSellPrice = myItemRepository.getSellPriceSumWhereSellDateBetween("2019-09-01", "2019-09-30");
        assertEquals(20, sumSellPrice);
    }

    public void createTestDB() {
        MyItem myitem = new MyItem();
        myitem.setItemId(1);
        myitem.setBuyPrice(10.0);
        myitem.setBuyDate("2019-09-10");
        myitem.setSellDate("2019-09-16");
        myitem.setSellPrice(20.0);
        entityManager.persist(myitem);
        entityManager.flush();
    }
}
