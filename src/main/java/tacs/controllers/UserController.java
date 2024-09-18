package tacs.controllers;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tacs.dto.CreateUser;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.User;
import tacs.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    @PostMapping
    public void createUser(@RequestBody CreateUser createUser) {
        userService.createUser(createUser.getName());
    }

    @GetMapping 
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}") 
    @ResponseBody
    public User getDataUser(@PathVariable Integer id) {
        return userService.getUsers(id);
    }

    @GetMapping("/{id}/reserves") 
    @ResponseBody
    public List<Ticket> getReserves(@PathVariable Integer id) {
        return userService.getReservations(id);
    }
}
