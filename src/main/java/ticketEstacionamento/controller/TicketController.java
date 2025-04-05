package ticketEstacionamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
        var tickets = ticketService.listarTickets();

        return ResponseEntity.ok(tickets);
    }

    //Gera um novo ticket e retorna o DTO
    @PostMapping("/generate")
    public ResponseEntity<TicketDTO> generateTicket(){
        TicketDTO ticket = ticketService.generateTicket();

        return ResponseEntity.ok(ticket);
    }

    //Atualização de token e validação de QrCode
    @PostMapping("/validate")
    public ResponseEntity<?> validateTicket(@RequestBody QrValidationRequest request) {
        try {
            Ticket ticket = ticketService.validateQrCode(request.getTicketId(), request.getToken());
            return ResponseEntity.ok(new ValidationResponse(true, "Ticket válido", ticket));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ValidationResponse(false, e.getMessage(), null));
        }
    }

}

