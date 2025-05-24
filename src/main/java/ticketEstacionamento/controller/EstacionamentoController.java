package ticketEstacionamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ticketEstacionamento.controller.dto.EstacionamentoDTO;
import ticketEstacionamento.entity.Estacionamento;
import ticketEstacionamento.service.EstacionamentoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estacionamento")
public class EstacionamentoController {

    @Autowired
    private EstacionamentoService estacionamentoService;

    //Listagem dos estacionamentos
    //Se usuario ADMIN, retorna todos os registros, senão apenas o vinculado
    @GetMapping
    public ResponseEntity<List<Estacionamento>> listagemDeEstacionamentosPorRole(){
        String username = "";
        //Conseguir usuario logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            var token = jwtAuth.getToken();
            username = token.getClaimAsString("username");
        }

        List<Estacionamento> estacionamentos = estacionamentoService.listandoFiliais(username);
        return ResponseEntity.ok(estacionamentos);
    }

    //Estacionamento por id
    @GetMapping("/{id}")
    public ResponseEntity<Estacionamento> estacionamentoPorId(@PathVariable("id") String id) {

        Optional<Estacionamento> estacionamento = estacionamentoService.estacionamentoPeloId(id);

        if(estacionamento.isPresent()){
            return ResponseEntity.ok(estacionamento.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Cadastro do estacionamento
    @PostMapping
    public ResponseEntity<Void> cadastroEstacionamento(@RequestBody EstacionamentoDTO estacionamentoDTO){

        Estacionamento estacionamento = estacionamentoService.criarEstacionamento(estacionamentoDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Atualizar estacionamento
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizandoEstacionamento(@PathVariable("id") String id, @RequestBody EstacionamentoDTO estacionamentoDTO){
        //Enviar somente os dados que vão ser alterados
        estacionamentoService.atualizandoEstacionamento(id, estacionamentoDTO);

        return ResponseEntity.noContent().build();
    }

    //Exclusão lógica de estacionamento
    @PutMapping("/toggle/{id}")
    public ResponseEntity<Void> alternarStatusEstacionamento(@PathVariable("id") String id){
        estacionamentoService.alternarStatusPorId(id);

        return ResponseEntity.noContent().build();
    }

    //Deletando estacionamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletandoEstacionamento(@PathVariable("id") String id){
        estacionamentoService.deletandoPeloId(id);

        return ResponseEntity.ok().build();
    }

}
