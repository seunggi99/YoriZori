package KNU.YoriZori.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter @Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;

    private String name;

    private String introduction;

    @Lob
    private String cookingInstructions;

    private String imageUrl;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "recipe_category_id")
    private RecipeCategory category;

}

