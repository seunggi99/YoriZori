package KNU.YoriZori.repository;

import KNU.YoriZori.domain.AvoidIngredient;
import KNU.YoriZori.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvoidIngredientRepository extends JpaRepository<AvoidIngredient, Long> {
    List<AvoidIngredient> findByUserId(Long userId);

    Optional<Long>findAvoidIngredientIdByUserIdAndIngredient_Id(Long userId, Long IngredientId);

    Optional<AvoidIngredient> findByUser_IdAndIngredient_Id(Long userId, Long ingredientId);
}
