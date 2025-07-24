package projects.todo.persistance;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @Override
    @EntityGraph(attributePaths = {"lists", "lists.tasks"})
    @Nonnull
    Optional<Task> findById(@Nonnull Long id);

    @Query(value = """
            SELECT t.*
            FROM task t
            JOIN task_task_list tlt ON t.id = tlt.task_id
            GROUP BY t.id
            HAVING COUNT(*) = 1
               AND BOOL_AND(tlt.list_id = :listId)
            """, nativeQuery = true)
    Set<Task> findTasksToBeAbandoned(@Param("listId") Long listId);


}
