package ticketEstacionamento.entity;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "tb_estacionamento")
public class Estacionamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "qtd_vagas")
    private Integer qtd_vagas;

    @Column(name = "taxa_horaria")
    private Double taxa_horaria;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "estacionamento")
    private List<Ticket> tickets;

    public Estacionamento() {
    }

    public Estacionamento(Long id, String nome, String endereco, Integer qtd_vagas, Double taxa_horaria, Boolean status) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.qtd_vagas = qtd_vagas;
        this.taxa_horaria = taxa_horaria;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getQtd_vagas() {
        return qtd_vagas;
    }

    public void setQtd_vagas(Integer qtd_vagas) {
        this.qtd_vagas = qtd_vagas;
    }

    public Double getTaxa_horaria() {
        return taxa_horaria;
    }

    public void setTaxa_horaria(Double taxa_horaria) {
        this.taxa_horaria = taxa_horaria;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


}
