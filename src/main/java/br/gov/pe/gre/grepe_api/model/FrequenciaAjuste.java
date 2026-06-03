package br.gov.pe.gre.grepe_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// CRIAÇÃO TABELA E COLUNAS PARA CONSULTA DE REAJUSTE DE HORÁRIOS PENDENTES
@Entity
@Table(name = "frequencia_ajustes")
public class FrequenciaAjuste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matriculaFrequencia", length = 8, nullable = false)
    private String matriculaFrequencia;

    // Etiqueta para ensinar o Java a ler "YYYY-MM-DD"
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dataEsquecida", nullable = false)
    private LocalDate dataEsquecida;

    // Etiqueta para ensinar o Java a ler "HH:MM:SS"
    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "horaSugerida", nullable = false)
    private LocalTime horaSugerida;

    @Column(name = "justificativa", length = 255)
    private String justificativa;

    // Status PENDENTE, APROVADA, NEGADA
    @Column(name = "status", length = 20, nullable = false)
    private String status = "PENDENTE"; 

    @Column(name = "matriculaAdmAprovador", length = 8)
    private String matriculaAdmAprovador;

    @Column(name = "dataAprovacao")
    private LocalDateTime dataAprovacao;

    public FrequenciaAjuste() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMatriculaFrequencia() { return matriculaFrequencia; }
    public void setMatriculaFrequencia(String matriculaFrequencia) { this.matriculaFrequencia = matriculaFrequencia; }

    public LocalDate getDataEsquecida() { return dataEsquecida; }
    public void setDataEsquecida(LocalDate dataEsquecida) { this.dataEsquecida = dataEsquecida; }

    public LocalTime getHoraSugerida() { return horaSugerida; }
    public void setHoraSugerida(LocalTime horaSugerida) { this.horaSugerida = horaSugerida; }

    public String getJustificativa() { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMatriculaAdmAprovador() { return matriculaAdmAprovador; }
    public void setMatriculaAdmAprovador(String matriculaAdmAprovador) { this.matriculaAdmAprovador = matriculaAdmAprovador; }

    public LocalDateTime getDataAprovacao() { return dataAprovacao; }
    public void setDataAprovacao(LocalDateTime dataAprovacao) { this.dataAprovacao = dataAprovacao; }
}