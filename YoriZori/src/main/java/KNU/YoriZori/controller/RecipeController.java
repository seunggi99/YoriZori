package KNU.YoriZori.controller;

import KNU.YoriZori.domain.Ingredient;
import KNU.YoriZori.domain.Recipe;
import KNU.YoriZori.domain.User;
import KNU.YoriZori.dto.IngredientInfoDto;
import KNU.YoriZori.dto.RecipeDetailsDto;
import KNU.YoriZori.dto.UserFilteredRecipeDetailsDto;
import KNU.YoriZori.dto.UserFilteredRecipeDto;
import KNU.YoriZori.service.RecipeBookmarkService;
import KNU.YoriZori.service.RecipeService;
import KNU.YoriZori.service.RecipeStorageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeStorageService storageService;
    private final RecipeBookmarkService recipeBookmarkService;
    // 레시피, 재료 추가
    @PostMapping("/import-recipes")
    public ResponseEntity<String> uploadJsonData(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
            }

            String jsonData = new String(file.getBytes());
            storageService.saveRecipesFromJson(jsonData);
            return ResponseEntity.status(HttpStatus.OK).body("Recipes imported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import recipes.");
        }
    }
    // 모든 레시피 조회
    @GetMapping("/all")
    public ResponseEntity<List<RecipeDto>> getAllRecipes() {
        List<RecipeDto> recipes = recipeService.findAllRecipes()
                .stream()
                .map(recipe -> new RecipeDto(
                        recipe.getId(),
                        recipe.getName(),
                        recipe.getBookmarkCount(),
                        recipe.getImageUrl(),
                        recipe.getCategory().getId(),
                        recipe.getCategory().getName()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipes);
    }

    // 회원 ID를 기반으로 기피 재료가 포함되지 않은 레시피 및 부족한 재료 조회
    @GetMapping("/user-filtered")
    public ResponseEntity<List<UserFilteredRecipeDto>> getUserFilteredRecipes(@AuthenticationPrincipal User user) {
        List<UserFilteredRecipeDto> recipes = recipeService.findUserFilteredRecipes(user.getFridge().getId());
        return ResponseEntity.ok(recipes);
    }

    // 비회원 레시피 상세 페이지
    @GetMapping("/all/{recipeId}")
    public ResponseEntity<RecipeDetailsDto> getRecipesDetail2(@PathVariable Long recipeId){
        Recipe recipe = recipeService.findOne(recipeId);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }


        RecipeDetailsDto responseDto = new RecipeDetailsDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getBookmarkCount(),
                recipe.getImageUrl(),
                recipe.getCategory().getId(),
                recipe.getCategory().getName(),
                null,
                null,
                null
        );
        recipeService.fetchAndMergeExternalData(responseDto);

        return ResponseEntity.ok(responseDto);
    }


    // 회원 레시피 상세 페이지
    @GetMapping("/user-filtered/{recipeId}")
    public ResponseEntity<UserFilteredRecipeDetailsDto> getUserFilteredRecipesDetail(@AuthenticationPrincipal User user, @PathVariable Long recipeId){
        UserFilteredRecipeDetailsDto recipes = recipeService.findUserFilteredRecipeDetails(user.getFridge().getId(),recipeId);
        recipeService.fetchAndMergeExternalData(recipes);
        return ResponseEntity.ok(recipes);
    }


    //레시피 북마크 개수 업데이트
    @PatchMapping("/update/{recipeId}")
    public ResponseEntity<?> updateRecipeBookmarkCount(@PathVariable Long recipeId, @RequestBody int count) {
        recipeService.updateBookmarkCount(recipeId, count);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<UserFilteredRecipeDto2>> getTodayRecommendations(@AuthenticationPrincipal User userPrincipal) {
        List<UserFilteredRecipeDto2> recommendations = recipeService.getDailyRecipes().stream()
                .map(recipe -> {
                    Long recipeId = recipe.getId();
                    Long fridgeId = userPrincipal.getFridge().getId();
                    List<Ingredient> insufficientIngredients = recipeService.findInsufficientIngredient(recipeId, fridgeId);
                    boolean check = recipeBookmarkService.isRecipeBookmarked(userPrincipal.getId(),recipeId);

                    return new UserFilteredRecipeDto2(
                            recipe.getId(),
                            recipe.getName(),
                            check,
                            recipe.getBookmarkCount(),
                            recipe.getImageUrl(),
                            recipe.getCategory().getId(),
                            recipe.getCategory().getName(),
                            insufficientIngredients.size(), // 부족한 재료 없음
                            insufficientIngredients.stream()
                                    .map(ingredient -> new IngredientInfoDto(ingredient.getId(), ingredient.getName()))
                                    .collect(Collectors.toList()) // 부족한 재료 목록 없음
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/recommendations/guest")
    public ResponseEntity<List<UserFilteredRecipeDto3>> getTodayRecommendations2() {
        List<UserFilteredRecipeDto3> recommendations = recipeService.getDailyRecipes().stream()
                .map(recipe -> {
                    Long recipeId = recipe.getId();

                    return new UserFilteredRecipeDto3(
                            recipe.getId(),
                            recipe.getName(),
                            recipe.getBookmarkCount(),
                            recipe.getImageUrl(),
                            recipe.getCategory().getId(),
                            recipe.getCategory().getName()
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(recommendations);
    }
    @Data
    @AllArgsConstructor
    private static class RecipeDto {
        private Long id;
        private String name;
        private int BookmarkCount;
        private String imageUrl;
        private Long categoryId;
        private String categoryName;
    }

    @Data
    @AllArgsConstructor
    private static class UserFilteredRecipeDto2 {
        private Long id;
        private String name;
        private boolean bookmarkCheck;
        private int bookmarkCount;
        private String imageUrl;
        private Long categoryId;
        private String categoryName;
        private int insufficientIngredientsCount;
        private List<IngredientInfoDto> insufficientIngredients;
    }

    @Data
    @AllArgsConstructor
    private static class UserFilteredRecipeDto3 {
        private Long id;
        private String name;
        private int bookmarkCount;
        private String imageUrl;
        private Long categoryId;
        private String categoryName;
    }
}
