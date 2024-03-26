package KNU.YoriZori.service;

import KNU.YoriZori.domain.Ingredient;
import KNU.YoriZori.domain.PutIn;
import KNU.YoriZori.domain.RecipeIngredient;
import KNU.YoriZori.repository.PutInRepository;
import KNU.YoriZori.repository.RecipeIngredientRepository;
import KNU.YoriZori.repository.RecipeRepository;
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
}
