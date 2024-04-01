package KNU.YoriZori.controller;

import KNU.YoriZori.domain.Recipe;
import KNU.YoriZori.service.AvoidIngredientService;
import KNU.YoriZori.service.FridgeService;
import KNU.YoriZori.service.RecipeService;
import KNU.YoriZori.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final FridgeService fridgeService;
    private final AvoidIngredientService avoidIngredientService;
    private final UserService userService;

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
    @GetMapping("/recipes/{fridgeId}")
    public ResponseEntity<List<RecipeDetailsDto>> getRecipesExcludingAvoidIngredients(@PathVariable Long fridgeId ) {
        Long userId = fridgeService.findOne(fridgeId).getUser().getId();

        List<RecipeDetailsDto> recipesDetails = recipeService.findRecipesDetailsExcludingAvoidIngredients(userId, fridgeId);
        return ResponseEntity.ok(recipesDetails);
    }

    // 비회원 레시피 상세 페이지
    @GetMapping("/recipes/guest/{recipeId}")
    public ResponseEntity<RecipeResponseDto> getRecipesDetail(@PathVariable Long recipeId){
        Recipe recipe = recipeService.findOne(recipeId);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }
        RecipeResponseDto responseDto = new RecipeResponseDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getIntroduction(),
                recipe.getCookingInstructions(),
                recipe.getImageUrl()
        );
        return ResponseEntity.ok(responseDto);
    }

    // 회원 레시피 상세 페이지
//    @GetMapping("/recipes/{fridgeId}/{recipeId}")
//    public ResponseEntity<List<RecipeResponseDto>> getRecipesDetail(@PathVariable Long fridgeId, @PathVariable Long recipeId){
//    }


    @Data
    @AllArgsConstructor
    private static class RecipeResponseDto {
        private Long id;
        private String name;
        private String introduction;
        private String cookingInstructions;
        private String imageUrl;
    }
    @Data
    @AllArgsConstructor
    public static class RecipeDetailsDto {
        private Long id;
        private String name;
        private String introduction;
        private String cookingInstructions;
        private String imageUrl;
        private int insufficientIngredientsCount;
        private List<Long> insufficientIngredients;

    }
}
