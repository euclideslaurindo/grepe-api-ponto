package br.gov.pe.gre.grepe_api.controller;

import br.gov.pe.gre.grepe_api.model.Frequencia;
import br.gov.pe.gre.grepe_api.service.FrequenciaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController    //essa classe vai responder a requisições web, invés de abrir uma janela ela devolve dados em formato JSON
@RequestMapping("/api/ponto")
// Libera o acesso para o navegador não bloquear a tela
public class FrequenciaController {

    private final FrequenciaService frequenciaService;

    public FrequenciaController(FrequenciaService frequenciaService) {
        this.frequenciaService = frequenciaService;
    }

    @GetMapping("/consulta/{matricula}")
    public List<Frequencia> obterFrequencias(@PathVariable String matricula) {
        return frequenciaService.buscarFrequencias(matricula);
    }
}