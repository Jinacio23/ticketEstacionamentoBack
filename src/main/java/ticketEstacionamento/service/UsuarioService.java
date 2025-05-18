package ticketEstacionamento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ticketEstacionamento.controller.dto.UsuarioDTO;
import ticketEstacionamento.entity.Estacionamento;
import ticketEstacionamento.entity.Role;
import ticketEstacionamento.entity.Usuario;
import ticketEstacionamento.repository.EstacionamentoRepository;
import ticketEstacionamento.repository.RoleRepository;
import ticketEstacionamento.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstacionamentoRepository estacionamentoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void criarUsuario(UsuarioDTO usuariodDTO) {
        Role managerRole = roleRepository.findByNome(Role.Values.MANAGER.name());

        Optional<Usuario> usuarioDB = usuarioRepository.findByNome(usuariodDTO.nome());
        Optional<Estacionamento> estacionamento = Optional.ofNullable(estacionamentoRepository.findById(usuariodDTO.estacionamentoId())
                .orElseThrow(() -> new RuntimeException("Estacionamento não encontrado!")));

        if (usuarioDB.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuariodDTO.nome());
        usuario.setSenha(passwordEncoder.encode(usuariodDTO.senha()));
        usuario.setRoles(Set.of(managerRole));
        usuario.setEstacionamento(estacionamento.get());

        usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> usuarioPeloId(UUID id) {
        return usuarioRepository.findById(id);
    }

    public void atualizarUsuario(UUID id, UsuarioDTO userDTO) {

        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

        if(usuarioExistente.isPresent()){
            Usuario usuario = usuarioExistente.get();

            if (!userDTO.nome().equals(usuario.getNome())){
                usuario.setNome(userDTO.nome());
            }
            if (!passwordEncoder.matches(userDTO.senha(), usuario.getSenha())){
                usuario.setSenha(passwordEncoder.encode(userDTO.senha()));
            }

            usuarioRepository.save(usuario);
        }

    }

    public void deletarUsuario(UUID id) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

        if(usuarioExistente.isPresent()){
            Usuario usuario = usuarioExistente.get();

            usuario.getRoles().clear();
            usuarioRepository.save(usuario);
            usuarioRepository.delete(usuario);
        }
    }
}
