package br.gov.pe.gre.grepe_api.dto.relatorio;

public class BatidaRelatorioDto {

    private String hora;

    public BatidaRelatorioDto() {
    }

    public BatidaRelatorioDto(String hora) {
        this.hora = hora;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
