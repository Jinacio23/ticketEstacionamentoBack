package ticketEstacionamento.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPagamento;

    @Column(name = "tipo_pagamento")
    private Double tipoPagamento;

    @Column(name = "valor_pagamento")
    private Double valorPagamento;

    @Column(name = "total_pagamento")
    private Double totalPagamento;

    public Pagamento() {

    }

    public Pagamento(Long idPagamento, Double tipoPagamento, Double valorPagamento, Double totalPagamento) {
        this.idPagamento = idPagamento;
        this.tipoPagamento = tipoPagamento;
        this.valorPagamento = valorPagamento;
        this.totalPagamento = totalPagamento;
    }

    public Long getIdPagamento() {
        return idPagamento;
    }

    public Double getTotalPagamento() {
        return totalPagamento;
    }

    public void setTotalPagamento(Double totalPagamento) {
        this.totalPagamento = totalPagamento;
    }

    public Double getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(Double valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public Double getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(Double tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

}
