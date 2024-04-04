package KNU.YoriZori.service;

import KNU.YoriZori.domain.Recipe;
import KNU.YoriZori.domain.User;
import KNU.YoriZori.dto.UserFilteredRecipeDto;
import KNU.YoriZori.repository.*;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class RecipeServiceTest2 {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private PutInRepository putInRepository;
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;
    @Autowired
    private AvoidIngredientRepository avoidIngredientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findUserFilteredRecipes_WithNoIngredientsAndAvoids_ShouldReturnAllRecipesWithInsufficiencies() {
        // Given
        Long fridgeId = 1L; // 예시 냉장고 ID
        Long userId = 1L; // 예시 사용자 ID
        when(userRepository.findByFridgeId(fridgeId)).thenReturn(new User());
        when(avoidIngredientRepository.findByUserId(userId)).thenReturn(new ArrayList<>()); // 기피 재료 없음
        when(putInRepository.findAllByFridgeId(fridgeId)).thenReturn(new ArrayList<>()); // 냉장고에 재료 없음
        List<Recipe> allRecipes = List.of(/* 모든 레시피 초기화 */);
        when(recipeRepository.findAll()).thenReturn(allRecipes); // 모든 레시피 반환

        // When
        List<UserFilteredRecipeDto> result = recipeService.findUserFilteredRecipes(fridgeId);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty()); // 결과가 비어있지 않아야 함
        assertEquals(allRecipes.size(), result.size()); // 모든 레시피가 반환되어야 함
        result.forEach(dto -> {
            assertNotNull(dto.getInsufficientIngredients());
            assertFalse(dto.getInsufficientIngredients().isEmpty()); // 모든 레시피가 부족한 재료를 가져야 함
        });
    }
}
