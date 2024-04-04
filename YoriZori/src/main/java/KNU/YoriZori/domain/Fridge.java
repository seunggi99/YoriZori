package KNU.YoriZori.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Fridge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fridge_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "fridge", cascade = CascadeType.ALL)
    private List<PutIn> putIns = new ArrayList<>();

    public void setUser (User user){
        this.user = user;
        user.setFridge(this);
    }
    public void addPutIn(PutIn putIn){
        putIns.add(putIn);
        putIn.setFridge(this);
    }

    public static Fridge createFridge(User user){
        Fridge fridge = new Fridge();
        fridge.setUser(user);

        return fridge;
    }

}

