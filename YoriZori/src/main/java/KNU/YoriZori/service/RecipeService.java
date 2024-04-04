package KNU.YoriZori.service;

import KNU.YoriZori.controller.RecipeController;
import KNU.YoriZori.domain.*;
import KNU.YoriZori.dto.UserFilteredRecipeDetailsDto;
import KNU.YoriZori.dto.UserFilteredRecipeDto;
import KNU.YoriZori.repository.*;
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
    private final UserRepository userRepository;
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
    public List<Recipe> findRecipesExcludingAvoidIngredients(Long userId) {
        List<Long> avoidIngredientIds = avoidIngredientRepository.findByUserId(userId)
                .stream()
                .map(avoidIngredient -> avoidIngredient.getIngredient().getId())
                .collect(Collectors.toList());

        // 기피 재료 목록이 비어 있으면 모든 레시피 반환
        if (avoidIngredientIds.isEmpty()) {
            return recipeRepository.findAll();
        }

        // 기피 재료를 포함하는 레시피 ID 목록 조회
        List<Long> avoidRecipeIds = recipeRepository.findRecipesWithIngredients(avoidIngredientIds)
                .stream()
                .map(Recipe::getId)
                .distinct()
                .collect(Collectors.toList());

        // 기피 재료를 포함하지 않는 레시피 목록 조회
        return recipeRepository.findByIdNotIn(avoidRecipeIds);
    }

    // 기피 재료가 포함되지 않은 레시피 및 부족한 재료 목록과 개수 조회
    @Transactional
    public List<UserFilteredRecipeDto> findUserFilteredRecipes(Long fridgeId) {

        Long userId = userRepository.findByFridgeId(fridgeId).getId();

        // 기피 재료를 포함하지 않는 레시피 목록 조회
        List<Recipe> recipesExcludingAvoidIngredients = findRecipesExcludingAvoidIngredients(userId);

        // 결과 DTO 리스트 생성
        List<UserFilteredRecipeDto> filteredRecipes = new ArrayList<>();

        for (Recipe recipe : recipesExcludingAvoidIngredients) {
            // 각 레시피에 대한 부족한 재료 목록 조회
            List<Ingredient> insufficientIngredients = findInsufficientIngredient(recipe.getId(), fridgeId);

            // 부족한 재료의 ID 목록 생성
            List<Long> insufficientIngredientIds = insufficientIngredients.stream()
                    .map(Ingredient::getId)
                    .collect(Collectors.toList());

            // UserFilteredRecipeDto 객체 생성 및 추가
            filteredRecipes.add(new UserFilteredRecipeDto(
                    recipe.getId(),
                    recipe.getName(),
                    recipe.getImageUrl(),
                    insufficientIngredients.size(), // 부족한 재료의 개수
                    insufficientIngredientIds       // 부족한 재료 ID 목록
            ));
        }

        return filteredRecipes;
    }

    @Transactional
    public List<UserFilteredRecipeDetailsDto> findUserFilteredRecipeDetails(Long fridgeId, Long recipeId) {

        Long userId = userRepository.findByFridgeId(fridgeId).getId();
        Recipe recipe = findOne(recipeId);
        // 결과 DTO 리스트 생성
        List<UserFilteredRecipeDetailsDto> filteredRecipes = new ArrayList<>();

        // 각 레시피에 대한 부족한 재료 목록 조회
        List<Ingredient> insufficientIngredients = findInsufficientIngredient(recipeId, fridgeId);

        // 부족한 재료의 ID 목록 생성
        List<Long> insufficientIngredientIds = insufficientIngredients.stream()
                    .map(Ingredient::getId)
                    .collect(Collectors.toList());

        // UserFilteredRecipeDto 객체 생성 및 추가
        filteredRecipes.add(new UserFilteredRecipeDetailsDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getIntroduction(),
                recipe.getCookingInstructions(),
                recipe.getImageUrl(),
                insufficientIngredients.size(), // 부족한 재료의 개수
                insufficientIngredientIds       // 부족한 재료 ID 목록
        ));
        return filteredRecipes;
    }
}
