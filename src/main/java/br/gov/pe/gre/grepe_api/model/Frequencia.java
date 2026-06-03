package br.gov.pe.gre.grepe_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "frequencia")
public class Frequencia {

    @Id
    @Column(name = "ID", length = 8)
    private String id;

    @Column(name = "data", length = 15)
    private String data;

    @Column(name = "hora", length = 7)
    private String hora;

    @Column(name = "departamentoFrequencia", length = 20)
    private String departamentoFrequencia;

    @Column(name = "matriculaFrequencia", length = 8, nullable = false)
    private String matriculaFrequencia;

    public Frequencia() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDepartamentoFrequencia() {
        return departamentoFrequencia;
    }

    public void setDepartamentoFrequencia(String departamentoFrequencia) {
        this.departamentoFrequencia = departamentoFrequencia;
    }

    public String getMatriculaFrequencia() {
        return matriculaFrequencia;
    }

    public void setMatriculaFrequencia(String matriculaFrequencia) {
        this.matriculaFrequencia = matriculaFrequencia;
    }
}
