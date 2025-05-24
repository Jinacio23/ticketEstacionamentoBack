package ticketEstacionamento.config;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ticketEstacionamento.entity.Role;
import ticketEstacionamento.entity.Usuario;
import ticketEstacionamento.repository.RoleRepository;
import ticketEstacionamento.repository.UsuarioRepository;

import java.util.Optional;
import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Role roleAdmin = roleRepository.findByNome(Role.Values.ADMIN.name());

        Optional<Usuario> usuarioAdmin = usuarioRepository.findByNome("admin");

        if (usuarioAdmin.isPresent()) {
            System.out.println("Iniciado com sucesso!");
        } else {
            Usuario usuario = new Usuario();
            usuario.setNome("admin");
            usuario.setSenha(passwordEncoder.encode("12345"));
            usuario.setRoles(Set.of(roleAdmin));
            usuarioRepository.save(usuario);
            System.out.println("Iniciado com sucesso!");
            System.out.println("Admin criado com sucesso\n username: admin \n senha:12345");
        }
    }
}
