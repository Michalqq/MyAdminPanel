package myapp.MyAdminPanel.repositoryTest;

import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

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

}
