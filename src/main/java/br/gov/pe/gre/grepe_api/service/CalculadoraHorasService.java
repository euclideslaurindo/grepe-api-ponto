package br.gov.pe.gre.grepe_api.service;

import br.gov.pe.gre.grepe_api.dto.relatorio.BatidaRelatorioDto;
import br.gov.pe.gre.grepe_api.dto.relatorio.DiaRelatorioDto;
import br.gov.pe.gre.grepe_api.dto.relatorio.RelatorioPeriodoDto;
import br.gov.pe.gre.grepe_api.dto.relatorio.StatusDia;
import br.gov.pe.gre.grepe_api.model.Frequencia;
import br.gov.pe.gre.grepe_api.model.Funcionario;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalculadoraHorasService {

    static final int DEDUPLICACAO_MINUTOS = 5;
    static final int MINIMO_HORA_EXTRA_MINUTOS = 10;
    static final int ALMOCO_MINUTOS = 90;
    static final LocalTime ALMOCO_INICIO = LocalTime.of(12, 0);
    static final LocalTime ALMOCO_FIM = LocalTime.of(13, 30);
    static final int CARGA_PADRAO_MINUTOS = 480;

    private static final DateTimeFormatter FORMATO_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PeriodoRelatorio resolverPeriodo(List<Frequencia> frequencias, LocalDate inicioInformado, LocalDate fimInformado) {
        LocalDate hoje = LocalDate.now();
        LocalDate menorData = null;
        LocalDate maiorData = null;

        for (Frequencia frequencia : frequencias) {
            LocalDate data = ParseadorDataHoraLegado.parseData(frequencia.getData());
            if (data == null) {
                continue;
            }
            if (menorData == null || data.isBefore(menorData)) {
                menorData = data;
            }
            if (maiorData == null || data.isAfter(maiorData)) {
                maiorData = data;
            }
        }

        LocalDate inicio = inicioInformado != null ? inicioInformado : (menorData != null ? menorData : hoje);
        LocalDate fim = fimInformado != null ? fimInformado : (maiorData != null ? maiorData : hoje);
        return new PeriodoRelatorio(inicio, fim);
    }

    public RelatorioPeriodoDto calcularRelatorio(
            Funcionario funcionario,
            List<Frequencia> frequencias,
            LocalDate periodoInicio,
            LocalDate periodoFim) {

        int cargaDiariaMinutos = resolverCargaHorariaMinutos(funcionario.getCh());

        Map<LocalDate, List<LocalDateTime>> batidasPorDia = agruparBatidasPorDia(frequencias, periodoInicio, periodoFim);

        RelatorioPeriodoDto relatorio = new RelatorioPeriodoDto();
        relatorio.setMatricula(funcionario.getMatricula());
        relatorio.setNome(funcionario.getNome());
        relatorio.setPeriodoInicio(periodoInicio.format(FORMATO_BR));
        relatorio.setPeriodoFim(periodoFim.format(FORMATO_BR));
        relatorio.setCargaHorariaDiariaMinutos(cargaDiariaMinutos);
        relatorio.setCargaHorariaDiariaFormatada(FormatadorHoras.formatarDuracao(cargaDiariaMinutos));

        int totalSaldo = 0;
        List<DiaRelatorioDto> dias = new ArrayList<>();

        for (Map.Entry<LocalDate, List<LocalDateTime>> entrada : batidasPorDia.entrySet()) {
            DiaRelatorioDto dia = calcularDia(entrada.getKey(), entrada.getValue(), cargaDiariaMinutos);
            dias.add(dia);
            if (dia.getStatus() == StatusDia.OK && dia.getSaldoDiaMinutos() != null) {
                totalSaldo += dia.getSaldoDiaMinutos();
            }
        }

        relatorio.setDias(dias);
        relatorio.setTotalSaldoMinutos(totalSaldo);
        relatorio.setTotalSaldoFormatado(FormatadorHoras.formatarSaldo(totalSaldo));
        return relatorio;
    }

DiaRelatorioDto calcularDia(LocalDate data, List<LocalDateTime> batidasOrdenadas, int cargaDiariaMinutos) {
        DiaRelatorioDto dia = new DiaRelatorioDto();
        dia.setData(data.format(FORMATO_BR));

        List<LocalDateTime> deduplicadas = deduplicarBatidas(batidasOrdenadas);
        dia.setBatidas(deduplicadas.stream()
                .map(b -> new BatidaRelatorioDto(b.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                .collect(Collectors.toList()));

        if (deduplicadas.isEmpty()) {
            dia.setStatus(StatusDia.INCONSISTENTE);
            dia.setMensagem("Nenhuma batida válida no dia.");
            return dia;
        }

        if (deduplicadas.size() % 2 != 0) {
            dia.setStatus(StatusDia.INCONSISTENTE);
            dia.setMensagem("Registro incompleto — falta batida para fechar o dia.");
            return dia;
        }

        int minutosTrabalhados = 0;
        int minutosDescontoAlmoco = 0;

        // bateu apenas 2 vezes então não bateu o almoço.
        // bateu 4 ou mais vezes o almoço já é o buraco entre as batidas.
        boolean usarAlmocoPadrao = deduplicadas.size() == 2;

        for (int i = 0; i < deduplicadas.size(); i += 2) {
            LocalDateTime entrada = deduplicadas.get(i);
            LocalDateTime saida = deduplicadas.get(i + 1);

            if (!saida.isAfter(entrada)) {
                dia.setStatus(StatusDia.INCONSISTENTE);
                dia.setMensagem("Registro inconsistente — saída anterior ou igual à entrada.");
                return dia;
            }

            long duracaoPar = Duration.between(entrada, saida).toMinutes();
            long descontoNestePar = 0;

            // Só aplica a dedução fantasma se ele esqueceu de bater o almoço
            if (usarAlmocoPadrao) {
                descontoNestePar = calcularDescontoAlmoco(entrada, saida);
                minutosDescontoAlmoco += (int) descontoNestePar;
            }

            minutosTrabalhados += (int) (duracaoPar - descontoNestePar);
        }

        dia.setStatus(StatusDia.OK);
        dia.setMinutosTrabalhadosFormatado(FormatadorHoras.formatarDuracao(minutosTrabalhados));

        int saldoBruto = minutosTrabalhados - cargaDiariaMinutos;
        int saldoDia = calcularSaldoDia(saldoBruto);

        dia.setSaldoDiaMinutos(saldoDia);
        dia.setSaldoDiaFormatado(FormatadorHoras.formatarSaldo(saldoDia));

        if (minutosDescontoAlmoco > 0) {
            dia.setMensagem("Desconto de almoço padrão (12:00–13:30): " + FormatadorHoras.formatarDuracao(minutosDescontoAlmoco));
        }

        return dia;
    }

    int calcularSaldoDia(int saldoBruto) {
        if (saldoBruto > 0) {
            return saldoBruto >= MINIMO_HORA_EXTRA_MINUTOS ? saldoBruto : 0;
        }
        return saldoBruto;
    }

    long calcularDescontoAlmoco(LocalDateTime entrada, LocalDateTime saida) {
        LocalDate data = entrada.toLocalDate();
        LocalDateTime almocoInicio = LocalDateTime.of(data, ALMOCO_INICIO);
        LocalDateTime almocoFim = LocalDateTime.of(data, ALMOCO_FIM);

        long overlap = minutosSobrepostos(entrada, saida, almocoInicio, almocoFim);
        return Math.min(overlap, ALMOCO_MINUTOS);
    }

    long minutosSobrepostos(LocalDateTime inicio1, LocalDateTime fim1, LocalDateTime inicio2, LocalDateTime fim2) {
        LocalDateTime inicio = inicio1.isAfter(inicio2) ? inicio1 : inicio2;
        LocalDateTime fim = fim1.isBefore(fim2) ? fim1 : fim2;
        if (!fim.isAfter(inicio)) {
            return 0;
        }
        return Duration.between(inicio, fim).toMinutes();
    }

    List<LocalDateTime> deduplicarBatidas(List<LocalDateTime> batidasOrdenadas) {
        List<LocalDateTime> resultado = new ArrayList<>();
        for (LocalDateTime batida : batidasOrdenadas) {
            if (resultado.isEmpty()) {
                resultado.add(batida);
                continue;
            }
            LocalDateTime ultima = resultado.get(resultado.size() - 1);
            if (Duration.between(ultima, batida).toMinutes() < DEDUPLICACAO_MINUTOS) {
                continue;
            }
            resultado.add(batida);
        }
        return resultado;
    }

Map<LocalDate, List<LocalDateTime>> agruparBatidasPorDia(
            List<Frequencia> frequencias,
            LocalDate periodoInicio,
            LocalDate periodoFim) {

        Map<LocalDate, List<LocalDateTime>> mapa = new LinkedHashMap<>();

        List<Frequencia> ordenadas = frequencias.stream()
                .sorted(Comparator
                        .comparing((Frequencia f) -> ParseadorDataHoraLegado.parseData(f.getData()), Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(f -> ParseadorDataHoraLegado.parseHora(f.getHora()), Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        for (Frequencia frequencia : ordenadas) {
            if (ParseadorDataHoraLegado.isDataPlaceholder(frequencia.getData())) {
                continue;
            }
            LocalDate data = ParseadorDataHoraLegado.parseData(frequencia.getData());
            LocalTime hora = ParseadorDataHoraLegado.parseHora(frequencia.getHora());
            
            if (data == null) {
                continue; // Pula se a string estiver totalmente corrompida
            }
            if (hora == null) {
                hora = LocalTime.MIDNIGHT;
            }


            // sem filtrar as datas por período.

            /*
            if (data.isBefore(periodoInicio) || data.isAfter(periodoFim)) {
                continue;
            }
            */

            mapa.computeIfAbsent(data, d -> new ArrayList<>()).add(LocalDateTime.of(data, hora));
        }

        mapa.values().forEach(lista -> lista.sort(Comparator.naturalOrder()));
        return mapa;
    }

    int resolverCargaHorariaMinutos(String ch) {
        if (ch == null || ch.isBlank()) {
            return CARGA_PADRAO_MINUTOS;
        }
        String digitos = ch.trim().replaceAll("[^0-9]", "");
        if (digitos.isEmpty()) {
            return CARGA_PADRAO_MINUTOS;
        }
        try {
            int horas = Integer.parseInt(digitos);
            if (horas <= 0 || horas > 24) {
                return CARGA_PADRAO_MINUTOS;
            }
            return horas * 60;
        } catch (NumberFormatException e) {
            return CARGA_PADRAO_MINUTOS;
        }
    }

}
