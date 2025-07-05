package projects.todo.api.response;

import java.util.List;

public class PaginationApiResponse<T>{
    List<T> results;
    Pagination pagination;
}
