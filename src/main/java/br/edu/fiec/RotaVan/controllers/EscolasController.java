package br.edu.fiec.RotaVan.controllers;

import br.edu.fiec.RotaVan.models.Escolas;
import br.edu.fiec.RotaVan.services.EscolasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/escolas")
public class EscolasController {

    private final EscolasService escolasService;

    public EscolasController(EscolasService escolasService) {
        this.escolasService = escolasService;
    }

    @PostMapping
    public ResponseEntity<Escolas> createEscola(@RequestBody Escolas escola) {
        Escolas savedEscola = escolasService.save(escola);
        return new ResponseEntity<>(savedEscola, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Escolas>> getAllEscolas() {
        return ResponseEntity.ok(escolasService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Escolas> getEscolaById(@PathVariable UUID id) {
        return escolasService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Escolas> updateEscola(@PathVariable UUID id, @RequestBody Escolas escolaDetails) {
        return escolasService.update(id, escolaDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEscola(@PathVariable UUID id) {
        if (escolasService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}