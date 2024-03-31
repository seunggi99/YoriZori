package KNU.YoriZori.controller;

import KNU.YoriZori.service.AvoidIngredientService;
import KNU.YoriZori.service.RecipeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final AvoidIngredientService avoidIngredientService;

    // 모든 레시피 조회
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeResponseDto>> getAllRecipes() {
        List<RecipeResponseDto> recipes = recipeService.findAllRecipes()
                .stream()
                .map(recipe -> new RecipeResponseDto(recipe.getId(), recipe.getName(), recipe.getIntroduction(), recipe.getCookingInstructions(), recipe.getImageUrl()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipes);
    }

    // 회원 ID를 기반으로 기피 재료가 포함되지 않은 레시피 조회
    @GetMapping("/recipes/{userId}")
    public ResponseEntity<List<RecipeResponseDto>> getRecipesExcludingAvoidIngredients(@PathVariable Long userId) {
        List<RecipeResponseDto> recipes = recipeService.findRecipesExcludingAvoidIngredients(userId)
                .stream()
                .map(recipe -> new RecipeResponseDto(recipe.getId(), recipe.getName(), recipe.getIntroduction(), recipe.getCookingInstructions(), recipe.getImageUrl()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipes);
    }

    @Data
    @AllArgsConstructor
    private static class RecipeResponseDto {
        private Long id;
        private String name;
        private String introduction;
        private String cookingInstructions;
        private String imageUrl;
    }
}
