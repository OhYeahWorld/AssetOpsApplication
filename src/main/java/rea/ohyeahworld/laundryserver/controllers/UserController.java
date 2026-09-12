package rea.ohyeahworld.laundryserver.controllers;

import org.springframework.web.bind.annotation.*;
import rea.ohyeahworld.laundryserver.entities.Users;
import rea.ohyeahworld.laundryserver.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public Users addNewUser() {
        return userService.addNewUser();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUserById(id);
    }
}
