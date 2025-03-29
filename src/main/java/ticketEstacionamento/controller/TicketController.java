package ticketEstacionamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ticketEstacionamento.dto.QrValidationRequest;
import ticketEstacionamento.dto.TicketDTO;
import ticketEstacionamento.dto.ValidationResponse;
import ticketEstacionamento.entity.Ticket;
import ticketEstacionamento.service.QrCodeService;
import ticketEstacionamento.service.TicketService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @Autowired
    QrCodeService qrCodeService;

    //Emissão e baixa de tickets

    @GetMapping
    public ResponseEntity<List<Ticket>> getTickets() {
        //Recuperar todos os tickets
        var tickets = ticketService.listarTickets();

        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/generate")
    public ResponseEntity<Ticket> generateTicket(){
        Ticket ticket = ticketService.createTicket();

        return ResponseEntity.ok(ticket);
    }

    /*
    @PutMapping("/{id}")
    public ResponseEntity<Void> baixaTicket(@PathVariable("id") String id){
        // Faz a baixa do ticket
        ticketService.baixaTicket(id);
        return ResponseEntity.noContent().build();
    }
     */

    //Atualização de token e validação de QrCode

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<byte[]> generateQRCode(@PathVariable Long id) throws Exception {
        try{
        byte[] qrCode = qrCodeService.generateTicketQrCode(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCode);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }

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

