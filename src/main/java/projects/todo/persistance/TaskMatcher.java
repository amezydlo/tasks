package projects.todo.persistance;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TaskMatcher {

    private final Specification<Task> spec;

    public Specification<Task> asSpecification() {
        return spec;
    }


    public static class Builder {
        private final List<Specification<Task>> specs = new ArrayList<>();

        public Builder withTitle(String title) {
            if (title != null) {
                specs.add((root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            return this;
        }

        public Builder withCompleted(Boolean completed) {
            if (completed != null) {
                specs.add(((root, query, cb) -> cb.isTrue(root.get("completed"))));
            }
            return this;
        }

        public TaskMatcher build() {
            Specification<Task> spec = specs.stream().reduce((root, query, cb) -> cb.conjunction(), Specification::and);
            return new TaskMatcher(spec);
        }
    }
}
