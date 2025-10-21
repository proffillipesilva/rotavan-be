package br.edu.fiec.RotaVan.features.veiculos.controllers; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.veiculos.models.Veiculo;
import br.edu.fiec.RotaVan.features.veiculos.services.VeiculoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/veiculos") // Caminho base
@AllArgsConstructor
public class VeiculoController {

    private final VeiculoService veiculoService;

    @PostMapping
    public ResponseEntity<Veiculo> createVeiculo(@Valid @RequestBody Veiculo veiculo) {
        try {
            Veiculo savedVeiculo = veiculoService.save(veiculo);
            return new ResponseEntity<>(savedVeiculo, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) { // Captura erro de placa duplicada
            return ResponseEntity.badRequest().body(null); // Ou retornar uma mensagem de erro
        } catch (RuntimeException e) { // Captura erro se motorista não existir
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ou retornar mensagem
        }
    }

    @GetMapping
    public ResponseEntity<List<Veiculo>> getAllVeiculos() {
        return ResponseEntity.ok(veiculoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> getVeiculoById(@PathVariable UUID id) {
        return veiculoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/placa/{placa}") // Exemplo de busca por placa
    public ResponseEntity<Veiculo> getVeiculoByPlaca(@PathVariable String placa) {
        return veiculoService.findByPlaca(placa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> updateVeiculo(@PathVariable UUID id, @Valid @RequestBody Veiculo veiculoDetails) {
        try {
            return veiculoService.update(id, veiculoDetails)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeiculo(@PathVariable UUID id) {
        if (veiculoService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Poderia adicionar um endpoint para listar veículos por motorista ID
    // GET /v1/api/veiculos/motorista/{motoristaId}
}