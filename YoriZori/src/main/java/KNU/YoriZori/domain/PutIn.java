package KNU.YoriZori.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "fridge_id")
    private Fridge fridge;

    private LocalDate putDate;
    private LocalDate expDate;
    private int dDay;

    @Enumerated(EnumType.STRING)
    private StoragePlace storagePlace;


    public static PutIn createPutIn(Fridge fridge, Ingredient ingredient, LocalDate putDate, LocalDate expDate, StoragePlace storagePlace){
        PutIn putIn = new PutIn();
        putIn.setFridge(fridge);
        putIn.setIngredient(ingredient);
        putIn.setPutDate(putDate);
        putIn.setStoragePlace(storagePlace);
        putIn.setExpDate(expDate);
        return putIn;
    }
    public void updateDday() {
        LocalDate now = LocalDate.now(); // 현재 날짜
        this.dDay = (int) ChronoUnit.DAYS.between(now, this.expDate); // 소비 기한까지 남은 일수 계산
    }
}

