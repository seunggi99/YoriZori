package KNU.YoriZori.service;

import KNU.YoriZori.domain.*;
import KNU.YoriZori.repository.FridgeRepository;
import KNU.YoriZori.repository.IngredientRepository;
import KNU.YoriZori.repository.PutInRepository;
import KNU.YoriZori.repository.UserRepository;
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
        Fridge fridge = fridgeRepository.findOne(fridgeId);
        Ingredient ingredient = ingredientRepository.findOne(ingredientId);

        PutIn putIn = PutIn.createPutIn(fridge,ingredient, putDate, storagePlace);
        putInRepository.save(putIn);
    }
}
