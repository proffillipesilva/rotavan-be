package br.edu.fiec.RotaVan.features.rotas.controllers; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Viagem;
import br.edu.fiec.RotaVan.features.rotas.services.ViagemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/viagens") // Caminho base
@AllArgsConstructor
public class ViagemController {

    private final ViagemService viagemService;

    @PostMapping
    public ResponseEntity<Viagem> createViagem(@Valid @RequestBody Viagem viagem) {
        // Se o service lançar IllegalArgumentException ou RuntimeException,
        // o GlobalExceptionHandler vai capturar e formatar a resposta.
        Viagem savedViagem = viagemService.save(viagem);
        return new ResponseEntity<>(savedViagem, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Viagem>> getAllViagens() {
        return ResponseEntity.ok(viagemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viagem> getViagemById(@PathVariable UUID id) {
        return viagemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Exemplo: Buscar viagens de um motorista em uma data específica
    // GET /v1/api/viagens/motorista/{motoristaId}?data=YYYY-MM-DD
    @GetMapping("/motorista/{motoristaId}")
    public ResponseEntity<List<Viagem>> getViagensByMotoristaAndData(
            @PathVariable UUID motoristaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<Viagem> viagens = viagemService.findByMotoristaIdAndData(motoristaId, data);
        return ResponseEntity.ok(viagens);
    }

    // Exemplo: Buscar viagens de uma criança em um período
    // GET /v1/api/viagens/crianca/{criancaId}?inicio=YYYY-MM-DD&fim=YYYY-MM-DD
    @GetMapping("/crianca/{criancaId}")
    public ResponseEntity<List<Viagem>> getViagensByCriancaAndPeriodo(
            @PathVariable UUID criancaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<Viagem> viagens = viagemService.findByCriancaIdAndPeriodo(criancaId, inicio, fim);
        return ResponseEntity.ok(viagens);
    }


    // Exemplo: Endpoint específico para atualizar apenas o status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Viagem> updateViagemStatus(@PathVariable UUID id, @RequestBody String status) {
        // Remover aspas extras se o status vier como "Agendada" em vez de Agendada
        String cleanStatus = status.replace("\"", "");
        return viagemService.updateStatus(id, cleanStatus)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViagem(@PathVariable UUID id) {
        if (viagemService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}