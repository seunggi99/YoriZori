package KNU.YoriZori.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class RecipeBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_bookmark_id")
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Recipe recipe;
}
