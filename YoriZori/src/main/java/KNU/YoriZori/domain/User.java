package KNU.YoriZori.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long Id;

    private String name;

    private String password;

    private String nickname;

    @OneToMany(mappedBy = "user")
    private List<Fridge> fridges = new ArrayList<>();
}

