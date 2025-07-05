package projects.todo.api.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PaginationApiRequest(
        Integer page,
        Integer size
) {
    public Pageable toPageable() {
        // TODO add sorting
        return PageRequest.of(page, size);
    }
}
