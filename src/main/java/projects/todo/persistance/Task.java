package projects.todo.persistance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty
    @Column(nullable = false) // Default length is 255
    private String title;

    @Size(max = 255)
    private String description;

    @ColumnDefault("false")
    private boolean completed;

    @ManyToMany
    @JoinTable(
            name = "task_task_list",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "list_id")
    )
    private List<TaskList> lists = new ArrayList<>();

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @PrePersist
    @PreUpdate
    private void ensureBelongsToList() {
        if (lists.isEmpty()) {
            throw new IllegalStateException("A task must belong to at least one list");
        }
    }
}
