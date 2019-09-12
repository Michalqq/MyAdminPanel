package myapp.MyAdminPanel.repository;


import myapp.MyAdminPanel.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByIdLessThan(int id);
    Item findByName(String name);
}
