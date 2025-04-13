package ticketEstacionamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketEstacionamento.dto.QrValidationRequest;
import ticketEstacionamento.dto.TicketDTO;
import ticketEstacionamento.dto.ValidationResponse;
import ticketEstacionamento.entity.Ticket;
import ticketEstacionamento.service.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    TicketService ticketService;

    //Emissão e baixa de tickets

    //Listagem dos tickets
    @GetMapping
    public ResponseEntity<List<Ticket>> getTickets() {
        //Recuperar todos os tickets
        List<Ticket> tickets = ticketService.listarTickets();

        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{estacionamentoId}/ativos")
    public ResponseEntity<List<Ticket>> activedTickets(@PathVariable("estacionamentoId") String id){
        List<Ticket> activeTickets = ticketService.activeTickets(id);

        return ResponseEntity.ok(activeTickets);
    }

    //Gera um novo ticket e retorna o DTO
    @PostMapping("/{estacionamentoId}/generate")
    public ResponseEntity<TicketDTO> generateTicket(@PathVariable("estacionamentoId") String id){
        TicketDTO ticket = ticketService.generateTicket(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    //Atualização de token e validação de QrCode
    @PutMapping("/{ticketToken}/validate")
    public ResponseEntity<?> validateTicket(@PathVariable("ticketToken") String token) {
        try {
            Ticket ticket = ticketService.validateQrCode(token);
            return ResponseEntity.ok(new ValidationResponse(true, "Ticket válido", ticket));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ValidationResponse(false, e.getMessage(), null));
        }
    }

}

