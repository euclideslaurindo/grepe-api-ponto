package br.gov.pe.gre.grepe_api.service;

import br.gov.pe.gre.grepe_api.dto.relatorio.RelatorioPeriodoDto;
import br.gov.pe.gre.grepe_api.model.Frequencia;
import br.gov.pe.gre.grepe_api.model.Funcionario;
import br.gov.pe.gre.grepe_api.repository.FrequenciaLegadoRepository;
import br.gov.pe.gre.grepe_api.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RelatorioService {

    private final FrequenciaLegadoRepository frequenciaLegadoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final CalculadoraHorasService calculadoraHorasService;

    public RelatorioService(
            FrequenciaLegadoRepository frequenciaLegadoRepository,
            FuncionarioRepository funcionarioRepository,
            CalculadoraHorasService calculadoraHorasService) {
        this.frequenciaLegadoRepository = frequenciaLegadoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.calculadoraHorasService = calculadoraHorasService;
    }

    public RelatorioPeriodoDto gerarRelatorio(String matricula, LocalDate inicio, LocalDate fim) {
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(matricula);
        if (funcionarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Funcionário não encontrado com a matrícula " + matricula);
        }

        List<Frequencia> frequencias = frequenciaLegadoRepository.buscarHistoricoPorMatricula(matricula);

        //PeriodoRelatorio periodo = calculadoraHorasService.resolverPeriodo(frequencias, inicio, fim);
        PeriodoRelatorio periodo = new PeriodoRelatorio(LocalDate.of(2020, 1, 1), LocalDate.of(2030, 12, 31));
        
        if (periodo.inicio().isAfter(periodo.fim())) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim.");
        }

        RelatorioPeriodoDto relatorio = calculadoraHorasService.calcularRelatorio(
                funcionarioOpt.get(), frequencias, periodo.inicio(), periodo.fim());

        if (frequencias.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhuma batida encontrada para a matrícula " + matricula
                            + ". Verifique se a matrícula do login é a mesma gravada em frequencia.matriculaFrequencia.");
        }

        if (relatorio.getDias().isEmpty()) {
            throw new IllegalArgumentException(
                    "Foram lidas " + frequencias.size() + " batidas, mas nenhuma data válida para o relatório. "
                            + "Exemplo no banco: data=\"" + frequencias.get(0).getData()
                            + "\", hora=\"" + frequencias.get(0).getHora() + "\"");
        }

        return relatorio;
    }
}
