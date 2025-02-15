package br.com.todolist.api.Services;

import br.com.todolist.api.DTO.LoginDTO;
import br.com.todolist.api.Models.TaskModel;
import br.com.todolist.api.Models.UserModel;
import br.com.todolist.api.Repositories.UserResponsitory;
import br.com.todolist.api.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserResponsitory responsitory;

    @Autowired
    private TaskRepository taskResponsitory;

    @Async
    @Transactional
    public ResponseEntity<?> createUser(UserModel user){
        try {
            boolean check = this.validationUser(user);

            if (!check) {
                return new ResponseEntity<>("Datas are required", HttpStatus.BAD_REQUEST);
            }

            if (user.getId() != null) {
                return new ResponseEntity<>("ID must not be provided for creation", HttpStatus.BAD_REQUEST);
            }

            user.setEmail(user.getEmail().toLowerCase());
            System.out.println(user.toString());

            this.responsitory.save(user);
            return new ResponseEntity<>(user,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error:\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getUser(Long id){
        try {
            if (id == 0) {
                return new ResponseEntity<>("Id is required",HttpStatus.BAD_REQUEST);
            }

            UserModel user = this.responsitory.findById(id).orElse(null);

            if (user == null ) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(user, HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error:\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteUser(Long id){
        try {
            if (id == 0) {
                return new ResponseEntity<>("Id is required",HttpStatus.BAD_REQUEST);
            }

            UserModel user = this.responsitory.findById(id).orElse(null);

            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            List<TaskModel> list = this.taskResponsitory.findByFkTaskId(user.getId());

            for(TaskModel t : list){
                this.taskResponsitory.deleteById(t.getId());
            }

            this.responsitory.deleteById(user.getId());

            return new ResponseEntity<>(user, HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error:\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    @Transactional
    public ResponseEntity<?> updateUser(UserModel user){
        try {
            boolean check = this.validationUser(user);
            if (user.getId() == 0) {
                return new ResponseEntity<>("Id is required",HttpStatus.BAD_REQUEST);
            }

            if (!check) {
                return new ResponseEntity<>("Datas are required", HttpStatus.BAD_REQUEST);
            }

            UserModel userFound = this.responsitory.findById(user.getId()).orElse(null);

            if (userFound == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            userFound.setName(user.getName());
            userFound.setPassword(user.getPassword());

            this.responsitory.save(userFound);

            return new ResponseEntity<>(userFound, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error:\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    public ResponseEntity<?> login(LoginDTO dto){
        try {
            UserModel user = this.responsitory.findByEmail(dto.email());

            if (user == null){
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }

            if (!Objects.equals(user.getPassword(), dto.password())){
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error:\n" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validationUser(UserModel user){
        try {

            if (user.getEmail().isEmpty()) {
                return false;
            }

            if (user.getName().isEmpty()) {
                return false;
            }

            if (user.getPassword().isEmpty()) {
                return false;
            }

            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }


}
