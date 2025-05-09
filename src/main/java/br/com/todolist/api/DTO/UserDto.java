package br.com.todolist.api.DTO;

import br.com.todolist.api.Models.UserModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UserDto {

    @NotBlank
    @Max(50)
    @Min(3)
    private String name;

    @Max(150)
    @Min(8)
    @NotBlank
    @Email
    private String email;

    @Max(50)
    @Min(8)
    @NotBlank
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserModel mappearToUserModel(){
        UserModel user = new UserModel();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }
}
