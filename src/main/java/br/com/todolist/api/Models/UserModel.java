package br.com.todolist.api.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "user_model")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    @NotNull
    private String name;

    @Column(length = 150, unique = true)
    @NotNull
    private String email;

    @NotNull
    @Column(length = 100)
    private String password;

    @Version
    private Long version;

}
