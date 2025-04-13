package ticketEstacionamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketEstacionamento.entity.Ticket;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEstacionamentoIdAndHrSaidaIsNull(Long estacionamentoId);

    Optional<Ticket> findByQrCodeToken(String token);
}
