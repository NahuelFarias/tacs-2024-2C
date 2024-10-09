package tacs.controllers;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tacs.dto.CreateUser;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.NormalUser;
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
        userService.createUser(createUser.getUsername(), createUser.getPassword());
    }

    @GetMapping 
    @ResponseBody
    public List<NormalUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}") 
    @ResponseBody
    public NormalUser getDataUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @GetMapping("/{id}/reserves") 
    @ResponseBody
    public List<Ticket> getReserves(@PathVariable String id) {
        return userService.getReservations(id);
    }
}
