package br.edu.fiec.RotaVan.features.user.controllers; // Verifique se o pacote está correto

import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.services.ResponsaveisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/responsaveis")
public class ResponsaveisController {

    private final ResponsaveisService responsaveisService;

    public ResponsaveisController(ResponsaveisService responsaveisService) {
        this.responsaveisService = responsaveisService;
    }


    @PostMapping
    public ResponseEntity<Responsaveis> createResponsavel(@RequestBody Responsaveis responsavel) {
        // O código Java não muda, mas o JSON enviado no Postman agora deve conter a lista de crianças.
        Responsaveis savedResponsavel = responsaveisService.criaResponsavel(responsavel);
        return new ResponseEntity<>(savedResponsavel, HttpStatus.CREATED);
    }


    @PostMapping("/{responsavelId}/criancas")
    public ResponseEntity<Crianca> adicionarCrianca(
            @PathVariable UUID responsavelId,
            @RequestBody Crianca novaCrianca) {

        return responsaveisService.adicionarCrianca(responsavelId, novaCrianca)
                .map(crianca -> new ResponseEntity<>(crianca, HttpStatus.CREATED))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Responsaveis>> getAllResponsaveis() {
        return ResponseEntity.ok(responsaveisService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Responsaveis> getResponsavelById(@PathVariable UUID id) {
        return responsaveisService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Responsaveis> updateResponsavel(@PathVariable UUID id, @RequestBody Responsaveis responsavelDetails) {
        return responsaveisService.update(id, responsavelDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}