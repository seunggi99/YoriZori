package KNU.YoriZori.service;

import KNU.YoriZori.domain.*;
import KNU.YoriZori.repository.AvoidIngredientRepository;
import KNU.YoriZori.repository.IngredientRepository;
import KNU.YoriZori.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvoidIngredientService {
    private final AvoidIngredientRepository avoidIngredientRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    // 기피 재료 추가
    @Transactional
    public AvoidIngredient addAvoidIngredient(Long userId, Long ingredientId) {
        AvoidIngredient avoidIngredient = new AvoidIngredient();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id " + ingredientId));

        avoidIngredient.setUser(user);
        avoidIngredient.setIngredient(ingredient);

        return avoidIngredientRepository.save(avoidIngredient);
    }

    // 기피 재료 제거
    @Transactional
    public void removeAvoidIngredient(Long avoidIngredientId) {
        avoidIngredientRepository.deleteById(avoidIngredientId);
    }

}
