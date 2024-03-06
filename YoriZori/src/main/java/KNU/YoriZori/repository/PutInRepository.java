package KNU.YoriZori.repository;

import KNU.YoriZori.domain.PutIn;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

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

}
