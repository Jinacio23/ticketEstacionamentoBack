package ticketEstacionamento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketEstacionamento.controller.dto.TicketDTO;
import ticketEstacionamento.entity.Estacionamento;
import ticketEstacionamento.entity.Pagamento;
import ticketEstacionamento.entity.Ticket;
import ticketEstacionamento.repository.EstacionamentoRepository;
import ticketEstacionamento.repository.PagamentoRepository;
import ticketEstacionamento.repository.TicketRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EstacionamentoRepository estacionamentoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

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

    //Listagem dos veículos no estacionamento
    public List<Ticket> findByEstacionamentoId(String id){
        Long estacionamentoId = Long.parseLong(id);
        return ticketRepository.findByEstacionamentoId(estacionamentoId);
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

        // token para o ticket
        String token = UUID.randomUUID().toString();

        //Horário de entrada e expiração do ticket
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusMinutes(15);

        try{
            //Criando novo ticket e salvando no banco
            Ticket newTicket = new Ticket();

            newTicket.setQrCodeToken(token);
            newTicket.setHrEntrada(now);
            newTicket.setQrCodeExpiration(expiration);
            newTicket.setValor(0.0);
            newTicket.setEstacionamento(estacionamento);
            newTicket.setPago(false);

            // Salva o ticket primeiro para garantir que o ID seja gerado
            newTicket = ticketRepository.save(newTicket);

            // Gera o conteúdo e o QR Code com o ID do ticket
            String qrContent = String.format("ticket:%s,token:%s", newTicket.getIdTicket(), token);
            String qrCodeBase64 = qrCodeService.generateQRCodeBase64(qrContent, 100, 100);

            newTicket.setQrCodeBase64(qrCodeBase64);

            System.out.println("Base64 lenght - "+ newTicket.getQrCodeBase64().length());

            ticketRepository.save(newTicket); // Atualiza com o QR code

            return new TicketDTO(
                    newTicket.getQrCodeToken(),
                    newTicket.getHrEntrada(),
                    newTicket.getQrCodeBase64(),
                    newTicket.getEstacionamento()
            );


        } catch (Exception e) {
            System.err.println("Erro ao gerar ticket: " + e.getMessage());
            throw new RuntimeException("Falha ao gerar o ticket! " + e.getMessage());
        }

    }

    //Validação do qrcode
    public Ticket validateQrCode(String token) {
        Ticket ticket = ticketRepository.findByQrCodeToken(token)
                .orElseThrow(() -> new RuntimeException("Id do ticket não encontrado!"));

        if(ticket.getHrSaida() != null){
            throw new RuntimeException("Saída já registrada para esse ticket.");
        }

        //Calcula o valor a ser pago pelo tempo de uso do estacionamento

        if (LocalDateTime.now().isAfter(ticket.getQrCodeExpiration())) {
            double taxaHoraria = ticket.getEstacionamento().getTaxa_horaria();
            LocalDateTime saida = LocalDateTime.now();
            Duration timeExpired = Duration.between(ticket.getQrCodeExpiration(), saida);

            double time = (double) timeExpired.toMinutes() / 60;
            double paymentValue = taxaHoraria * time;

            //Arredonda pra 2 casas decimais
            BigDecimal roundedValue = new BigDecimal(paymentValue)
                    .setScale(2, RoundingMode.HALF_UP);

            ticket.setValor(roundedValue.doubleValue());
        } else {
            ticket.setValor(0);
        }

//        if (!"ABERTO".equals(ticket.getStatus())) {
//            throw new RuntimeException("Ticket não está aberto para validação");
//        }

        //Atualizando status e hora da baixa
        ticket.setHrSaida(LocalDateTime.now());
        ticketRepository.save(ticket);

        return ticket;
    }

    public Pagamento pagarTicket(String ticketToken,String formaPagamento){
        Ticket ticket = ticketRepository.findByQrCodeToken(ticketToken)
                .orElseThrow(() -> new RuntimeException("Id do ticket não encontrado!"));

        if (ticket.getHrSaida() == null){
            throw new RuntimeException("Saída ainda não registrada");
        }

        if (ticket.getPago()){
            throw new RuntimeException("O ticket já foi pago");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setDt_pagamento(LocalDateTime.now());
        pagamento.setTipoPagamento(formaPagamento);
        pagamento.setValorPagamento(ticket.getValor());
        pagamento.setTicket(ticket);

        pagamentoRepository.save(pagamento);

        ticket.setPago(true);

        ticketRepository.save(ticket);

        return pagamento;
    }
}
