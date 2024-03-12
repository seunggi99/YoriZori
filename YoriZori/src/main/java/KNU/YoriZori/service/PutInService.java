package KNU.YoriZori.service;

import KNU.YoriZori.domain.*;
import KNU.YoriZori.repository.FridgeRepository;
import KNU.YoriZori.repository.IngredientRepository;
import KNU.YoriZori.repository.PutInRepository;
import KNU.YoriZori.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PutInService {
    private final PutInRepository putInRepository;
    private final IngredientRepository ingredientRepository;
    private final FridgeRepository fridgeRepository;

    @Transactional
    public void addIngredientToFridge(Long fridgeId, Long ingredientId, LocalDateTime putDate, StoragePlace storagePlace) {
        Fridge fridge = fridgeRepository.findById(fridgeId)
                .orElseThrow(() -> new EntityNotFoundException("Fridge not found for id: " + fridgeId));
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found for id: " + ingredientId));

        PutIn putIn = PutIn.createPutIn(fridge,ingredient, putDate, storagePlace);
        updatePutIn(putIn);
    }

    public void updatePutIn(PutIn putIn) {
        putIn.updateDday(); // D-Day 계산 및 업데이트
        putInRepository.save(putIn); // 변경된 D-Day와 함께 PutIn 정보 저장
    }
}
