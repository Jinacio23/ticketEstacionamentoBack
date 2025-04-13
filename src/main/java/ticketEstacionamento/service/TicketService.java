package ticketEstacionamento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketEstacionamento.dto.TicketDTO;
import ticketEstacionamento.entity.Estacionamento;
import ticketEstacionamento.entity.Ticket;
import ticketEstacionamento.repository.EstacionamentoRepository;
import ticketEstacionamento.repository.TicketRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EstacionamentoRepository estacionamentoRepository;

    @Autowired
    private QrCodeService qrCodeService;

    //Encontra por id
    public Ticket findById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
    }

    //Salva no banco
    public Ticket save(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    //Listagem dos registros
    public List<Ticket> listarTickets() {
        return ticketRepository.findAll();
    }

    //Listagem dos veículos no estacionamento - tickets ativos
    public List<Ticket> activeTickets(String id) {
        Long estacionamentoId = Long.parseLong(id);
        return ticketRepository.findByEstacionamentoIdAndHrSaidaIsNull(estacionamentoId);
    }

    //Gera novo ticket para o usuário
    public TicketDTO generateTicket(String id) {

        Long estacionamentoId = Long.parseLong(id);
        Estacionamento estacionamento = estacionamentoRepository.findById(estacionamentoId)
                .orElseThrow(() -> new RuntimeException("Estacionamento não encontrado"));

        //Gerando token para o ticket
        String token = UUID.randomUUID().toString();

        //Definindo valor padrão
        double ticketValue = 10.0;

        try{
            //Criando novo ticket e salvando no banco
            Ticket newTicket = new Ticket();

            newTicket.setQrCodeToken(token);
            newTicket.setHrEntrada(LocalDateTime.now());
            newTicket.setValor(ticketValue);
            newTicket.setEstacionamento(estacionamento);
            newTicket.setPago(false);

            ticketRepository.save(newTicket);

            // Cria o conteúdo do QR Code
            String qrContent = String.format("ticket:%s,token:%s", newTicket.getIdTicket(), newTicket.getQrCodeToken());
            String qrCode = qrCodeService.generateQRCodeBase64(qrContent, 200,200);

            // Criando DTO de retorno para o front
            return new TicketDTO(
                    newTicket.getQrCodeToken(),
                    newTicket.getHrEntrada(),
                    qrCode,
                    newTicket.getEstacionamento()
            );


        } catch (Exception e) {
            System.err.println("Erro ao gerar ticket: " + e.getMessage());
            throw new RuntimeException("Falha ao gerar o ticket! " + e.getMessage());
        }

    }

    //Validação e baixa do ticket
//    public void baixaTicket(String id){
//        Long idTicket = Long.parseLong(id);
//
//        Optional<Ticket> ticketEntity = ticketRepository.findById(idTicket);
//
//        if(ticketEntity.isPresent()){
//            Ticket ticket = ticketEntity.get();
//
//            ticket.setStatus(false);
//            ticket.setBaixaTicket(Instant.now());
//
//            ticketRepository.save(ticket);
//        } else {
//            throw new RuntimeException("Ticket não encontrado!");
//        }
//    }

    //Validação do qrcode
    public Ticket validateQrCode(String token) {

        Optional<Ticket> ticketRegister = ticketRepository.findByQrCodeToken(token);

        if(!ticketRegister.isPresent()){
            throw new RuntimeException("Id do ticket não encontrado!");
        }

        Ticket ticket = ticketRegister.get();

        if(ticket.getHrSaida() != null){
            throw new RuntimeException("Saída já registrada para esse ticket.");
        }

//        if (LocalDateTime.now().isAfter(ticket.getQrCodeExpiration())) {
//            throw new RuntimeException("QR Code expirado");
//        }

//        if (!"ABERTO".equals(ticket.getStatus())) {
//            throw new RuntimeException("Ticket não está aberto para validação");
//        }

        //Atualizando status e hora da baixa
        ticket.setPago(false);
        ticket.setHrSaida(LocalDateTime.now());
        ticketRepository.save(ticket);

        return ticket;
    }


}
