package br.gov.pe.gre.grepe_api.service;

import br.gov.pe.gre.grepe_api.dto.relatorio.DiaRelatorioDto;
import br.gov.pe.gre.grepe_api.dto.relatorio.RelatorioPeriodoDto;
import br.gov.pe.gre.grepe_api.dto.relatorio.StatusDia;
import br.gov.pe.gre.grepe_api.model.Frequencia;
import br.gov.pe.gre.grepe_api.model.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CalculadoraHorasServiceTest {

    private CalculadoraHorasService calculadora;
    private Funcionario funcionario;
    private LocalDate dia;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraHorasService();
        funcionario = new Funcionario();
        funcionario.setMatricula("12345678");
        funcionario.setNome("Servidor Teste");
        funcionario.setCh("6");
        dia = LocalDate.of(2026, 5, 15);
    }

    @Test
    void diaComBatidasImpares_deveSerInconsistente() {
        List<Frequencia> frequencias = List.of(
                batida("15/05/2026", "08:00:00"),
                batida("15/05/2026", "12:00:00"),
                batida("15/05/2026", "17:00:00"));

        RelatorioPeriodoDto relatorio = calculadora.calcularRelatorio(
                funcionario, frequencias, dia, dia);

        DiaRelatorioDto diaRelatorio = relatorio.getDias().get(0);
        assertEquals(StatusDia.INCONSISTENTE, diaRelatorio.getStatus());
        assertNull(diaRelatorio.getSaldoDiaMinutos());
        assertEquals(0, relatorio.getTotalSaldoMinutos());
    }

    @Test
    void batidasDuplicadas_devemManterApenasAPrimeira() {
        List<Frequencia> frequencias = List.of(
                batida("15/05/2026", "08:00:00"),
                batida("15/05/2026", "08:02:00"),
                batida("15/05/2026", "12:00:00"),
                batida("15/05/2026", "13:30:00"),
                batida("15/05/2026", "17:00:00"),
                batida("15/05/2026", "17:02:00"));

        RelatorioPeriodoDto relatorio = calculadora.calcularRelatorio(
                funcionario, frequencias, dia, dia);

        DiaRelatorioDto diaRelatorio = relatorio.getDias().get(0);
        assertEquals(StatusDia.OK, diaRelatorio.getStatus());
        assertEquals(4, diaRelatorio.getBatidas().size());
    }

    @Test
    void excessoMenorQueDezMinutos_naoCreditaHoraExtra() {
        funcionario.setCh("4");
        List<Frequencia> frequencias = List.of(
                batida("15/05/2026", "08:00:00"),
                batida("15/05/2026", "12:05:00"));

        RelatorioPeriodoDto relatorio = calculadora.calcularRelatorio(
                funcionario, frequencias, dia, dia);

        DiaRelatorioDto diaRelatorio = relatorio.getDias().get(0);
        assertEquals(StatusDia.OK, diaRelatorio.getStatus());
        assertEquals(0, diaRelatorio.getSaldoDiaMinutos());
    }

    @Test
    void excessoMaiorOuIgualDezMinutos_creditaHoraExtra() {
        List<Frequencia> frequencias = List.of(
                batida("15/05/2026", "08:00:00"),
                batida("15/05/2026", "12:00:00"),
                batida("15/05/2026", "13:30:00"),
                batida("15/05/2026", "16:10:00"));

        RelatorioPeriodoDto relatorio = calculadora.calcularRelatorio(
                funcionario, frequencias, dia, dia);

        DiaRelatorioDto diaRelatorio = relatorio.getDias().get(0);
        assertEquals(40, diaRelatorio.getSaldoDiaMinutos());
        assertEquals(40, relatorio.getTotalSaldoMinutos());
    }

    @Test
    void intervaloCruzandoAlmoco_deveDescontarNoventaMinutos() {
        List<Frequencia> frequencias = List.of(
                batida("15/05/2026", "08:00:00"),
                batida("15/05/2026", "17:00:00"));

        RelatorioPeriodoDto relatorio = calculadora.calcularRelatorio(
                funcionario, frequencias, dia, dia);

        DiaRelatorioDto diaRelatorio = relatorio.getDias().get(0);
        assertEquals(StatusDia.OK, diaRelatorio.getStatus());
        assertEquals(90, diaRelatorio.getSaldoDiaMinutos());
    }

    @Test
    void cargaHorariaVemDoCampoCh() {
        funcionario.setCh("8");
        List<Frequencia> frequencias = List.of(
                batida("15/05/2026", "08:00:00"),
                batida("15/05/2026", "12:00:00"),
                batida("15/05/2026", "13:30:00"),
                batida("15/05/2026", "17:30:00"));

        RelatorioPeriodoDto relatorio = calculadora.calcularRelatorio(
                funcionario, frequencias, dia, dia);

        assertEquals(480, relatorio.getCargaHorariaDiariaMinutos());
        assertEquals(0, relatorio.getTotalSaldoMinutos());
    }

    private Frequencia batida(String data, String hora) {
        Frequencia frequencia = new Frequencia();
        frequencia.setData(data);
        frequencia.setHora(hora);
        frequencia.setMatriculaFrequencia("12345678");
        return frequencia;
    }
}
