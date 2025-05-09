package br.com.todolist.api.Services;

import br.com.todolist.api.Models.UserModel;
import br.com.todolist.api.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> optionalUser = userRepository.findByEmail(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        UserModel user = optionalUser.get();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                true, true, true, true,
                List.of()
        );
    }
}
