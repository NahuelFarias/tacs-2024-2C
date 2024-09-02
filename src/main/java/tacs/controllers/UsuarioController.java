package tacs.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsuarioController {

    //private final UsuarioService usuarioService;
    /*
    @PostMapping
    public void createUsuario(@RequestBody Usuario usuario) {
        usuarioService.createUsuario(usuario);
    }

    @GetMapping 
    @ResponseBody
    public List<Usuario> getAllUsers() {
        return usuarioService.getAllUsuarios();
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
    }*/
}
