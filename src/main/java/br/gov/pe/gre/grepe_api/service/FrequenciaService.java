package br.gov.pe.gre.grepe_api.service;

import br.gov.pe.gre.grepe_api.model.Frequencia;
import br.gov.pe.gre.grepe_api.model.Funcionario;
import br.gov.pe.gre.grepe_api.repository.FrequenciaLegadoRepository;
import br.gov.pe.gre.grepe_api.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FrequenciaService {

    private final FrequenciaLegadoRepository frequenciaLegadoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public FrequenciaService(FrequenciaLegadoRepository frequenciaLegadoRepository, FuncionarioRepository funcionarioRepository) {
        this.frequenciaLegadoRepository = frequenciaLegadoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Frequencia> buscarFrequencias(String matricula) {
        
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(matricula);
        
        if (funcionarioOpt.isEmpty()) {
            throw new RuntimeException("Erro: Funcionario nao encontrado com a matricula " + matricula);
        }

        return frequenciaLegadoRepository.buscarHistoricoPorMatricula(matricula).stream()
                .filter(f -> ParseadorDataHoraLegado.parseData(f.getData()) != null)
                .filter(f -> ParseadorDataHoraLegado.parseHora(f.getHora()) != null)
                .collect(Collectors.toList());
    }
}