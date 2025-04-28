package ticketEstacionamento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketEstacionamento.dto.TicketDTO;
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

        //Gerando token para o ticket
        String token = UUID.randomUUID().toString();

        //Definindo valor padrão
        double ticketValue = 0.0;

        try{
            //Criando novo ticket e salvando no banco
            Ticket newTicket = new Ticket();

            newTicket.setQrCodeToken(token);
            newTicket.setHrEntrada(LocalDateTime.now());
            newTicket.setQrCodeExpiration(LocalDateTime.now().plusMinutes(15));
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

            System.out.println("paymentValue: "+ paymentValue);

            //Arredonda pra 2 casas decimais
            BigDecimal roundedValue = new BigDecimal(paymentValue)
                    .setScale(2, RoundingMode.HALF_UP);

            System.out.println("RoundedValue: "+ roundedValue);
            System.out.println("RoundedValue: "+ roundedValue.doubleValue());

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
