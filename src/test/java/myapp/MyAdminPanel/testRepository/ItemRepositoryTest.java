package myapp.MyAdminPanel.testRepository;

import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void whenFindByName_thenReturnItem() {

        Item item = new Item();
        item.setName("testName");
        testEntityManager.persist(item);
        testEntityManager.flush();

        Item found = itemRepository.findByName(item.getName());
        assertThat(found.getName()).isEqualTo(item.getName());

    }

}
