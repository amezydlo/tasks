package projects.todo.shared.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/*
*
* Used in requests
*
* */
public record PageApiRequest(
        Integer page,
        Integer size
) {
    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
