package br.com.todolist.api.Repositories;

import br.com.todolist.api.Models.TaskModel;
import br.com.todolist.api.Models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<TaskModel, Long> {

    @Query(value = "SELECT * FROM task_model t where t.USER_ID = :id ", nativeQuery = true )
    Page<TaskModel> findByFkTaskId(Long id, Pageable pageable);

    @Query(value = "SELECT * FROM user_model u where u.id = :id ", nativeQuery = true )
    UserModel findUser(Long id);

    List<TaskModel> findAllByUserId(Long id);
}
