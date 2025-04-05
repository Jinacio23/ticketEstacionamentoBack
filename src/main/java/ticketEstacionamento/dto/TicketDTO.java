package ticketEstacionamento.dto;

//Status -> Identifica se o ticket está ativo ou não
//value -> Valor padrão do ticket
//qrCode -> qrCode em base64 para conversão no front
public record TicketDTO (Boolean status, double value, String qrCode){
}
