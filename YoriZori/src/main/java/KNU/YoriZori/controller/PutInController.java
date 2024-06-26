package KNU.YoriZori.controller;

import KNU.YoriZori.domain.StoragePlace;
import KNU.YoriZori.domain.User;
import KNU.YoriZori.dto.UpdatePutInRequestDto;
import KNU.YoriZori.service.PutInService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fridges")
public class PutInController {
    private final PutInService putInService;

    // 냉장고에 재료 추가
    @PostMapping("/ingredients")
    public ResponseEntity<Void> addIngredientToFridge(@AuthenticationPrincipal User user,@RequestBody PutInDTO putInDTO) {
        putInService.addIngredientToFridge(user.getFridge().getId(), putInDTO.ingredientId, putInDTO.putDate, putInDTO.expDate, putInDTO.storagePlace);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 냉장고에서 재료 제거
    @DeleteMapping("/ingredients/{putInId}")
    public ResponseEntity<Void> removeIngredientFromFridge(
            @PathVariable Long putInId) {

        putInService.removeIngredientFromFridge(putInId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ingredients/{putInId}")
    public ResponseEntity<List<PutInResponseDto>> updateIngredientFromFridge(
            @PathVariable Long putInId,
            @RequestBody UpdatePutInRequestDto putInDTO){
        putInService.updatePutInFromFridge(putInId, putInDTO);
        return ResponseEntity.ok().build();

    }

    // 냉장고의 재료 목록 조회
    @GetMapping("/ingredients")
    public ResponseEntity<List<PutInResponseDto>> getIngredientsInFridge(
            @AuthenticationPrincipal User user) {
        putInService.updateAllDdays(); //디데이 업데이트
        List<PutInResponseDto> ingredients = putInService.getIngredientsInFridge(user.getFridge().getId()).stream()
                .map(putIn -> new PutInResponseDto(
                        putIn.getId(),
                        putIn.getDDay(),
                        putIn.getExpDate(),
                        putIn.getPutDate(),
                        putIn.getStoragePlace(),
                        putIn.getIngredient().getId(),
                        putIn.getIngredient().getCategory().getImageUrl(),
                        putIn.getIngredient().getCategoryId(),
                        putIn.getIngredient().getCategory().getName(),
                        putIn.getIngredient().getName()
                ))
                .sorted(Comparator.comparingInt(PutInResponseDto::getDDay)) // 디데이 기준으로 오름차순 정렬
                .collect(Collectors.toList());
        return ResponseEntity.ok(ingredients);
    }

    @Data
    static class PutInDTO {
        private Long ingredientId;
        private LocalDate putDate;
        private LocalDate expDate;
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
        private String imageUrl;
        private Long categoryId;
        private String categoryName;
        private String name;
    }

}
