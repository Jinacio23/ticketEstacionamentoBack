package ticketEstacionamento.dto;

import ticketEstacionamento.entity.Estacionamento;

import java.time.LocalDateTime;

//Status -> Identifica se o ticket está ativo ou não
//value -> Valor padrão do ticket
//qrCode -> qrCode em base64 para conversão no front
public record TicketDTO (String token, LocalDateTime hr_entrada, String qrCode, Estacionamento estacionamento){
}
