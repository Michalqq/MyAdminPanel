package myapp.MyAdminPanel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Basket {
    private String name;
    private List<MyItem> myItemList = new ArrayList<>();

//    public void addItemToMyList(Optional<MyItem> myItem){
//        if (myItem.isPresent()) myItemList.add(myItem.get());
//    }

        public void add(MyItem myItem){
            this.myItemList.add(myItem);
        }
}
