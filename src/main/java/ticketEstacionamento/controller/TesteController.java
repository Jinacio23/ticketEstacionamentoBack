package ticketEstacionamento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketEstacionamento.dto.TesteDTO;
import ticketEstacionamento.entity.Teste;
import ticketEstacionamento.service.TesteService;

import java.net.URI;
import java.util.List;

//Definindo rota raiz do controller
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TesteController {

    //Injeção de dependências
    private final TesteService testeService;

    public TesteController(TesteService testeService) {
        this.testeService = testeService;
    }

    //Conseguindo um teste pelo id
    @GetMapping("/{id}")
    public ResponseEntity<Teste> getTeste(@PathVariable("id") String id) {

        var teste = testeService.getTesteById(id);

        if(teste.isPresent()){
            return ResponseEntity.ok(teste.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Listando registros
    @GetMapping("/list")
    public ResponseEntity<List<Teste>> listTestes() {
        var testes = testeService.listTeste();

        return ResponseEntity.ok(testes);
    }

    //Criando teste
    @PostMapping
    public ResponseEntity<Object> createTeste(@RequestBody TesteDTO testeDTO){
        var testeId = testeService.createTeste(testeDTO);

        return ResponseEntity.created(URI.create("/api/" + testeId.toString())).build();
    }

   //Atualizando teste
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTeste(@PathVariable("id") String id, @RequestBody TesteDTO testeDTO){
        // Enviar somente os dados que vão ser alterados
        testeService.updateTeste(id, testeDTO);
        return ResponseEntity.noContent().build();
    }

   //Deletando teste
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeste(@PathVariable("id") String id){
        testeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
