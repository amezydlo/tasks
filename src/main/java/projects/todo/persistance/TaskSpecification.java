package projects.todo.persistance;

import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import projects.todo.api.filter.TaskFilter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskSpecification implements Specification<Task> {

    private final Specification<Task> delegate;

    public TaskSpecification(TaskFilter filter) {
        this.delegate = builder().withTitle(filter.title()).withCompleted(filter.completed()).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Predicate toPredicate(@Nonnull Root<Task> root, CriteriaQuery<?> query, @Nonnull CriteriaBuilder criteriaBuilder) {
        return delegate.toPredicate(root, query, criteriaBuilder);
    }

    public static class Builder {
        private final List<Specification<Task>> specs = new ArrayList<>();

        public Builder withTitle(String title) {
            if (title != null && !title.isEmpty()) {
                specs.add((root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            return this;
        }

        public Builder withCompleted(Boolean completed) {
            if (completed != null) {
                if (completed) {
                    specs.add((root, query, cb) -> cb.isTrue(root.get("completed")));
                } else {
                    specs.add((root, query, cb) -> cb.isFalse(root.get("completed")));
                }

            }
            return this;
        }

        public TaskSpecification build() {
            var combinedSpecs = specs.stream()
                    .reduce((root, query, cb) -> cb.conjunction(), Specification::and);
            return new TaskSpecification(combinedSpecs);


        }
    }

}
