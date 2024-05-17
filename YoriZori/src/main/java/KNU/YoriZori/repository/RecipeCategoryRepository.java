package KNU.YoriZori.repository;

import KNU.YoriZori.domain.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {
    Optional<RecipeCategory> findByName(String name);
}

