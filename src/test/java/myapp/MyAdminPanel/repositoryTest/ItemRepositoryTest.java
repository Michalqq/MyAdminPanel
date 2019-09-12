package myapp.MyAdminPanel.repositoryTest;

import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void whenFindByName_thenReturnItem() {
        Item item = new Item();
        item.setName("TestName");
        entityManager.persist(item);
        entityManager.flush();

        Item found = itemRepository.findByName(item.getName());
        assertThat(found.getName()).isEqualTo(item.getName());

    }
    @Test
    public void findByIdLessThanTest() {
        Item item = new Item();
        Item item2 = new Item();
        item.setName("test");
        item2.setName("test2");
        entityManager.persist(item);
        entityManager.persist(item2);
        entityManager.flush();

        List<Item> found = itemRepository.findByIdLessThan(item2.getId());
        List<Item> found2 = itemRepository.findByIdLessThan(item2.getId()+1);

        assertThat(found.size()).isEqualTo(1);
        assertThat(found2.size()).isEqualTo(2);
    }

}
