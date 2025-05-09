package br.com.todolist.api.controllers;

import br.com.todolist.api.DTO.TaskDto;
import br.com.todolist.api.Models.TaskModel;
import br.com.todolist.api.Models.UserModel;
import br.com.todolist.api.Services.TaskService;
import br.com.todolist.api.Services.UserService;
import br.com.todolist.api.config.JwtService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService service;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "bearerAuth")
    public TaskModel getTask(@PathVariable Long id) {
        return this.service.getTask(id);
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getAllTask(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long id = jwtService.extractUserId(token);

        return this.service.getAllTask(id, pageable);
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createTask(@RequestBody @Valid TaskDto task, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long id = jwtService.extractUserId(token);

        return this.service.createTask(task.mappearToTaskModel(), id);
    }

    @PutMapping()
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateTask(@RequestBody TaskDto task) {
        return this.service.updateTask(task.mappearToTaskModel());
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        return this.service.deleteTask(id);
    }

}
