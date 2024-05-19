package KNU.YoriZori.repository;

import KNU.YoriZori.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("SELECT r FROM Recipe r JOIN r.ingredients ri WHERE ri.ingredient.id IN :ingredientIds")
    List<Recipe> findRecipesWithIngredients(@Param("ingredientIds") List<Long> ingredientIds);

    @Query(value = "SELECT * FROM recipe ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Recipe> findRandomRecipes(@Param("count") int count);
    
    List<Recipe> findByIdNotIn(List<Long> recipeIds);
}
