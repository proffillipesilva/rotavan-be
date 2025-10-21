package br.edu.fiec.RotaVan.features.contratos.controllers; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.contratos.models.Contrato;
import br.edu.fiec.RotaVan.features.contratos.services.ContratoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/contratos") // Caminho base
@AllArgsConstructor
public class ContratoController {

    private final ContratoService contratoService;

    @PostMapping
    public ResponseEntity<?> createContrato(@Valid @RequestBody Contrato contrato) {
        try {
            Contrato savedContrato = contratoService.save(contrato);
            return new ResponseEntity<>(savedContrato, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) { // Captura erro de data
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) { // Captura erro de Responsavel/Motorista não encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Contrato>> getAllContratos() {
        return ResponseEntity.ok(contratoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> getContratoById(@PathVariable UUID id) {
        return contratoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/responsavel/{responsavelId}")
    public ResponseEntity<List<Contrato>> getContratosByResponsavel(@PathVariable UUID responsavelId) {
        List<Contrato> contratos = contratoService.findByResponsavelId(responsavelId);
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/motorista/{motoristaId}")
    public ResponseEntity<List<Contrato>> getContratosByMotorista(@PathVariable UUID motoristaId) {
        List<Contrato> contratos = contratoService.findByMotoristaId(motoristaId);
        return ResponseEntity.ok(contratos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContrato(@PathVariable UUID id, @Valid @RequestBody Contrato contratoDetails) {
        try {
            return contratoService.update(id, contratoDetails)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContrato(@PathVariable UUID id) {
        if (contratoService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}