package KNU.YoriZori.controller;

import KNU.YoriZori.service.IngredientService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping("/ingredients")
    public ResponseEntity<List<IngredientResponseDto>> getAllIngredients() {
        List<IngredientResponseDto> ingredients = ingredientService.getAllIngredients().stream()
                .map(ingredient -> new IngredientResponseDto(
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getDefaultExpDate(),
                        ingredient.getImageUrl(),
                        ingredient.getCategoryId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ingredients);
    }


    @Data
    @AllArgsConstructor
    public class IngredientResponseDto {
        private Long id;
        private String name;
        private int defaultExpDate;
        private String imageUrl;
        private Long categoryId;
    }
}
