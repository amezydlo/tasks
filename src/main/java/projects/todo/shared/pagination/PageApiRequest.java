package projects.todo.shared.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import projects.todo.shared.sorting.SortRequest;

import java.util.Optional;

/*
 *
 * Used in requests
 *
 * */
public record PageApiRequest(
        Integer page,
        Integer size
) {
    public <T extends Enum<T>> Pageable toPageable(Optional<SortRequest<T>> sortRequest) {
        var sortBy = sortRequest.map(sort -> Sort.by(
                Sort.Direction.valueOf(sort.getDirection().name()),
                sort.getSortBy().name())).orElse(Sort.by(Sort.Direction.ASC, "id"));

        return PageRequest.of(page, size, sortBy);
    }
}
