package KNU.YoriZori.repository;

import KNU.YoriZori.domain.AvoidIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvoidIngredientRepository extends JpaRepository<AvoidIngredient, Long> {
}
