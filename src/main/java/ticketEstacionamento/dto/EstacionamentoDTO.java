package ticketEstacionamento.dto;

public class EstacionamentoDTO {
    private String nome;
    private String endereco;
    private Integer qtd_vagas;
    private Double taxa_horaria;
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
