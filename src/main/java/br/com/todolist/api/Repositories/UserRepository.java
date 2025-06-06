package br.com.todolist.api.Repositories;

import br.com.todolist.api.Models.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);
}
