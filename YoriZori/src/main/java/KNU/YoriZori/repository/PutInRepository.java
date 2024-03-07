package KNU.YoriZori.repository;

import KNU.YoriZori.domain.PutIn;
import KNU.YoriZori.domain.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PutInRepository {
    private final EntityManager em;

    public PutInRepository(EntityManager em){
        this.em = em;
    }

    public void save(PutIn putIn) {
        em.persist(putIn);
    }

    public PutIn findOne(Long id) {
        return em.find(PutIn.class, id);
    }

    public List<PutIn> findAllByFridgeId(Long fridgeId) {
        return em.createQuery("SELECT p FROM PutIn p WHERE p.fridge.id = :fridgeId", PutIn.class)
                .setParameter("fridgeId", fridgeId)
                .getResultList();
    }
}
