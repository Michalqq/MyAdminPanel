package myapp.MyAdminPanel.repository;

import myapp.MyAdminPanel.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellersRepository extends JpaRepository<Seller, Integer> {
}
