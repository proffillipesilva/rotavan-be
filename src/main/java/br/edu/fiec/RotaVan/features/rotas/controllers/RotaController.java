package br.edu.fiec.RotaVan.features.rotas.controllers; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.rotas.services.RotaService;
import jakarta.validation.Valid; // Para validar o corpo da requisição
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/rotas") // Define o caminho base
@AllArgsConstructor // Injeta o RotaService via construtor
public class RotaController {

    private final RotaService rotaService;

    @PostMapping
    public ResponseEntity<Rota> createRota(@Valid @RequestBody Rota rota) {
        // A lógica de salvar os pontos associados já está no RotaServiceImpl
        Rota savedRota = rotaService.save(rota);
        return new ResponseEntity<>(savedRota, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Rota>> getAllRotas() {
        return ResponseEntity.ok(rotaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rota> getRotaById(@PathVariable UUID id) {
        return rotaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rota> updateRota(@PathVariable UUID id, @Valid @RequestBody Rota rotaDetails) {
        return rotaService.update(id, rotaDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRota(@PathVariable UUID id) {
        if (rotaService.deleteById(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }

    // --- Endpoints específicos para Pontos dentro de uma Rota ---

    @PostMapping("/{rotaId}/pontos")
    public ResponseEntity<Ponto> addPontoToRota(@PathVariable UUID rotaId, @Valid @RequestBody Ponto ponto) {
        return rotaService.adicionarPontoNaRota(rotaId, ponto)
                .map(savedPonto -> new ResponseEntity<>(savedPonto, HttpStatus.CREATED))
                .orElse(ResponseEntity.notFound().build()); // Retorna 404 se a rota não existir
    }

    @GetMapping("/{rotaId}/pontos")
    public ResponseEntity<List<Ponto>> getPontosByRotaId(@PathVariable UUID rotaId) {
        // Verifica se a rota existe antes de buscar os pontos (opcional, mas bom)
        if (rotaService.findById(rotaId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Ponto> pontos = rotaService.findPontosByRotaId(rotaId); // Usa o método do service
        return ResponseEntity.ok(pontos);
    }

    // Você precisaria adicionar métodos no RotaService e PontoService/Repository
    // para atualizar ou deletar um ponto específico de uma rota, se necessário.
    // Ex: PUT /v1/api/rotas/{rotaId}/pontos/{pontoId}
    // Ex: DELETE /v1/api/rotas/{rotaId}/pontos/{pontoId}
}