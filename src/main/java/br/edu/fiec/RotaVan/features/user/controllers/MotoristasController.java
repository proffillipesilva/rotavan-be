package br.edu.fiec.RotaVan.features.user.controllers;

import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.services.MotoristasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/motoristas") // URL base para todos os endpoints de motoristas
public class MotoristasController {

    private final MotoristasService motoristasService;

    public MotoristasController(MotoristasService motoristasService) {
        this.motoristasService = motoristasService;
    }

    /**
     * Endpoint para CRIAR um novo motorista.
     * URL: POST http://localhost:8080/motoristas
     */
    @PostMapping
    public ResponseEntity<Motoristas> createMotorista(@RequestBody Motoristas motorista) {
        Motoristas savedMotorista = motoristasService.save(motorista);
        return new ResponseEntity<>(savedMotorista, HttpStatus.CREATED);
    }

    /**
     * Endpoint para LISTAR TODOS os motoristas.
     * URL: GET http://localhost:8080/motoristas
     */
    @GetMapping
    public ResponseEntity<List<Motoristas>> getAllMotoristas() {
        List<Motoristas> motoristas = motoristasService.findAll();
        return ResponseEntity.ok(motoristas);
    }

    /**
     * Endpoint para BUSCAR UM motorista pelo seu ID.
     * URL: GET http://localhost:8080/motoristas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Motoristas> getMotoristaById(@PathVariable UUID id) {
        return motoristasService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para ATUALIZAR um motorista existente.
     * URL: PUT http://localhost:8080/motoristas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Motoristas> updateMotorista(@PathVariable UUID id, @RequestBody Motoristas motoristaDetails) {
        return motoristasService.update(id, motoristaDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para APAGAR um motorista pelo seu ID.
     * URL: DELETE http://localhost:8080/motoristas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMotorista(@PathVariable UUID id) {
        if (motoristasService.deleteById(id)) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}