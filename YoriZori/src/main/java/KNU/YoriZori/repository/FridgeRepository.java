package KNU.YoriZori.repository;

import KNU.YoriZori.domain.Fridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FridgeRepository extends JpaRepository<Fridge, Long>{

}
