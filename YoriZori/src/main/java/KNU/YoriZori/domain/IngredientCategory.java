package KNU.YoriZori.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class IngredientCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_category_id")
    private Long id;

    private String name;

    private String ImageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Ingredient> ingredients = new ArrayList<>();
}
