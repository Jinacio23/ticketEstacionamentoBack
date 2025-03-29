package ticketEstacionamento.service;

import org.springframework.stereotype.Service;
import ticketEstacionamento.dto.TicketDTO;
import ticketEstacionamento.entity.Ticket;
import ticketEstacionamento.repository.TicketRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

    public Ticket findById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
    }

    public Ticket save(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    public List<Ticket> listarTickets() {
        return ticketRepository.findAll();
    }

    public Ticket createTicket(){
        Ticket newTicket = new Ticket(
                null,
                true,
                Instant.now(),
                null
        );

        return ticketRepository.save(newTicket);
    }

    public void baixaTicket(String id){
        Long idTicket = Long.parseLong(id);

        Optional<Ticket> ticketEntity = ticketRepository.findById(idTicket);

        if(ticketEntity.isPresent()){
            Ticket ticket = ticketEntity.get();

            ticket.setStatus(false);
            ticket.setBaixaTicket(Instant.now());

            ticketRepository.save(ticket);
        } else {
            throw new RuntimeException("Ticket não encontrado!");
        }
    }

    public Ticket validateQrCode(Long ticketId, String token) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));

        if (!token.equals(ticket.getQrCodeToken())) {
            throw new RuntimeException("Token inválido");
        }

        if (LocalDateTime.now().isAfter(ticket.getQrCodeExpiration())) {
            throw new RuntimeException("QR Code expirado");
        }

//        if (!"ABERTO".equals(ticket.getStatus())) {
//            throw new RuntimeException("Ticket não está aberto para validação");
//        }

        ticket.setStatus(false);

        ticketRepository.save(ticket);

        return ticket;
    }
}
