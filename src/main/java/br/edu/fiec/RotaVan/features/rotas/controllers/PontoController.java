package br.edu.fiec.RotaVan.features.rotas.controllers; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.services.PontoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/pontos") // Caminho base para pontos individuais
@AllArgsConstructor
public class PontoController {

    private final PontoService pontoService;

    // Não faz sentido criar um ponto sem rota, então não teremos POST aqui.
    // A criação é feita via POST /v1/api/rotas/{rotaId}/pontos

    @GetMapping("/{id}")
    public ResponseEntity<Ponto> getPontoById(@PathVariable UUID id) {
        return pontoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ponto> updatePonto(@PathVariable UUID id, @Valid @RequestBody Ponto pontoDetails) {
        // Nota: Este update não altera a rota do ponto.
        return pontoService.update(id, pontoDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePonto(@PathVariable UUID id) {
        if (pontoService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}