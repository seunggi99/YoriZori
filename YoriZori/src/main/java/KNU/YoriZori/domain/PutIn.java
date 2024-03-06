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
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PutIn {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "put_in_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    private LocalDateTime putDate;
    private LocalDateTime expdate;
    private Long dday;

    @Enumerated(EnumType.STRING)
    private StoragePlace storagePlace;


    public static PutIn createPutIn(Ingredient ingredient, LocalDateTime putDate, StoragePlace storagePlace, LocalDateTime expdate){
        PutIn putIn = new PutIn();
        putIn.setIngredient(ingredient);
        putIn.setPutDate(putDate);
        putIn.setStoragePlace(storagePlace);
        putIn.setExpdate(putDate.plusDays(ingredient.getDefaultExpDate()));
        return putIn;
    }
}

