package KNU.YoriZori.service;

import KNU.YoriZori.domain.*;
import KNU.YoriZori.repository.AvoidIngredientRepository;
import KNU.YoriZori.repository.PutInRepository;
import KNU.YoriZori.repository.RecipeIngredientRepository;
import KNU.YoriZori.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        // 사용자가 기피하는 재료 목록을 조회
        List<Long> avoidIngredientIds = avoidIngredientRepository.findByUserId(userId)
                .stream()
                .map(avoidIngredient -> avoidIngredient.getIngredient().getId())
                .collect(Collectors.toList());

        // 기피 재료를 포함하지 않는 레시피 목록 조회 로직 구현
        List<Long> avoidRecipeIds = recipeRepository.findRecipesWithIngredients(avoidIngredientIds)
                .stream()
                .map(Recipe::getId)
                .distinct()
                .collect(Collectors.toList());

        return recipeRepository.findByIdNotIn(avoidRecipeIds);
    }

}
