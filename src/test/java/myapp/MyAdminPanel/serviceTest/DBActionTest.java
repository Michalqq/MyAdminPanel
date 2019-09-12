package myapp.MyAdminPanel.serviceTest;

import myapp.MyAdminPanel.repository.MyItemRepository;
import myapp.MyAdminPanel.service.DBAction;
import myapp.MyAdminPanel.service.MyItemDBAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DBActionTest{ //todo
//
//    @Autowired
//    private DBAction dbAction;
//
////    @Autowired
////    private MyItemDBAction myItemDBAction;
//
//    @Autowired
//    private MyItemRepository myItemRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//    public void saveTest(){
//        //dbAction.setMyItemRepository(myItemRepository);
//        dbAction.createNewItem(1.1,1,1);
//        System.out.println(myItemRepository.findAll().size());
//    }
}
