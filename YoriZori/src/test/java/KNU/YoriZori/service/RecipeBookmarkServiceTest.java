package KNU.YoriZori.service;

import KNU.YoriZori.domain.Recipe;
import KNU.YoriZori.domain.RecipeBookmark;
import KNU.YoriZori.domain.User;
import KNU.YoriZori.repository.RecipeBookmarkRepository;
import KNU.YoriZori.repository.RecipeRepository;
import KNU.YoriZori.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RecipeBookmarkServiceTest {
    @Autowired
    private RecipeBookmarkService recipeBookmarkService;

    @Autowired
    private RecipeBookmarkRepository recipeBookmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    public void testAddBookmark() {
        User user = new User();
        user = createUser("testUser");

        Recipe recipe = new Recipe();
        recipe = createRecipe("testRecipe");

        RecipeBookmark bookmark = recipeBookmarkService.addBookmark(user.getId(), recipe.getId());
        assertNotNull(bookmark);
        assertNotNull(bookmark.getId());
        assertEquals(user.getId(), bookmark.getUser().getId());
        assertEquals(recipe.getId(), bookmark.getRecipe().getId());
    }

    @Test
    public void testRemoveBookmark() {
        User user = new User();
        user = createUser("testUser");

        Recipe recipe = new Recipe();
        recipe = createRecipe("testRecipe");

        RecipeBookmark bookmark = recipeBookmarkService.addBookmark(user.getId(), recipe.getId());
        Long bookmarkId = bookmark.getId();
        recipeBookmarkService.removeBookmark(bookmarkId);
        assertFalse(recipeBookmarkRepository.existsById(bookmarkId));
    }

    @Test
    public void testFindBookmarksByUser() {
        User user = new User();
        user = createUser("testUser");

        Recipe recipe = new Recipe();
        recipe = createRecipe("testRecipe");

        recipeBookmarkService.addBookmark(user.getId(), recipe.getId());
        assertFalse(recipeBookmarkService.findBookmarksByUser(user.getId()).isEmpty());
    }
    private User createUser(String username) {
        User newUser = new User();
        newUser.setName(username);
        // 저장된 User 객체를 반환
        return userRepository.save(newUser);
    }


    private Recipe createRecipe(String recipeName) {
        Recipe newRecipe = new Recipe();
        newRecipe.setName(recipeName);
        // 저장된 Recipe 객체를 반환
        return recipeRepository.save(newRecipe);
    }

}