package KNU.YoriZori.repository;

import KNU.YoriZori.domain.Cart;
import KNU.YoriZori.domain.PutIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUserId(Long userId);
}
