package ticketEstacionamento.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    @Column(name = "status")
    private Boolean status;

    //@Column(name = "qtd_ticket")
    //private Long qtdTicket;

    @Column(name = "qr_code_token", unique = true)
    private String qrCodeToken;

    @Column(name = "qr_code_expiration")
    private LocalDateTime qrCodeExpiration;

    @CreationTimestamp
    private Instant horaEmissao;
    @UpdateTimestamp
    private Instant baixaTicket;

    public Ticket(){

    }

    public Ticket(Long idTicket, Boolean status, Instant horaEmissao, Instant baixaTicket) {
        this.idTicket = idTicket;
        this.status = status;
        this.horaEmissao = horaEmissao;
        this.baixaTicket = baixaTicket;
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
/*
    public Long getQtdTicket() {
        return qtdTicket;
    }

    public void setQtdTicket(Long qtdTicket) {
        this.qtdTicket = qtdTicket;
    }
*/
    public Instant getHoraEmissao() {
        return horaEmissao;
    }

    public void setHoraEmissao(Instant horaEmissao) {
        this.horaEmissao = horaEmissao;
    }

    public Instant getBaixaTicket() {
        return baixaTicket;
    }

    public void setBaixaTicket(Instant baixaTicket) {
        this.baixaTicket = baixaTicket;
    }

    public LocalDateTime getQrCodeExpiration() {
        return qrCodeExpiration;
    }

    public void setQrCodeExpiration(LocalDateTime qrCodeExpiration) {
        this.qrCodeExpiration = qrCodeExpiration;
    }

    public String getQrCodeToken() {
        return qrCodeToken;
    }

    public void setQrCodeToken(String qrCodeToken) {
        this.qrCodeToken = qrCodeToken;
    }
}

