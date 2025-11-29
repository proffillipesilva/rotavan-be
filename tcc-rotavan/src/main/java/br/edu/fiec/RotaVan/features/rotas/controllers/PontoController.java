package br.edu.fiec.RotaVan.features.rotas.controllers;

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.services.PontoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/pontos")
@SecurityRequirement(name = "bearerAuth")
public class PontoController {

    private final PontoService pontoService;

    public PontoController(PontoService pontoService) {
        this.pontoService = pontoService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo ponto de parada")
    public ResponseEntity<Ponto> criarPonto(@RequestBody Ponto ponto) {
        // O service já trata o geocoding (latitude/longitude)
        return ResponseEntity.status(HttpStatus.CREATED).body(pontoService.criarPonto(ponto));
    }

    @GetMapping
    @Operation(summary = "Lista todos os pontos cadastrados")
    public ResponseEntity<List<Ponto>> listarPontos() {
        return ResponseEntity.ok(pontoService.listarPontos());
    }

    // CORREÇÃO PRINCIPAL AQUI:
    // 1. @PathVariable agora é Integer (não UUID)
    // 2. O método chamado é buscarPontoPorId (não findById)
    @GetMapping("/{id}")
    @Operation(summary = "Busca um ponto específico por ID")
    public ResponseEntity<Ponto> buscarPontoPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pontoService.buscarPontoPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um ponto existente")
    public ResponseEntity<Ponto> atualizarPonto(@PathVariable UUID id, @RequestBody Ponto ponto) {
        return ResponseEntity.ok(pontoService.atualizarPonto(id, ponto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um ponto")
    public ResponseEntity<Void> deletarPonto(@PathVariable UUID id) {
        pontoService.deletarPonto(id); // Nome corrigido para deletarPonto
        return ResponseEntity.noContent().build();
    }
}