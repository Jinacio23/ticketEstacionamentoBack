package ticketEstacionamento.service;

import org.springframework.stereotype.Service;
import ticketEstacionamento.controller.dto.TesteDTO;
import ticketEstacionamento.entity.Teste;
import ticketEstacionamento.repository.TesteRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

//Service representa a lógica referente a regra de negócios
@Service
public class TesteService {

    //Injeção de dependências
    private final TesteRepository testeRepository;

    public TesteService(TesteRepository testeRepository) {
        this.testeRepository = testeRepository;
    }

    public Long createTeste(TesteDTO testeDTO) {
        //DTO -> Entity
        var entity = new Teste(
                null,
                testeDTO.nome(),
                testeDTO.senha(),
                Instant.now(),
                null);

        var testeSaved =  testeRepository.save(entity);
        return testeSaved.getId();
    }

    public Optional<Teste> getTesteById(String id){
        return testeRepository.findById(Long.parseLong(id));
    }

    public List<Teste> listTeste(){
        return testeRepository.findAll();
    }

    public void updateTeste(String id, TesteDTO testeDTO){
        var testeId = Long.parseLong(id);

        var testeEntity = testeRepository.findById(testeId);

        if(testeEntity.isPresent()){
            var teste = testeEntity.get();

            if(testeDTO.nome() != null){
                teste.setNome(testeDTO.nome());
            }
            if(testeDTO.senha() != teste.getSenha() && testeDTO.senha() != 0) {
                teste.setSenha(testeDTO.senha());
            }

            testeRepository.save(teste);
        }
    }

    public void deleteById(String id){
        var testeId = Long.parseLong(id);

        var testeExist = testeRepository.existsById(testeId);

        if(testeExist){
            testeRepository.deleteById(testeId);
        }
    }

}
