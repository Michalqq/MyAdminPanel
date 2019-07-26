package myapp.MyAdminPanel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Basket {
    private String name;
    private List<String> myItemList;

//    public void addItemToMyList(Optional<MyItem> myItem){
//        if (myItem.isPresent()) myItemList.add(myItem.get());
//    }

        public void add(String text){
            this.myItemList.add(text);
        }
}
