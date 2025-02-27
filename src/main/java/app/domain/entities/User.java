package app.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@lombok.Data
@lombok.NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;

    @JsonProperty
    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_meals",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    @JsonProperty
    private final List<Meal> meals = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }
}
