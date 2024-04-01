package KNU.YoriZori.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter @Setter
public class Ingredient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long id;

    private String name;
    private int defaultExpDate;
    private String imageUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ingredient_category_id")
    private IngredientCategory category;

    @JsonIgnore
    @OneToMany(mappedBy = "ingredient")
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    public Long getCategoryId() {
        return category.getId();
    }
}

