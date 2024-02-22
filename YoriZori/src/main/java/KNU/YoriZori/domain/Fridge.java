package KNU.YoriZori.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fridge {
    @Id
    @GeneratedValue
    @Column(name = "fridge_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "fridge")
    private List<PutIngredient> putIngredients = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    private Date putInDate;

    @Temporal(TemporalType.DATE)
    private Date expDate;

    @Enumerated(EnumType.STRING)
    private StoragePlace storagePlace;
}

