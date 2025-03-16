package ticketEstacionamento.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;

//Representação da tabela 'td_teste' no banco de dados
@Entity
@Table(name = "tb_teste")
public class Teste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "senha")
    private int senha;
    @CreationTimestamp
    private Instant creationTimestamp;
    @UpdateTimestamp
    private Instant updateTimestamp;

    public Teste() {

    }

    public Teste(Long id, String nome, int senha, Instant creationTimestamp, Instant updateTimestamp) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.creationTimestamp = creationTimestamp;
        this.updateTimestamp = updateTimestamp;
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

    public int getSenha() {
        return senha;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Instant updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

}
