package ticketEstacionamento.controller.dto;

public record LoginResponse(String accessToken, String refreshToken, Long expiresIn) {
}
