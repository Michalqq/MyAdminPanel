package myapp.MyAdminPanel.controller;

import myapp.MyAdminPanel.model.Basket;
import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BasketController {
    @Autowired
    private MyItemRepository myItemRepository;

    @Autowired
    private Basket basket;


}

