package myapp.MyAdminPanel.service;

import lombok.Getter;
import lombok.Setter;
import myapp.MyAdminPanel.model.Item;
import myapp.MyAdminPanel.model.MyItem;
import myapp.MyAdminPanel.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Setter
@Getter
public class ItemsNameFiller {
    @Autowired
    private ItemRepository itemRepository;

    private Map<Integer, String> itemMap = new HashMap<>();

    public Map<Integer, String> getItemsMap() {
        if (itemMap.isEmpty()) {
            setItemMap();
        }
        return itemMap;
    }

    public void setItemMap() {
        List<Item> items = itemRepository.findAll();
        itemMap = new HashMap<>();
        for (Item item : items) {
            itemMap.put(item.getId(), item.getName());
        }
    }

    public List<MyItem> getItemsNames(List<MyItem> itemList) {
        Map<Integer, String> itemMap = this.getItemsMap();
        for (MyItem myItem : itemList) {
            myItem.setName(itemMap.get(myItem.getItemId()));
        }
        return itemList;
    }

    public void setItemMap(Map<Integer, String> itemMap) {
        this.itemMap = itemMap;
    }
}
