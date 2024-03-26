package KNU.YoriZori.service;

import KNU.YoriZori.domain.*;
import KNU.YoriZori.repository.*;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class RecipeServiceTest {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private PutInService putInService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private PutInRepository putInRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testCountMissingIngredientsTypes() {

        // Fridge 엔터티 생성 및 저장
        Fridge fridge = new Fridge();
        fridge = fridgeRepository.save(fridge);

        // 레시피 생성 및 저장
        Recipe recipe = new Recipe();
        recipe.setName("Sample Recipe1");
        recipe = recipeRepository.save(recipe);

        // 재료 생성 및 저장
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Sample Ingredient1");
        ingredient.setDefaultExpDate(1);
        ingredient = ingredientRepository.save(ingredient);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Sample Ingredient2");
        ingredient2.setDefaultExpDate(2);
        ingredient2 = ingredientRepository.save(ingredient2);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setName("Sample Ingredient2");
        ingredient3.setDefaultExpDate(2);
        ingredient3 = ingredientRepository.save(ingredient3);

        // 레시피 재료 생성 및 저장
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredientRepository.save(recipeIngredient);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient();
        recipeIngredient2.setRecipe(recipe);
        recipeIngredient2.setIngredient(ingredient2);
        recipeIngredientRepository.save(recipeIngredient2);

        RecipeIngredient recipeIngredient3 = new RecipeIngredient();
        recipeIngredient3.setRecipe(recipe);
        recipeIngredient3.setIngredient(ingredient3);
        recipeIngredientRepository.save(recipeIngredient3);

        // 냉장고에 재료 추가
        putInService.addIngredientToFridge(fridge.getId(), ingredient.getId(), LocalDateTime.now(), StoragePlace.COLD);

        // 부족한 재료
        List<Ingredient> insufficientIngredients = recipeService.findInsufficientIngredient(recipe.getId(), fridge.getId());

        // 기대하는 부족한 재료의 ID 목록 생성
        List<Long> expectedInsufficientIngredientIds = new ArrayList<>();
        expectedInsufficientIngredientIds.add(ingredient2.getId());
        expectedInsufficientIngredientIds.add(ingredient3.getId());

        // 실제 부족한 재료의 ID 목록 생성
        List<Long> actualInsufficientIngredientIds = insufficientIngredients.stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());

        System.out.println("******************************");
        System.out.println(actualInsufficientIngredientIds);
        // 검증
        // 부족한 재료 개수
        assertEquals(2, insufficientIngredients.size(), "부족한 재료의 종류 수가 예상과 다릅니다.");
        // 부족한 재료 리스트 비교
        assertTrue(actualInsufficientIngredientIds.containsAll(expectedInsufficientIngredientIds), "부족한 재료 목록이 예상과 다릅니다.");
    }
}