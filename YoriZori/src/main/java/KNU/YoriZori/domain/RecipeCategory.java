package KNU.YoriZori.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
public class RecipeCategory {
    @Id
    @GeneratedValue
    @Column(name = "ingredient_category_id")
    private Long id;

    private String name;
}
