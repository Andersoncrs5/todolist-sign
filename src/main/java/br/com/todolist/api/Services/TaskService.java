package br.com.todolist.api.Services;

import br.com.todolist.api.Models.TaskModel;
import br.com.todolist.api.Models.UserModel;
import br.com.todolist.api.Repositories.TaskRepository;
import br.com.todolist.api.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Async
    public TaskModel getTask(Long id){
        try {
            if (id == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is required");
            }

            TaskModel task = this.repository.findById(id).orElse(null);

            if(task == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
            }

            return task;
        } catch (Exception e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error internal in server please try again later");
        }
    }

    @Async
    public ResponseEntity<?> getAllTask(Long id, Pageable pageable){
        try {
            Page<TaskModel> task = this.repository.findByFkTaskId(id, pageable);

            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error:\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    @Transactional
    public ResponseEntity<?> createTask(TaskModel task, Long id){
        try {
            UserModel user = this.userRepository.findById(id).orElse(null);

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            task.setDone(false);
            this.repository.save(task);
            return new ResponseEntity<>("Task created with success", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    @Transactional
    public ResponseEntity<?> updateTask(TaskModel task){
        try {
            TaskModel taskFound = this.repository.findById(task.getId()).orElse(null);

            if (taskFound == null) {
                return new ResponseEntity<>("Task no found", HttpStatus.NOT_FOUND);
            }

            taskFound.setTitle(task.getTitle());
            taskFound.setDescription(task.getDescription());

            this.repository.save(taskFound);
            return new ResponseEntity<>("Task updated with success!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    @Transactional
    public ResponseEntity<?> deleteTask(Long id){
        try {
            this.getTask(id);

            this.repository.deleteById(id);

            return new ResponseEntity<>("Task deleted!",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
