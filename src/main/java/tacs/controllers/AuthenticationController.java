package tacs.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tacs.dto.JWT;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthenticationController {
    @PostMapping("")
    @ResponseBody
    public JWT authenticate() {
        return new JWT("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiZXhhbXBsZS1qd3QifQ.JYF_kuzaikLAIQbRCNe_wA-B7yZZKYp0jOwBUazOFKM");
    }
}
