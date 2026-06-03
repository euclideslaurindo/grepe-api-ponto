package br.gov.pe.gre.grepe_api.controller;

import br.gov.pe.gre.grepe_api.model.FrequenciaAjuste;
import br.gov.pe.gre.grepe_api.service.FrequenciaAjusteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ajustes")

public class FrequenciaAjusteController {

    @Autowired
    private FrequenciaAjusteService service;

    //servidor envia a solicitação
    @PostMapping("/solicitar")
    public ResponseEntity<FrequenciaAjuste> solicitarAjuste(@RequestBody FrequenciaAjuste ajuste) {
        return ResponseEntity.ok(service.solicitarAjuste(ajuste));
    }

    // ADM listar a tabela de pendentes
    @GetMapping("/pendentes")
    public ResponseEntity<List<FrequenciaAjuste>> listarPendentes() {
        return ResponseEntity.ok(service.listarAjustesPendentes());
    }

    // para o ADM aprovar
    @PutMapping("/{id}/aprovar/{matriculaAdm}")
    public ResponseEntity<FrequenciaAjuste> aprovarAjuste(@PathVariable Long id, @PathVariable String matriculaAdm) {
        return ResponseEntity.ok(service.aprovarAjuste(id, matriculaAdm));
    }

    // para o ADM negar
    @PutMapping("/{id}/negar/{matriculaAdm}")
    public ResponseEntity<FrequenciaAjuste> negarAjuste(@PathVariable Long id, @PathVariable String matriculaAdm) {
        return ResponseEntity.ok(service.negarAjuste(id, matriculaAdm));
    }
}