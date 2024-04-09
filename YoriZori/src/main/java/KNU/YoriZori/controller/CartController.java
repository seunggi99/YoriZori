package KNU.YoriZori.controller;

import KNU.YoriZori.domain.Cart;
import KNU.YoriZori.domain.User;
import KNU.YoriZori.service.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("users/{userId}/cart")
    public ResponseEntity<List<CartResponseDto>> getAvoidIngredientsByUserId(@PathVariable Long userId) {
        List<CartResponseDto> avoidIngredients = cartService.getAllCartByUserId(userId).stream()
                .map(cart -> new CartResponseDto(
                        cart.getId(),
                        cart.getIngredient().getId(),
                        cart.isPinned()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(avoidIngredients);
    }

    @PostMapping("users/{userId}/cart")
    public ResponseEntity<?> addAvoidIngredientToUser(@PathVariable Long userId, @RequestBody List<Long> ingredientIds) {
        for (Long ingredientId : ingredientIds) {
            cartService.addIngredientToCart(userId, ingredientId);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("users/cart")
    public UpdateCartResponse updateCartPinned(
            @RequestBody @Valid UpdateCartResponse request){
        cartService.updateIngredientFromCart(request.cartId, request.pinned);
        Cart findCart = cartService.findOne(request.cartId);
        return new UpdateCartResponse(findCart.getId(), findCart.isPinned());
    }

    @DeleteMapping("users/cart/{cartId}")
    public ResponseEntity<?> removeCartFromUser(
            @PathVariable Long cartId) {
        if (cartId != null && cartId != 0) {
            cartService.removeIngredientFromCart(cartId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Data
    @AllArgsConstructor
    public class CartResponseDto{
        private Long cartId;
        private Long ingredientId;
        private boolean pinned;
    }

    @Data
    @AllArgsConstructor
    static class UpdateCartResponse{
        private Long cartId;
        private boolean pinned;
    }
}
