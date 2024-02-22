package KNU.YoriZori.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PutIngredient {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    private LocalDateTime putDate;

    @Enumerated(EnumType.STRING)
    private StoragePlace storagePlace;

}

