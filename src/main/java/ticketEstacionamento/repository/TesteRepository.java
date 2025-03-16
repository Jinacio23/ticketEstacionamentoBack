package ticketEstacionamento.repository;

import org.springframework.stereotype.Repository;
import ticketEstacionamento.entity.Teste;
import org.springframework.data.jpa.repository.JpaRepository;

//Interface do JPA para a entidade Teste -> representação do nosso database para tb_teste
@Repository
public interface TesteRepository extends JpaRepository<Teste, Long> {
}
