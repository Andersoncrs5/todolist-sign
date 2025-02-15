package br.com.todolist.api.controllers;

import br.com.todolist.api.DTO.LoginDTO;
import br.com.todolist.api.Models.UserModel;
import br.com.todolist.api.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return this.service.getUser(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserModel user) {
        return this.service.createUser(user);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserModel user) {
        return this.service.updateUser(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return this.service.deleteUser(id);
    }

    @PostMapping("/Login")
    public ResponseEntity<?> Login(@RequestBody LoginDTO dto) {
        return this.service.login(dto);
    }

}
