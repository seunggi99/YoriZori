package KNU.YoriZori.service;

import KNU.YoriZori.domain.Ingredient;
import KNU.YoriZori.repository.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Transactional
    public void saveIngredient(Ingredient ingredient){
        ingredientRepository.save((ingredient));
    }

    public Ingredient findOne(Long ingredientId){
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found for id: " + ingredientId));
    }
}
