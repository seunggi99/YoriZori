package KNU.YoriZori.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class RecipeIngredient {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    private Ingredient ingredient;
}
