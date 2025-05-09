package br.com.todolist.api.controllers;

import br.com.todolist.api.DTO.LoginDTO;
import br.com.todolist.api.DTO.UserDto;
import br.com.todolist.api.Models.UserModel;
import br.com.todolist.api.Services.UserService;
import br.com.todolist.api.config.JwtService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody @Valid LoginDTO dto) {
        return this.service.login(dto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDto dto) {
        return this.service.createUser(dto.mappearToUserModel());
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Valid UserDto dto, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long id = jwtService.extractUserId(token);

        return this.service.updateUser(dto.mappearToUserModel(), id);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long id = jwtService.extractUserId(token);
        return this.service.deleteUser(id);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserModel get(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long id = jwtService.extractUserId(token);

        return this.service.getUser(id);
    }


}
