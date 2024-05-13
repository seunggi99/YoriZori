package KNU.YoriZori.controller;

import KNU.YoriZori.domain.Recipe;
import KNU.YoriZori.domain.User;
import KNU.YoriZori.dto.UserFilteredRecipeDetailsDto;
import KNU.YoriZori.dto.UserFilteredRecipeDto;
import KNU.YoriZori.service.RecipeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

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

//    // 비회원 레시피 상세 페이지
//    @GetMapping("/all/{recipeId}")
//    public ResponseEntity<RecipeResponseDto> getRecipesDetail(@PathVariable Long recipeId){
//        Recipe recipe = recipeService.findOne(recipeId);
//        if (recipe == null) {
//            return ResponseEntity.notFound().build();
//        }
//        RecipeResponseDto responseDto = new RecipeResponseDto(
//                recipe.getId(),
//                recipe.getName(),
//                recipe.getIntroduction(),
//                recipe.getCookingInstructions(),
//                recipe.getImageUrl(),
//                recipe.getCategory().getId(),
//                recipe.getCategory().getName()
//        );
//        return ResponseEntity.ok(responseDto);
//    }

    @GetMapping("/all/{recipeId}")
    public Mono<ResponseEntity<RecipeResponseDto>> getRecipesDetail(@PathVariable Long recipeId) {
        Recipe recipe = recipeService.findOne(recipeId);
        if (recipe == null) {
            return Mono.just(ResponseEntity.notFound().build());
        }

        return recipeService.getAdditionalRecipeInfo(recipe.getName())
                .map(additionalInfo -> {
                    if (additionalInfo == null) {
                        // 만약 additionalInfo가 null이면 예외 상황 처리
                        // 이 경우에는 예를 들어 빈 RecipeResponseDto를 반환하거나 다른 적절한 처리를 수행할 수 있습니다.
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }

                    RecipeResponseDto responseDto = new RecipeResponseDto(
                            recipe.getId(),
                            recipe.getName(),
                            recipe.getBookmarkCount(),
                            recipe.getImageUrl(),
                            recipe.getCategory().getId(),
                            recipe.getCategory().getName(),
                            additionalInfo.getRCP_PARTS_DTLS(),
                            additionalInfo.getMANUAL(),
                            additionalInfo.getMANUAL_IMG()
                    );
                    return ResponseEntity.ok(responseDto);
                });
    }

    // 회원 레시피 상세 페이지
    @GetMapping("/user-filtered/{recipeId}")
    public ResponseEntity<UserFilteredRecipeDetailsDto> getUserFilteredRecipesDetail(@AuthenticationPrincipal User user, @PathVariable Long recipeId){
        UserFilteredRecipeDetailsDto recipes = recipeService.findUserFilteredRecipeDetails(user.getFridge().getId(),recipeId);

        return ResponseEntity.ok(recipes);
    }


    //레시피 북마크 개수 업데이트
    @PatchMapping("/update/{recipeId}")
    public ResponseEntity<?> updateRecipeBookmarkCount(@PathVariable Long recipeId, @RequestBody int count) {
        recipeService.updateBookmarkCount(recipeId, count);
        return ResponseEntity.ok().build();
    }
    @Data
    @AllArgsConstructor
    public static class RecipeResponseDto {

        public RecipeResponseDto() {
            // 기본 생성자 추가
        }
        private Long id;
        private String name;
        private int BookmarkCount;
        private String imageUrl;
        private Long categoryId;
        private String categoryName;
        private String RCP_PARTS_DTLS;
        private List<String> MANUAL;
        private List<String> MANUAL_IMG;
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
