package KNU.YoriZori.controller;

import KNU.YoriZori.domain.PutIn;
import KNU.YoriZori.domain.StoragePlace;
import KNU.YoriZori.service.PutInService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<PutInResponseDto>> getIngredientsInFridge(@PathVariable Long fridgeId) {
        putInService.updateAllDdays(); //디데이 업데이트
        List<PutInResponseDto> ingredients = putInService.getIngredientsInFridge(fridgeId).stream()
                .map(putIn -> new PutInResponseDto(
                        putIn.getId(),
                        putIn.getDDay(),
                        putIn.getExpDate(),
                        putIn.getPutDate(),
                        putIn.getStoragePlace(),
                        putIn.getIngredient().getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ingredients);
    }

    @Data
    static class PutInDTO {
        private Long fridgeId;
        private Long ingredientId;
        private LocalDate putDate;
        private StoragePlace storagePlace;
    }

    @Data
    @AllArgsConstructor
    private static class PutInResponseDto {
        private Long id;
        private int dDay;
        public LocalDate expDate;
        public LocalDate putDate;
        private StoragePlace storagePlace;
        private Long ingredientId;
    }

}
