package KNU.YoriZori.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.EnableMBeanExport;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
public class Ingredient {
    @Id @GeneratedValue
    @Column(name = "ingredient_id")
    private Long id;

    private String name;

    private int defaultExpDate;

    @OneToMany(mappedBy = "ingredient")
    private List<PutIngredient> putIngredients = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "ingredient_category_id")
    private IngredientCategory category;

}

