package ticketEstacionamento.controller.dto;

import ticketEstacionamento.entity.Ticket;

public class ValidationResponse {
    private boolean valid;
    private String message;
    private Ticket ticket;

    public ValidationResponse(boolean valid, String message, Ticket ticket) {
        this.valid = valid;
        this.message = message;
        this.ticket = ticket;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
