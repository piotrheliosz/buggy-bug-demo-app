package app.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@lombok.Data
@lombok.NoArgsConstructor
@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;

    @JsonProperty
    private String name;

    @JsonProperty
    @Column(columnDefinition = "TEXT")
    private String description;

    public Meal(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
