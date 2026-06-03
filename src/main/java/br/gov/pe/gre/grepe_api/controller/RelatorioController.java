package br.gov.pe.gre.grepe_api.controller;

import br.gov.pe.gre.grepe_api.dto.relatorio.RelatorioPeriodoDto;
import br.gov.pe.gre.grepe_api.service.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ponto")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/relatorio/{matricula}")
    public ResponseEntity<?> gerarRelatorio(
            @PathVariable String matricula,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        try {
            RelatorioPeriodoDto relatorio = relatorioService.gerarRelatorio(matricula, inicio, fim);
            return ResponseEntity.ok(relatorio);
        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }
    }
}
