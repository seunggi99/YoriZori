package KNU.YoriZori.repository;

import KNU.YoriZori.domain.RecipeBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeBookmarkRepository extends JpaRepository<RecipeBookmark, Long> {
    List<RecipeBookmark> findAllByUserId(Long userId);
}
