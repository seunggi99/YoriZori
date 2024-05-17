package KNU.YoriZori.service;

import KNU.YoriZori.JsonFileReader;
import KNU.YoriZori.domain.Ingredient;
import KNU.YoriZori.domain.Recipe;
import KNU.YoriZori.domain.RecipeCategory;
import KNU.YoriZori.domain.RecipeIngredient;
import KNU.YoriZori.dto.RecipeImportDto;
import KNU.YoriZori.repository.IngredientRepository;
import KNU.YoriZori.repository.RecipeCategoryRepository;
import KNU.YoriZori.repository.RecipeIngredientRepository;
import KNU.YoriZori.repository.RecipeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeStorageService {


    private final RecipeRepository recipeRepository;
    private final RecipeCategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final EntityManager entityManager;

    @Transactional
    public void saveRecipesFromJson(String jsonData) {
        List<RecipeImportDto> recipes = JsonFileReader.parseJson(jsonData);
        saveRecipes(recipes);
    }

    @Transactional
    public void saveRecipes(List<RecipeImportDto> recipes) {
        int batchSize = 50; // 배치 크기를 조정할 수 있습니다
        List<Recipe> recipeEntities = new ArrayList<>();
        List<RecipeIngredient> recipeIngredientEntities = new ArrayList<>();

        for (int i = 0; i < recipes.size(); i++) {
            RecipeImportDto dto = recipes.get(i);

            // 레시피 카테고리 저장 또는 조회
            Optional<RecipeCategory> categoryOpt = categoryRepository.findByName(dto.getRecipeCategory());
            RecipeCategory category;
            if (categoryOpt.isPresent()) {
                category = categoryOpt.get();
            } else {
                category = new RecipeCategory();
                category.setName(dto.getRecipeCategory());
                categoryRepository.save(category);
            }

            // 레시피 저장
            Recipe recipe = new Recipe();
            recipe.setName(dto.getName());
            recipe.setImageUrl(dto.getImageUrl());
            recipe.setCategory(category);
            recipeEntities.add(recipe);

            // 재료 저장 및 레시피-재료 매핑
            for (String ingredientName : dto.getIngredients()) {
                Optional<Ingredient> ingredientOpt = ingredientRepository.findByName(ingredientName);
                Ingredient ingredient;
                if (ingredientOpt.isPresent()) {
                    ingredient = ingredientOpt.get();
                } else {
                    ingredient = new Ingredient();
                    ingredient.setName(ingredientName);
                    ingredientRepository.save(ingredient);
                }

                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setRecipe(recipe);
                recipeIngredient.setIngredient(ingredient);
                recipeIngredientEntities.add(recipeIngredient);
            }

            if (i > 0 && i % batchSize == 0) {
                recipeRepository.saveAll(recipeEntities);
                recipeIngredientRepository.saveAll(recipeIngredientEntities);
                entityManager.flush();
                entityManager.clear();

                recipeEntities.clear();
                recipeIngredientEntities.clear();
            }
        }

        if (!recipeEntities.isEmpty()) {
            recipeRepository.saveAll(recipeEntities);
            recipeIngredientRepository.saveAll(recipeIngredientEntities);
        }
    }
}