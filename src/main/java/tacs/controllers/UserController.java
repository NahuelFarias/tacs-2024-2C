package tacs.controllers;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tacs.dto.CreateUser;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.NormalUser;
import tacs.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class UserController {

    private final UserService userService;
    
    @PostMapping
    public void createUser(@RequestBody CreateUser createUser) {
        userService.createUser(createUser.getUsername(), createUser.getPassword(), createUser.getEmail());
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

    @PutMapping("/reset-password")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Reset user password by username", description = "Requires ADMIN role")
    public long resetPassword(@RequestParam String username, @RequestParam String newPassword) {
        return userService.resetPassword(username, newPassword);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        try {
            NormalUser user = userService.getUserByUsername(username);
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("id", user.getId());
            
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body(Collections.singletonMap("error", e.getReason()));
        }
    }
}
