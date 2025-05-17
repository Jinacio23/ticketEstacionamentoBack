package ticketEstacionamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketEstacionamento.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByNome(String nome);
}
