package myapp.MyAdminPanel.repository;


import myapp.MyAdminPanel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}