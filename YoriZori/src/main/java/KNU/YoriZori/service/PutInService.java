package KNU.YoriZori.service;

import KNU.YoriZori.domain.*;
import KNU.YoriZori.dto.UpdatePutInRequestDto;
import KNU.YoriZori.repository.FridgeRepository;
import KNU.YoriZori.repository.IngredientRepository;
import KNU.YoriZori.repository.PutInRepository;
import KNU.YoriZori.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PutInService {
    private final PutInRepository putInRepository;
    private final IngredientRepository ingredientRepository;
    private final FridgeRepository fridgeRepository;

    @Transactional
    public void addIngredientToFridge(Long fridgeId, Long ingredientId, LocalDate putDate,LocalDate expDate, StoragePlace storagePlace) {
        Fridge fridge = fridgeRepository.findById(fridgeId)
                .orElseThrow(() -> new EntityNotFoundException("Fridge not found for id: " + fridgeId));
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found for id: " + ingredientId));

        PutIn putIn = PutIn.createPutIn(fridge,ingredient, putDate, expDate, storagePlace);
        updatePutIn(putIn);
    }

    // 디데이 업데이트
    public void updatePutIn(PutIn putIn) {
        if (putIn.getExpDate() != null) {
            putIn.updateDday(); // D-Day 계산 및 업데이트
            putInRepository.save(putIn); // 변경된 D-Day와 함께 PutIn 정보 저장
        }
        else {
            putIn.setDDay(990808);
            putInRepository.save(putIn);
        }
    }

    // 냉장고 속 재료 업데이트
    @Transactional
    public void updatePutInFromFridge(Long putInId, UpdatePutInRequestDto dto) {

        PutIn putIn = putInRepository.findById(putInId)
                .orElseThrow(() -> new EntityNotFoundException("PutIn not found with id: " + putInId));

        putIn.setExpDate(dto.getExpDate());
        putIn.setPutDate(dto.getPutDate());
        putIn.setStoragePlace(dto.getStoragePlace());

    }

    // 냉장고에서 재료 제거
    @Transactional
    public void removeIngredientFromFridge(Long putInId) {
        Optional<PutIn> putInOptional = putInRepository.findById(putInId);
        putInOptional.ifPresent(putIn -> putInRepository.delete(putIn));
    }

    // 냉장고의 재료 목록 조회
    @Transactional
    public List<PutIn> getIngredientsInFridge(Long fridgeId) {
        return putInRepository.findAllByFridgeId(fridgeId);
    }

    @Async
    public void updateAllDdays() {
        List<PutIn> allPutIns = putInRepository.findAll();
        LocalDate today = LocalDate.now();
        allPutIns.forEach(putIn -> {
            long dDay = ChronoUnit.DAYS.between(today, putIn.getExpDate());
            putIn.setDDay((int)dDay);
        });
        putInRepository.saveAll(allPutIns);
    }
}
