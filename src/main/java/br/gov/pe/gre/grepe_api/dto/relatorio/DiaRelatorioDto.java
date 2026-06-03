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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public StatusDia getStatus() {
        return status;
    }

    public void setStatus(StatusDia status) {
        this.status = status;
    }

    public List<BatidaRelatorioDto> getBatidas() {
        return batidas;
    }

    public void setBatidas(List<BatidaRelatorioDto> batidas) {
        this.batidas = batidas;
    }

    public String getMinutosTrabalhadosFormatado() {
        return minutosTrabalhadosFormatado;
    }

    public void setMinutosTrabalhadosFormatado(String minutosTrabalhadosFormatado) {
        this.minutosTrabalhadosFormatado = minutosTrabalhadosFormatado;
    }

    public String getSaldoDiaFormatado() {
        return saldoDiaFormatado;
    }

    public void setSaldoDiaFormatado(String saldoDiaFormatado) {
        this.saldoDiaFormatado = saldoDiaFormatado;
    }

    public Integer getSaldoDiaMinutos() {
        return saldoDiaMinutos;
    }

    public void setSaldoDiaMinutos(Integer saldoDiaMinutos) {
        this.saldoDiaMinutos = saldoDiaMinutos;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
