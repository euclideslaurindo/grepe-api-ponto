# Recria TODOS os arquivos DTO do relatorio (use se o build falhar com "package dto.relatorio does not exist")
$dir = Join-Path $PSScriptRoot "src\main\java\br\gov\pe\gre\grepe_api\dto\relatorio"
New-Item -ItemType Directory -Force -Path $dir | Out-Null

@'
package br.gov.pe.gre.grepe_api.dto.relatorio;

public enum StatusDia {
    OK,
    INCONSISTENTE
}
'@ | Set-Content (Join-Path $dir "StatusDia.java") -Encoding UTF8

@'
package br.gov.pe.gre.grepe_api.dto.relatorio;

public class BatidaRelatorioDto {
    private String hora;
    public BatidaRelatorioDto() {}
    public BatidaRelatorioDto(String hora) { this.hora = hora; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
}
'@ | Set-Content (Join-Path $dir "BatidaRelatorioDto.java") -Encoding UTF8

@'
package br.gov.pe.gre.grepe_api.dto.relatorio;

import java.util.ArrayList;
import java.util.List;

public class DiaRelatorioDto {
    private String data;
    private StatusDia status;
    private List<BatidaRelatorioDto> batidas = new ArrayList<>();
    private String minutosTrabalhadosFormatado;
    private String saldoDiaFormatado;
    private Integer saldoDiaMinutos;
    private String mensagem;
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public StatusDia getStatus() { return status; }
    public void setStatus(StatusDia status) { this.status = status; }
    public List<BatidaRelatorioDto> getBatidas() { return batidas; }
    public void setBatidas(List<BatidaRelatorioDto> batidas) { this.batidas = batidas; }
    public String getMinutosTrabalhadosFormatado() { return minutosTrabalhadosFormatado; }
    public void setMinutosTrabalhadosFormatado(String v) { this.minutosTrabalhadosFormatado = v; }
    public String getSaldoDiaFormatado() { return saldoDiaFormatado; }
    public void setSaldoDiaFormatado(String v) { this.saldoDiaFormatado = v; }
    public Integer getSaldoDiaMinutos() { return saldoDiaMinutos; }
    public void setSaldoDiaMinutos(Integer v) { this.saldoDiaMinutos = v; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
'@ | Set-Content (Join-Path $dir "DiaRelatorioDto.java") -Encoding UTF8

@'
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
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getPeriodoInicio() { return periodoInicio; }
    public void setPeriodoInicio(String v) { this.periodoInicio = v; }
    public String getPeriodoFim() { return periodoFim; }
    public void setPeriodoFim(String v) { this.periodoFim = v; }
    public int getCargaHorariaDiariaMinutos() { return cargaHorariaDiariaMinutos; }
    public void setCargaHorariaDiariaMinutos(int v) { this.cargaHorariaDiariaMinutos = v; }
    public String getCargaHorariaDiariaFormatada() { return cargaHorariaDiariaFormatada; }
    public void setCargaHorariaDiariaFormatada(String v) { this.cargaHorariaDiariaFormatada = v; }
    public List<DiaRelatorioDto> getDias() { return dias; }
    public void setDias(List<DiaRelatorioDto> dias) { this.dias = dias; }
    public int getTotalSaldoMinutos() { return totalSaldoMinutos; }
    public void setTotalSaldoMinutos(int v) { this.totalSaldoMinutos = v; }
    public String getTotalSaldoFormatado() { return totalSaldoFormatado; }
    public void setTotalSaldoFormatado(String v) { this.totalSaldoFormatado = v; }
}
'@ | Set-Content (Join-Path $dir "RelatorioPeriodoDto.java") -Encoding UTF8

Write-Host "4 arquivos DTO criados em:"
Write-Host $dir
Get-ChildItem $dir -Filter *.java | ForEach-Object { Write-Host "  -" $_.Name }
