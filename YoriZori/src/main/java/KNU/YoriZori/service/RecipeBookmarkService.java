package KNU.YoriZori.service;

import KNU.YoriZori.domain.Recipe;
import KNU.YoriZori.domain.RecipeBookmark;
import KNU.YoriZori.domain.User;
import KNU.YoriZori.repository.RecipeBookmarkRepository;
import KNU.YoriZori.repository.RecipeRepository;
import KNU.YoriZori.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeBookmarkService {

    private final RecipeBookmarkRepository recipeBookmarkRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    // 북마크 추가
    @Transactional
    public RecipeBookmark addBookmark(Long userId, Long recipeId) {
        RecipeBookmark bookmark = new RecipeBookmark();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id " + recipeId));

        bookmark.setUser(user);
        bookmark.setRecipe(recipe);

        return recipeBookmarkRepository.save(bookmark);
    }

    // 북마크 제거
    @Transactional
    public void removeBookmark(Long bookmarkId) {
        recipeBookmarkRepository.deleteById(bookmarkId);
    }

    // 북마크 레시피 조회
    @Transactional(readOnly = true)
    public List<Recipe> findBookmarksByUser(Long userId) {
        List<RecipeBookmark> bookmarks = recipeBookmarkRepository.findAllByUserId(userId);
        return bookmarks.stream().map(RecipeBookmark::getRecipe).collect(Collectors.toList());
    }
}
