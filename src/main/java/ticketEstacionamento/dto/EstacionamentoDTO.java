package ticketEstacionamento.dto;

public class EstacionamentoDTO {
    private String nome;
    private String endereco;
    private Integer qtd_vagas;
    private Boolean status;

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
