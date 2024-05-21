package KNU.YoriZori.controller;

import KNU.YoriZori.domain.Cart;
import KNU.YoriZori.domain.User;
import KNU.YoriZori.service.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @GetMapping("users/cart")
    public ResponseEntity<List<CartResponseDto>> getAvoidIngredientsByUserId(@AuthenticationPrincipal User user) {
        List<CartResponseDto> carts = cartService.getAllCartByUserId(user.getId()).stream()
                .map(cart -> new CartResponseDto(
                        cart.getId(),
                        cart.getIngredient().getId(),
                        cart.getIngredient().getName(),
                        cart.getIngredient().getCategory().getImageUrl(),
                        cart.isPinned()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(carts);
    }


    @PostMapping("users/cart")
    public ResponseEntity<?> addAvoidIngredientToUser(@AuthenticationPrincipal User user, @RequestBody List<Long> ingredientIds) {
        for (Long ingredientId : ingredientIds) {
            cartService.addIngredientToCart(user.getId(), ingredientId);
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
        private String name;
        private String imageUrl;
        private boolean pinned;
    }

    @Data
    @AllArgsConstructor
    static class UpdateCartResponse{
        private Long cartId;
        private boolean pinned;
    }
}
