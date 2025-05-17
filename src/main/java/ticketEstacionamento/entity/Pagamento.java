package ticketEstacionamento.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "tb_pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPagamento;

    @Column(name = "valor_pagamento")
    private double valorPagamento;

    @Column(name = "dt_pagamento")
    private LocalDateTime dt_pagamento;

    @Column(name = "tipo_pagamento")
    private String tipoPagamento; //pix, dinheiro, cartao

    @OneToOne
    @JoinColumn(name = "ticket_id")
    @JsonBackReference
    private Ticket ticket;

    public Pagamento() {

    }

    public Pagamento(Long idPagamento, double valorPagamento, LocalDateTime dt_pagamento, String tipoPagamento) {
        this.idPagamento = idPagamento;
        this.valorPagamento = valorPagamento;
        this.dt_pagamento = dt_pagamento;
        this.tipoPagamento = tipoPagamento;
//        this.status = status;
    }

    public Long getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Long idPagamento) {
        this.idPagamento = idPagamento;
    }

    public double getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(double valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public LocalDateTime getDt_pagamento() {
        return dt_pagamento;
    }

    public void setDt_pagamento(LocalDateTime dt_pagamento) {
        this.dt_pagamento = dt_pagamento;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    //    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
}
