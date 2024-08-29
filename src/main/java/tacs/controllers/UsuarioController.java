package tacs.controllers;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tacs.models.domain.users.Usuario;
import tacs.models.domain.users.UsuarioService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    
    @PostMapping
    public void createUsuario(@RequestBody Usuario usuario) {
        usuarioService.createUsuario(usuario);
    }

    @GetMapping("/{id}") 
    @ResponseBody
    public Optional<Usuario> getUser(@PathVariable Integer id) {
        return usuarioService.getUsuario(id);
    }

    @GetMapping("/{id}/reserves") 
    @ResponseBody
    public String getReserves(@PathVariable Integer id) {
        //TODO: Definir logica en el service
        return "hola" + id;
    }

    @GetMapping("/all") 
    @ResponseBody
    public List<Usuario> getAllUsers() {
        return usuarioService.getAllUsuarios();
    }

}
