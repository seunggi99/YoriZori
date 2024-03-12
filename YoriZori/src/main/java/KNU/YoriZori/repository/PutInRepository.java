package KNU.YoriZori.repository;

import KNU.YoriZori.domain.PutIn;
import KNU.YoriZori.domain.User;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PutInRepository extends JpaRepository<PutIn, Long> {
    List<PutIn> findAllByFridgeId(Long fridgeId);
}
