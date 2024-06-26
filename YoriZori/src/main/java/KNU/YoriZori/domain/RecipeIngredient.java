package KNU.YoriZori.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_ingredient_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ingredient_id", foreignKey = @ForeignKey(name = "FK_recipe_ingredient_ingredient_id", value = ConstraintMode.CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (ingredient_id) REFERENCES ingredient (ingredient_id) ON DELETE CASCADE"))
    private Ingredient ingredient;
}
