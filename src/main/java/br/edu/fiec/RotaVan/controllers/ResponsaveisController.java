package br.edu.fiec.RotaVan.controllers;

import br.edu.fiec.RotaVan.models.Responsaveis;
import br.edu.fiec.RotaVan.services.ResponsaveisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Importação recomendada para mais controlo
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Define um caminho (URL) base para todos os métodos nesta classe.
// Ex: http://localhost:8080/responsaveis
@RestController
@RequestMapping("/responsaveis")
public class ResponsaveisController {

    private final ResponsaveisService responsaveisService;

    // Injeção de dependência via construtor (forma recomendada)
    public ResponsaveisController(ResponsaveisService responsaveisService) {
        this.responsaveisService = responsaveisService;
    }

    /**
     * Endpoint para CRIAR um novo responsável.
     * Acede através de: POST http://localhost:8080/responsaveis
     * O corpo do pedido deve conter um JSON com os dados do novo responsável.
     * @param novoResponsavel O objeto Responsaveis vindo do corpo do pedido (JSON).
     * @return O responsável que foi criado e guardado na base de dados.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Responsaveis criaResponsavel(@RequestBody Responsaveis novoResponsavel) {
        return this.responsaveisService.criaResponsavel(novoResponsavel);
    }

    /**
     * Endpoint para LISTAR TODOS os responsáveis.
     * Acede através de: GET http://localhost:8080/responsaveis
     * @return Uma lista com todos os responsáveis encontrados.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Responsaveis> listarTodos() {
        return this.responsaveisService.findAll(); // Supondo que o teu serviço tem um método findAll()
    }

    /**
     * Endpoint para BUSCAR UM responsável pelo seu ID.
     * Acede através de: GET http://localhost:8080/responsaveis/1 (onde 1 é o ID)
     * @param id O ID do responsável a ser procurado, vindo do URL.
     * @return O responsável encontrado ou um status 404 (Not Found) se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Responsaveis> getResponsavelPorId(@PathVariable UUID id) {
        // Optional é uma boa prática para lidar com a possibilidade de não encontrar o objeto
        Optional<Responsaveis> responsavel = this.responsaveisService.findById(id); // Supondo que o teu serviço tem um método findById()

        if (responsavel.isPresent()) {
            return ResponseEntity.ok(responsavel.get()); // Retorna 200 OK com o objeto
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found
        }
    }
}