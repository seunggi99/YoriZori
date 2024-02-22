package KNU.YoriZori.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class IngredientCategory {

    @Id @GeneratedValue
    @Column(name = "ingredient_category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Ingredient> ingredients;
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
