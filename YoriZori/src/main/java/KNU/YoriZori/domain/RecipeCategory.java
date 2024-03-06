package KNU.YoriZori.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
public class RecipeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Recipe> recipes = new ArrayList<>();
}
