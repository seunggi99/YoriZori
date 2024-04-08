package KNU.YoriZori.service;

import KNU.YoriZori.domain.*;
import KNU.YoriZori.dto.UpdatePutInRequestDto;
import KNU.YoriZori.repository.CartRepository;
import KNU.YoriZori.repository.IngredientRepository;
import KNU.YoriZori.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final CartRepository cartRepository;

    // 장바구니에 재료 추가
    @Transactional
    public Cart addIngredientToCart(Long userId, Long ingredientId) {
        Cart cart = new Cart();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with id " + ingredientId));

        cart.setUser(user);
        cart.setIngredient(ingredient);
        cart.setPinned(false);

        return cartRepository.save(cart);
    }

    // 장바구니의 재료 제거
    @Transactional
    public void removeIngredientFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    // 장바구니의 재료 업데이트
    @Transactional
    public void updateIngredientFromCart(Long cartId, boolean pinned) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + cartId));

        cart.setPinned(pinned);
    }

    public List<Cart> getAllCartByUserId(Long userId) {
        return cartRepository.findAllByUserId(userId);
    }

    public Cart findOne(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for id: " + cartId));
    }

}
