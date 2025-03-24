package ticketEstacionamento.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tb_ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "qtd_ticket")
    private Long qtdTicket;

    @CreationTimestamp
    private Instant horaEmissao;
    @UpdateTimestamp
    private Instant baixaTicket;

    public Long getIdTicket() {
        return idTicket;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getQtdTicket() {
        return qtdTicket;
    }

    public void setQtdTicket(Long qtdTicket) {
        this.qtdTicket = qtdTicket;
    }

    public Instant getHoraEmissao() {
        return horaEmissao;
    }

    public Instant getBaixaTicket() {
        return baixaTicket;
    }

    public Ticket() {

    }
}

