package projects.todo.persistance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotEmpty
    @Column(nullable = false) // Default length is 255
    private String title;

    @Size(max = 255)
    private String description;

    @ColumnDefault("false")
    private boolean completed;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
