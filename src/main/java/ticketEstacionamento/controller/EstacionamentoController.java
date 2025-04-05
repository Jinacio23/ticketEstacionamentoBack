package ticketEstacionamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketEstacionamento.dto.EstacionamentoDTO;
import ticketEstacionamento.entity.Estacionamento;
import ticketEstacionamento.service.EstacionamentoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estacionamento")
@CrossOrigin(origins = "*")
public class EstacionamentoController {

    @Autowired
    private EstacionamentoService estacionamentoService;

    @GetMapping
    public ResponseEntity<List<Estacionamento>> listagemDeEstacionamentos(){
        List<Estacionamento> estacionamentos = estacionamentoService.listandoFiliais();

        return ResponseEntity.ok(estacionamentos);
    }

    //Conseguindo um estacionamento pelo id
    @GetMapping("/{id}")
    public ResponseEntity<Estacionamento> conseguindoEstacionamento(@PathVariable("id") String id) {

        Optional<Estacionamento> estacionamento = estacionamentoService.estacionamentoPeloId(id);

        if(estacionamento.isPresent()){
            return ResponseEntity.ok(estacionamento.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> cadastroEstacionamento(@RequestBody EstacionamentoDTO estacionamentoDTO){

        Estacionamento estacionamento = estacionamentoService.criarEstacionamento(estacionamentoDTO);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizandoEstacionamento(@PathVariable("id") String id, @RequestBody EstacionamentoDTO estacionamentoDTO){
        // Enviar somente os dados que vão ser alterados
        estacionamentoService.atualizandoEstacionamento(id, estacionamentoDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletandoEstacionamento(@PathVariable("id") String id){
        estacionamentoService.deletandoPeloId(id);

        return ResponseEntity.noContent().build();
    }

}
