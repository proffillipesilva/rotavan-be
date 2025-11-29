package br.edu.fiec.RotaVan.features.rotas.controllers;

import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.rotas.services.RotaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/rotas")
@SecurityRequirement(name = "bearerAuth")
public class RotaController {

    private final RotaService rotaService;

    public RotaController(RotaService rotaService) {
        this.rotaService = rotaService;
    }

    @PostMapping
    public ResponseEntity<Rota> criarRota(@RequestBody Rota rota) {
        return ResponseEntity.ok(rotaService.criarRota(rota));
    }

    @GetMapping
    public ResponseEntity<List<Rota>> listarRotas() {
        return ResponseEntity.ok(rotaService.listarRotas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rota> buscarRotaPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(rotaService.buscarRotaPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rota> atualizarRota(@PathVariable UUID id, @RequestBody Rota rota) {
        return ResponseEntity.ok(rotaService.atualizarRota(id, rota));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRota(@PathVariable UUID id) {
        rotaService.deletarRota(id);
        return ResponseEntity.noContent().build();
    }
}