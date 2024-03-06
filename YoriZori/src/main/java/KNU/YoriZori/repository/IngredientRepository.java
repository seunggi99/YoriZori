package KNU.YoriZori.repository;

import KNU.YoriZori.domain.Ingredient;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientRepository{
    private final EntityManager em;

    public void save(Ingredient ingredient){
        if (ingredient.getId() == null) {
            em.persist(ingredient);
        } else {
            em.merge(ingredient);
        }
    }

    public Ingredient findOne(Long id) {
        return em.find(Ingredient.class, id);
    }

    public List<Ingredient> findAll() {
        return em.createQuery("select i from Ingredient i", Ingredient.class)
                .getResultList();
    }
}
