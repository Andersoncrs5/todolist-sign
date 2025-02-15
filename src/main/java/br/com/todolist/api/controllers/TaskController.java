package br.com.todolist.api.controllers;

import br.com.todolist.api.Models.TaskModel;
import br.com.todolist.api.Models.UserModel;
import br.com.todolist.api.Services.TaskService;
import br.com.todolist.api.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService service;

    @GetMapping("/getTask/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id) {
        return this.service.getTask(id);
    }

    @GetMapping("/getAllTask/{id}")
    public ResponseEntity<?> getAllTask(@PathVariable Long id) {
        return this.service.getAllTask(id);
    }

    @PostMapping("/createTask")
    public ResponseEntity<?> createTask(@RequestBody TaskModel task) {
        return this.service.createTask(task);
    }

    @PutMapping("/updateTask")
    public ResponseEntity<?> updateTask(@RequestBody TaskModel task) {
        return this.service.updateTask(task);
    }

    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        return this.service.deleteTask(id);
    }

}
