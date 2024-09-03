package tacs.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tacs.dto.CrearUsuario;
import tacs.models.domain.events.Ticket;
import tacs.models.domain.users.Usuario;
import tacs.service.UsuarioService;

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
public class UsuarioController {

    //private final UsuarioService usuarioService;
    /*
    @PostMapping
    public void createUsuario(@RequestBody CrearUsuario createUser) {
        usuarioService.createUsuario(createUser.getName());
    }

    @GetMapping 
    @ResponseBody
    public List<Usuario> getAllUsers() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}") 
    @ResponseBody
    public Usuario getDataUser(@PathVariable Integer id) {
        return usuarioService.getUsuario(id);
    }

    @GetMapping("/{id}/reserves") 
    @ResponseBody
    public String getReserves(@PathVariable Integer id) {
        //TODO: Definir logica en el service
        return "hola" + id;
    }*/
    public List<Ticket> getReserves(@PathVariable Integer id) {
        return usuarioService.getReserves(id);
    }
}
