package br.edu.fiec.RotaVan.features.solicitacao.controllers;

import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.solicitacao.dto.DecisaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.dto.SolicitacaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao;
import br.edu.fiec.RotaVan.features.solicitacao.services.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/solicitacoes")
@AllArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    /**
     * PASSO 1 e 2: Responsável solicita a vaga.
     */
    @PostMapping
    public ResponseEntity<Solicitacao> criarSolicitacao(@Valid @RequestBody SolicitacaoRequestDTO request) {
        Solicitacao solicitacao = solicitacaoService.criarSolicitacaoEGerarRotas(request);
        return new ResponseEntity<>(solicitacao, HttpStatus.CREATED);
    }

    /**
     * PASSO 3 (View do Motorista): Motorista vê as rotas sugestão da solicitação.
     */
    @GetMapping("/{id}/rotas")
    public ResponseEntity<List<Rota>> getRotasDaSolicitacao(@PathVariable UUID id) {
        // Busca a solicitação e retorna a lista de rotas dela
        // (O seu SolicitacaoRepository pode precisar de um findById)
        // Esta é uma implementação simples; o ideal é o serviço buscar:
        Solicitacao solicitacao = solicitacaoService.findById(id).orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
        return ResponseEntity.ok(solicitacao.getRotasSugeridas());
    }

    /**
     * PASSO 3 e 4: Motorista Aceita ou Recusa.
     */
    @PatchMapping("/{id}/decisao")
    public ResponseEntity<Solicitacao> decidirSolicitacao(@PathVariable UUID id, @Valid @RequestBody DecisaoRequestDTO request) {
        Solicitacao solicitacao = solicitacaoService.decidirSolicitacao(id, request);
        return ResponseEntity.ok(solicitacao);
    }

    // TODO: Adicionar um GET para o motorista listar todas as solicitações PENDENTES
    // Ex: @GetMapping("/motorista/{motoristaId}/pendentes")
}