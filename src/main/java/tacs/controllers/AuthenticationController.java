package tacs.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tacs.dto.JWT;
import tacs.dto.LoginRequest;
import tacs.service.AutenticationService;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AutenticationService authService;

    @PostMapping("")
    @ResponseBody
    public JWT authenticate(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/salt")
    @ResponseBody
    public String getSalt(@RequestParam String username) {
        return authService.getSalt(username);
    }

}
