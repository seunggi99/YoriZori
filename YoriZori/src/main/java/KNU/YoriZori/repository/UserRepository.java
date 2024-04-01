package KNU.YoriZori.repository;

import KNU.YoriZori.domain.Recipe;
import KNU.YoriZori.domain.RecipeIngredient;
import KNU.YoriZori.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByNameAndPassword(String name, String password);
}
