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

    @Column(name = "pago")
    private Boolean pago = false;

//    @Column(name = "qtd_ticket")
//    private Long qtdTicket;

    @Column(name = "valor")
    private double valor;

    @Column(name = "qr_code_token", unique = true)
    private String qrCodeToken;

//    @Column(name = "qr_code_expiration")
//    private LocalDateTime qrCodeExpiration;

    private Instant hr_entrada;
    private Instant hr_saida;

    @ManyToOne
    @JoinColumn(name = "estacionamento_id")
    private Estacionamento estacionamento;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Pagamento pagamento;

    public Ticket(){

    }

    public Ticket(Long idTicket, Boolean pago, double valor, String qrCodeToken, Instant hr_entrada, Instant hr_saida) {
        this.idTicket = idTicket;
        this.pago = pago;
        this.valor = valor;
        this.qrCodeToken = qrCodeToken;
        this.hr_entrada = hr_entrada;
        this.hr_saida = hr_saida;
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getQrCodeToken() {
        return qrCodeToken;
    }

    public void setQrCodeToken(String qrCodeToken) {
        this.qrCodeToken = qrCodeToken;
    }

    public Instant getHr_entrada() {
        return hr_entrada;
    }

    public void setHr_entrada(Instant hr_entrada) {
        this.hr_entrada = hr_entrada;
    }

    public Instant getHr_saida() {
        return hr_saida;
    }

    public void setHr_saida(Instant hr_saida) {
        this.hr_saida = hr_saida;
    }
}

