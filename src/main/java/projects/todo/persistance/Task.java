package projects.todo.persistance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Task {
    @Id
    @GeneratedValue // TODO change when ready for production
    private Long id;

    @NotEmpty
    @Column(nullable = false) // Default length is 255
    private String title;

    @Size(max = 255)
    private String description;

    @ColumnDefault("false")
    private boolean completed;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "task_task_list",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "list_id")
    )
    private Set<TaskList> lists = new HashSet<>();

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void addList(TaskList list) {
        lists.add(list);
        list.getTasks().add(this);
    }

    public void removeList(TaskList list) {
        lists.remove(list);
        list.getTasks().remove(this);
    }
}
