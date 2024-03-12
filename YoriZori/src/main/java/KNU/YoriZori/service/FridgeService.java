package KNU.YoriZori.service;

import KNU.YoriZori.domain.Fridge;
import KNU.YoriZori.repository.FridgeRepository;
import KNU.YoriZori.repository.IngredientRepository;
import KNU.YoriZori.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FridgeService {
    private final FridgeRepository fridgeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    public Fridge findOne(Long fridgeId) {
        return fridgeRepository.findById(fridgeId)
                .orElseThrow(() -> new EntityNotFoundException("Fridge not found for id: " + fridgeId));
    }
}
