package br.com.todolist.api.Repositories;

import br.com.todolist.api.Models.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResponsitory extends CrudRepository<UserModel, Long> {
    UserModel findByEmail(String email);
}
