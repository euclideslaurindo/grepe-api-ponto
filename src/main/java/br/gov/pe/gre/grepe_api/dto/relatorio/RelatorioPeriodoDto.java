package br.gov.pe.gre.grepe_api.dto.relatorio;

import java.util.ArrayList;
import java.util.List;

public class RelatorioPeriodoDto {

    private String matricula;
    private String nome;
    private String periodoInicio;
    private String periodoFim;
    private int cargaHorariaDiariaMinutos;
    private String cargaHorariaDiariaFormatada;
    private List<DiaRelatorioDto> dias = new ArrayList<>();
    private int totalSaldoMinutos;
    private String totalSaldoFormatado;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(String periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public String getPeriodoFim() {
        return periodoFim;
    }

    public void setPeriodoFim(String periodoFim) {
        this.periodoFim = periodoFim;
    }

    public int getCargaHorariaDiariaMinutos() {
        return cargaHorariaDiariaMinutos;
    }

    public void setCargaHorariaDiariaMinutos(int cargaHorariaDiariaMinutos) {
        this.cargaHorariaDiariaMinutos = cargaHorariaDiariaMinutos;
    }

    public String getCargaHorariaDiariaFormatada() {
        return cargaHorariaDiariaFormatada;
    }

    public void setCargaHorariaDiariaFormatada(String cargaHorariaDiariaFormatada) {
        this.cargaHorariaDiariaFormatada = cargaHorariaDiariaFormatada;
    }

    public List<DiaRelatorioDto> getDias() {
        return dias;
    }

    public void setDias(List<DiaRelatorioDto> dias) {
        this.dias = dias;
    }

    public int getTotalSaldoMinutos() {
        return totalSaldoMinutos;
    }

    public void setTotalSaldoMinutos(int totalSaldoMinutos) {
        this.totalSaldoMinutos = totalSaldoMinutos;
    }

    public String getTotalSaldoFormatado() {
        return totalSaldoFormatado;
    }

    public void setTotalSaldoFormatado(String totalSaldoFormatado) {
        this.totalSaldoFormatado = totalSaldoFormatado;
    }
}
