package KNU.YoriZori.service;

import KNU.YoriZori.controller.RecipeController;
import KNU.YoriZori.domain.*;
import KNU.YoriZori.repository.AvoidIngredientRepository;
import KNU.YoriZori.repository.PutInRepository;
import KNU.YoriZori.repository.RecipeIngredientRepository;
import KNU.YoriZori.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final PutInRepository putInRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final AvoidIngredientRepository avoidIngredientRepository;
    public Recipe findOne(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found for id: " + recipeId));
    }

    // 부족한 재료 반환
    public List<Ingredient> findInsufficientIngredient(Long recipeId, Long fridgeId) {
        // 레시피에 필요한 재료 목록 조회
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findAllByRecipeId(recipeId);

        // 냉장고에 있는 재료 목록 조회
        List<PutIn> fridgeIngredients = putInRepository.findAllByFridgeId(fridgeId);

        // 레시피 재료 ID 목록
        Set<Long> recipeIngredientIds = recipeIngredients.stream()
                .map(recipeIngredient -> recipeIngredient.getIngredient().getId())
                .collect(Collectors.toSet());

        // 냉장고 재료 ID 목록
        Set<Long> fridgeIngredientIds = fridgeIngredients.stream()
                .map(putIn -> putIn.getIngredient().getId())
                .collect(Collectors.toSet());

        // 부족한 재료 종류의 수 계산
        recipeIngredientIds.removeAll(fridgeIngredientIds);

        // 부족한 재료의 ID를 기반으로 Ingredient 객체 조회 및 리스트 생성
        List<Ingredient> insufficientIngredients = recipeIngredients.stream()
                .map(RecipeIngredient::getIngredient)
                .filter(ingredient -> recipeIngredientIds.contains(ingredient.getId()))
                .collect(Collectors.toList());

        return insufficientIngredients;
    }

    @Transactional
    public List<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }

    // 회원 ID를 기반으로 기피 재료가 포함되지 않은 레시피 조회
    @Transactional
    public List<RecipeController.RecipeDetailsDto> findRecipesDetailsExcludingAvoidIngredients(Long userId, Long fridgeId) {
        // 사용자의 기피 재료 ID 목록을 조회
        List<Long> avoidIngredientIds = avoidIngredientRepository.findByUserId(userId).stream()
                .map(avoidIngredient -> avoidIngredient.getIngredient().getId()) // 올바른 접근 방식
                .collect(Collectors.toList());

        // 기피 재료를 제외한 모든 레시피 조회
        List<Recipe> filteredRecipes = recipeRepository.findAll()
                .stream()
                .filter(recipe -> recipe.getRecipeIngredients().stream() // Recipe 엔터티에서 메서드 호출
                        .noneMatch(ri -> avoidIngredientIds.contains(ri.getIngredient().getId())))
                .collect(Collectors.toList());

        List<RecipeController.RecipeDetailsDto> recipeDetailsDtos = new ArrayList<>();

        for (Recipe recipe : filteredRecipes) {
            List<Ingredient> insufficientIngredients = findInsufficientIngredient(recipe.getId(), fridgeId);
            List<Long> insufficientIngredientNames = insufficientIngredients.stream()
                    .map(Ingredient::getId)
                    .collect(Collectors.toList());

            RecipeController.RecipeDetailsDto recipeDetailsDto = new RecipeController.RecipeDetailsDto(
                    recipe.getId(),
                    recipe.getName(),
                    recipe.getIntroduction(),
                    recipe.getCookingInstructions(),
                    recipe.getImageUrl(),
                    insufficientIngredients.size(),
                    insufficientIngredientNames);

            recipeDetailsDtos.add(recipeDetailsDto);
        }
        return recipeDetailsDtos;
    }

}
