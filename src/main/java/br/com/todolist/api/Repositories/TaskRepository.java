package br.com.todolist.api.Repositories;

import br.com.todolist.api.Models.TaskModel;
import br.com.todolist.api.Models.UserModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<TaskModel, Long> {

    @Query(value = "SELECT * FROM task_model t where t.FK_TASK_ID = :id ", nativeQuery = true )
    List<TaskModel> findByFkTaskId(Long id);

    @Query(value = "SELECT * FROM user_model u where u.id = :id ", nativeQuery = true )
    UserModel findUser(Long id);

}
