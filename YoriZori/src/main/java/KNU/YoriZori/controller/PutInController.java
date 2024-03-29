package KNU.YoriZori.controller;

import KNU.YoriZori.domain.PutIn;
import KNU.YoriZori.domain.StoragePlace;
import KNU.YoriZori.service.PutInService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PutInController {
    private final PutInService putInService;

    // 냉장고에 재료 추가
    @PostMapping("/{fridgeId}/ingredients")
    public ResponseEntity<Void> addIngredientToFridge(@PathVariable Long fridgeId, @RequestBody PutInDTO putInDTO) {
        putInService.addIngredientToFridge(putInDTO.fridgeId, putInDTO.ingredientId, putInDTO.putDate, putInDTO.storagePlace);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 냉장고에서 재료 제거
    @DeleteMapping("/{fridgeId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> removeIngredientFromFridge(@PathVariable Long fridgeId, @PathVariable Long ingredientId) {
        putInService.removeIngredientFromFridge(fridgeId, ingredientId);
        return ResponseEntity.ok().build();
    }

    // 냉장고의 재료 목록 조회
    @GetMapping("/{fridgeId}/ingredients")
    public ResponseEntity<List<PutIn>> getIngredientsInFridge(@PathVariable Long fridgeId) {
        List<PutIn> ingredients = putInService.getIngredientsInFridge(fridgeId);
        return ResponseEntity.ok(ingredients);
    }

    @Data
    static class PutInDTO {
        private Long fridgeId;
        private Long ingredientId;
        private LocalDateTime putDate;
        private StoragePlace storagePlace;
    }
}
