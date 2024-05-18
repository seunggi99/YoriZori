package KNU.YoriZori.repository;

import KNU.YoriZori.domain.RecipeBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeBookmarkRepository extends JpaRepository<RecipeBookmark, Long> {
    List<RecipeBookmark> findAllByUserId(Long userId);
    Optional<RecipeBookmark> findByUserIdAndRecipeId(Long userId, Long recipeId);
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);
}
