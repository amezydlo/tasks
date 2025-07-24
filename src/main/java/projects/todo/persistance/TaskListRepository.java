package projects.todo.persistance;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    @EntityGraph(attributePaths = "tasks") // probably could be optimized by jpql - I don't think we need 2 joins...
    Set<TaskList> findAllByIdIn(Set<Long> listIds);


    @Query("""
    SELECT DISTINCT l FROM TaskList l
    LEFT JOIN FETCH l.tasks t
    LEFT JOIN FETCH t.lists
    WHERE l.id = :listId
""")
    Optional<TaskList> findByIdWithTasksAndLists(@Param("listId") Long listId);

}
