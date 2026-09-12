package rea.ohyeahworld.laundryserver.services;

import org.springframework.stereotype.Service;
import rea.ohyeahworld.laundryserver.entities.Users;
import rea.ohyeahworld.laundryserver.repos.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<Users> getAllUsers() {
        return repository.findAll();
    }

    public Users addNewUser() {
        return repository.save(new Users());
    }

    public void deleteUserById(String id) {
        repository.deleteById(id);
    }
}
