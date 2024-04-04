package KNU.YoriZori.repository;


import KNU.YoriZori.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByNameAndPassword(String name, String password);

    User findByFridgeId (Long fridgeId);
}
