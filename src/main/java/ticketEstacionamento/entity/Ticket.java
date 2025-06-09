package ticketEstacionamento.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Boolean pago;

    @Column(name = "valor")
    private double valor;

    @Column(name = "qr_code_token", unique = true)
    private String qrCodeToken;

    @Column(name = "qr_code_expiration")
    private LocalDateTime qrCodeExpiration;

    @Column(name = "qr_code_base64", length = 500)
    private String qrCodeBase64;

    private LocalDateTime hrEntrada;
    private LocalDateTime hrSaida;

    @ManyToOne
    @JoinColumn(name = "estacionamento_id")
//    @JsonBackReference(value = "estacionamento-tickets")
    @JsonIgnoreProperties("tickets")
    private Estacionamento estacionamento;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Pagamento pagamento;

    public Ticket(){
    }

    public Ticket(Long idTicket, Boolean pago, double valor, String qrCodeToken, LocalDateTime qrCodeExpiration, String qrCodeBase64, LocalDateTime hrEntrada, LocalDateTime hrSaida) {
        this.idTicket = idTicket;
        this.pago = pago;
        this.valor = valor;
        this.qrCodeToken = qrCodeToken;
        this.qrCodeExpiration = qrCodeExpiration;
        this.qrCodeBase64 = qrCodeBase64;
        this.hrEntrada = hrEntrada;
        this.hrSaida = hrSaida;
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

    public LocalDateTime getQrCodeExpiration() {
        return qrCodeExpiration;
    }

    public void setQrCodeExpiration(LocalDateTime qrCodeExpiration) {
        this.qrCodeExpiration = qrCodeExpiration;
    }

    public String getQrCodeBase64() {
        return qrCodeBase64;
    }

    public void setQrCodeBase64(String qrCodeBase64) {
        this.qrCodeBase64 = qrCodeBase64;
    }

    public LocalDateTime getHrEntrada() {
        return hrEntrada;
    }

    public void setHrEntrada(LocalDateTime hrEntrada) {
        this.hrEntrada = hrEntrada;
    }

    public LocalDateTime getHrSaida() {
        return hrSaida;
    }

    public void setHrSaida(LocalDateTime hrSaida) {
        this.hrSaida = hrSaida;
    }

    public Estacionamento getEstacionamento() {
        return estacionamento;
    }

    public void setEstacionamento(Estacionamento estacionamento) {
        this.estacionamento = estacionamento;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}

