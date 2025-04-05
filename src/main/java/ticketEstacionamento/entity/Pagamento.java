package ticketEstacionamento.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPagamento;

    @Column(name = "valor_pagamento")
    private BigDecimal valorPagamento;

    @Column(name = "dt_pagamento")
    private LocalDateTime dt_pagamento;

    @Column(name = "tipo_pagamento")
    private String tipoPagamento; //pix, dinheiro, cartao

    @Column(name = "status")
    private String status; //pago, pendente, cancelado

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public Pagamento() {

    }

    public Pagamento(Long idPagamento, BigDecimal valorPagamento, LocalDateTime dt_pagamento, String tipoPagamento, String status) {
        this.idPagamento = idPagamento;
        this.valorPagamento = valorPagamento;
        this.dt_pagamento = dt_pagamento;
        this.tipoPagamento = tipoPagamento;
        this.status = status;
    }

    public Long getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Long idPagamento) {
        this.idPagamento = idPagamento;
    }

    public BigDecimal getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(BigDecimal valorPagamento) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
