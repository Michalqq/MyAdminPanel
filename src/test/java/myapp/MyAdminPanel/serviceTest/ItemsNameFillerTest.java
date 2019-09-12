package myapp.MyAdminPanel.serviceTest;

import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.ItemRepository;
import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.service.ItemsNameFiller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemsNameFillerTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MyItemRepository myItemRepository;

    @Test
    public void setItemMapTest() {
        ItemsNameFiller itemsNameFiller = new ItemsNameFiller();
        for (Item item:itemsList()){
            entityManager.persist(item);
            entityManager.flush();
            entityManager.clear();
        }
        for (MyItem myItem : myItemList()){
            entityManager.persist(myItem);
            entityManager.flush();
            entityManager.clear();
        }
        //itemsNameFiller.setItemMap();
        System.out.println("__________________________");
        //System.out.println(itemRepository.findAll().get(0).getName());
        //System.out.println(itemsNameFiller.getItemsMap().get(0));
        System.out.println(myItemRepository.findAll());
        System.out.println("__________________________");
        assertThat(itemsNameFiller.getItemsNames(myItemList()).get(0).getName()).isEqualTo("test0");
    }

    private List<Item> itemsList() {
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            item.setName("test" + i);
            itemList.add(item);
        }
        return itemList;
    }

    private List<MyItem> myItemList(){
        List<MyItem> myItemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MyItem myItem = new MyItem();
            myItem.setId(i+1);
            myItem.setItemId(i+1);
            myItem.setBuyPrice(0.0);
            myItem.setDeliveredToPoland(0);
            myItem.setLastActionDate("2019-09-12 14:19:00");
            myItem.setBuyDate("2019-09-12");
            myItem.setSellerId(1);
            myItemList.add(myItem);
        }
        return myItemList;
    }

}
