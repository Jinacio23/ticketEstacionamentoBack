package ticketEstacionamento.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ticketEstacionamento.controller.dto.UsuarioDTO;
import ticketEstacionamento.entity.Usuario;
import ticketEstacionamento.service.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
//@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    //Listar usuario - somente ADMIN
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    //Cadastrar usuario - somente ADMIN
    @Transactional
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> novoUsuario(@RequestBody UsuarioDTO usuariodDTO){
        usuarioService.criarUsuario(usuariodDTO);
        return ResponseEntity.ok().build();
    }

    //Usuario por ID - somente ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Usuario> conseguirPorId(@PathVariable("id") UUID id){
        Optional<Usuario> usuario = usuarioService.usuarioPeloId(id);

        if(usuario.isPresent()){
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Atualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarUsuario(@RequestBody UsuarioDTO userDTO, @PathVariable("id") UUID id){
        usuarioService.atualizarUsuario(id, userDTO);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deletarUsuario(@PathVariable("id") UUID id){
        usuarioService.deletarUsuario(id);
        return ResponseEntity.ok().build();
    }
}
