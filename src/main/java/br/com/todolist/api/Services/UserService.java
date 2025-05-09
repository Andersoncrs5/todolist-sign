package br.com.todolist.api.Services;

import br.com.todolist.api.DTO.LoginDTO;
import br.com.todolist.api.Models.TaskModel;
import br.com.todolist.api.Models.UserModel;
import br.com.todolist.api.Repositories.UserRepository;
import br.com.todolist.api.Repositories.TaskRepository;
import br.com.todolist.api.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository responsitory;

    @Autowired
    private TaskRepository taskResponsitory;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Async
    @Transactional
    public ResponseEntity<?> createUser(UserModel user){
        try {
            boolean check = this.responsitory.existsByEmail(user.getEmail());
            if (check)
                return new ResponseEntity<>("Email is used", HttpStatus.CONFLICT);

            user.setEmail(user.getEmail().toLowerCase());

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));

            this.responsitory.save(user);
            return new ResponseEntity<>("User created with success!!!",HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error:\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    public UserModel getUser(Long id){
        try {
            UserModel user = this.responsitory.findById(id).orElse(null);

            if (user == null ) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            return user;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error:\n" + e);
        }
    }

    @Async
    @Transactional
    public ResponseEntity<?> deleteUser(Long id){
        try {
            UserModel user = this.getUser(id);

            List<TaskModel> list = this.taskResponsitory.findAllByUserId(user.getId());

            for(TaskModel t : list){
                this.taskResponsitory.deleteById(t.getId());
            }

            this.responsitory.deleteById(user.getId());

            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error:\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    @Transactional
    public ResponseEntity<?> updateUser(UserModel user, Long id){
        try {
            UserModel userFound = this.getUser(id);

            userFound.setName(user.getName());
            userFound.setPassword(passwordEncoder.encode(user.getPassword()));

            this.responsitory.save(userFound);

            return new ResponseEntity<>(userFound, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error:\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    public ResponseEntity<?> login(LoginDTO dto) {
        try {
            Optional<UserModel> userOp = this.responsitory.findByEmail(dto.getEmail());

            if (userOp.isEmpty()) {
                return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
            }

            UserModel user = userOp.get();

            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            String token = jwtService.generateToken((UserDetails) authentication.getPrincipal(), user.getId());

            return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        } catch (Exception e) {
            return new ResponseEntity<>("Error:\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
