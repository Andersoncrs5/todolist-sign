package br.com.todolist.api.Services;

import br.com.todolist.api.Models.TaskModel;
import br.com.todolist.api.Models.UserModel;
import br.com.todolist.api.Repositories.TaskRepository;
import br.com.todolist.api.Repositories.UserResponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private UserResponsitory userResponsitory;

    @Async
    public ResponseEntity<?> getTask(Long id){
        try {

            if (id == 0) {
                return new ResponseEntity<>("Id is required",HttpStatus.BAD_REQUEST);
            }

            TaskModel task = this.repository.findById(id).orElse(null);

            if(task == null) {
                return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(task, HttpStatus.FOUND);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    public ResponseEntity<?> getAllTask(Long id){
        try {
            if (id == 0) {
                return new ResponseEntity<>("Id is required",HttpStatus.BAD_REQUEST);
            }

            boolean check = this.userResponsitory.existsById(id);

            if (check == false){
                return new ResponseEntity<>("User not exists",HttpStatus.BAD_REQUEST);
            }

            List<TaskModel> task = this.repository.findByFkTaskId(id);

            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Error:\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    @Transactional
    public ResponseEntity<?> createTask(TaskModel task){
        try {
            System.out.println(task.toString());
            boolean check = this.validationTask(task);

            if (!check) {
                return new ResponseEntity<>("Datas are required", HttpStatus.BAD_REQUEST);
            }

            UserModel user = this.repository.findUser(task.getFk_task_id());

            if (user == null){
                return new ResponseEntity<>("Not found user with id :" + task.getFk_task_id(), HttpStatus.NOT_FOUND);
            }

            task.setDone(false);
            System.out.println(task.toString());
            this.repository.save(task);
            return new ResponseEntity<>(task, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    @Transactional
    public ResponseEntity<?> updateTask(TaskModel task){
        try {
            boolean check = this.validationTask(task);

            if (!check) {
                return new ResponseEntity<>("Datas are required", HttpStatus.BAD_REQUEST);
            }

            if (task.getId() == 0) {
                return new ResponseEntity<>("Id is required",HttpStatus.BAD_REQUEST);
            }

            TaskModel taskFound = this.repository.findById(task.getId()).orElse(null);

            if (taskFound == null) {
                return new ResponseEntity<>("Task no found", HttpStatus.NOT_FOUND);
            }

            taskFound.setTitle(task.getTitle());
            taskFound.setDescription(task.getDescription());

            System.out.println(taskFound.toString());
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
            if (id == 0) {
                return new ResponseEntity<>("Id is required",HttpStatus.BAD_REQUEST);
            }

            this.repository.deleteById(id);

            return new ResponseEntity<>("Task deleted!",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validationTask(TaskModel task){
        try {

            if (task.getTitle().isEmpty()) {
                return false;
            }

            if (task.getDescription().isEmpty()) {
                return false;
            }

            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

}
