package ticketEstacionamento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ticketEstacionamento.controller.dto.EstacionamentoDTO;
import ticketEstacionamento.entity.Estacionamento;
import ticketEstacionamento.entity.Usuario;
import ticketEstacionamento.repository.EstacionamentoRepository;
import ticketEstacionamento.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EstacionamentoService {

    @Autowired
    private EstacionamentoRepository estacionamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Estacionamento> listandoFiliais(String username) {
        Optional<Usuario> usuario = Optional.ofNullable(usuarioRepository.findByNome(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!")));

        boolean isAdmin = usuario.get().getRoles()
                .stream()
                .anyMatch(role -> role.getNome().equalsIgnoreCase("ADMIN"));

        if (isAdmin){
            return estacionamentoRepository.findAll();
        } else {
            return List.of(usuario.get().getEstacionamento());
        }
    }

    public Optional<Estacionamento> estacionamentoPeloId(String id) {
        return estacionamentoRepository.findById(Long.parseLong(id));
    }

    public Estacionamento criarEstacionamento(EstacionamentoDTO estacionamentoDTO) {

        Estacionamento novaFilial = new Estacionamento(
                null,
                estacionamentoDTO.getNome(),
                estacionamentoDTO.getEndereco(),
                estacionamentoDTO.getQtd_vagas(),
                estacionamentoDTO.getTaxa_horaria(),
                estacionamentoDTO.getStatus()
        );

        return estacionamentoRepository.save(novaFilial);
    }

    public void atualizandoEstacionamento(String id, EstacionamentoDTO estacionamentoDTO) {
        Long estacionamentoId = Long.parseLong(id);
        Optional<Estacionamento> filial = estacionamentoRepository.findById(estacionamentoId);

        if(filial.isPresent()){
            Estacionamento estacionamento = filial.get();

            if(estacionamentoDTO.getNome() != null){
                estacionamento.setNome(estacionamentoDTO.getNome());
            }
            if(estacionamentoDTO.getEndereco() != null){
                estacionamento.setEndereco(estacionamentoDTO.getEndereco());
            }
            if(estacionamentoDTO.getQtd_vagas() != null){
                estacionamento.setQtd_vagas(estacionamentoDTO.getQtd_vagas());
            }
            if(estacionamentoDTO.getTaxa_horaria() != null){
                estacionamento.setTaxa_horaria(estacionamentoDTO.getTaxa_horaria());
            }
            if(estacionamentoDTO.getStatus() != null){
                estacionamento.setStatus(estacionamentoDTO.getStatus());
            }

            estacionamentoRepository.save(estacionamento);
        }
    }

    public void alternarStatusPorId(String id) {
        Long estacionamentoId = Long.parseLong(id);
        Estacionamento estacionamento = estacionamentoRepository.findById(estacionamentoId)
                .orElseThrow(() -> new RuntimeException("Estacionamento não encontrado"));

        estacionamento.setStatus(!estacionamento.getStatus());
        estacionamentoRepository.save(estacionamento);
    }

    public void deletandoPeloId(String id) {

        Long estacionamentoId = Long.parseLong(id);
        boolean estacionamentoExiste = estacionamentoRepository.existsById(estacionamentoId);

        if(estacionamentoExiste){
        estacionamentoRepository.deleteById(Long.parseLong(id));
        }
    }
}
