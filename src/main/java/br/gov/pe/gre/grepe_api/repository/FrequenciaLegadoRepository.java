package br.gov.pe.gre.grepe_api.repository;

import br.gov.pe.gre.grepe_api.model.Frequencia;
import br.gov.pe.gre.grepe_api.service.ParseadorDataHoraLegado;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Leitura igual ao GREPE legado ({@code BDFrequencia}, case {@code '1'}):
 * JOIN com funcionarios, filtra matrícula, exclui data placeholder depois em Java.
 */
@Repository
public class FrequenciaLegadoRepository {

    private static final String SQL_HISTORICO_LEGADO = """
            SELECT f.ID, f.data, f.hora, f.departamentoFrequencia, f.matriculaFrequencia
            FROM frequencia f
            INNER JOIN funcionarios fu ON TRIM(f.matriculaFrequencia) = TRIM(fu.matricula)
            WHERE TRIM(fu.matricula) = ?
            ORDER BY f.ID DESC
            """;

    private static final String SQL_MATRICULA_DIRETA = """
            SELECT ID, data, hora, departamentoFrequencia, matriculaFrequencia
            FROM frequencia
            WHERE matriculaFrequencia = ?
            ORDER BY ID DESC
            """;

    private static final String SQL_MATRICULA_TRIM = """
            SELECT ID, data, hora, departamentoFrequencia, matriculaFrequencia
            FROM frequencia
            WHERE TRIM(matriculaFrequencia) = TRIM(?)
            ORDER BY ID DESC
            """;

    private final JdbcTemplate jdbcTemplate;

    public FrequenciaLegadoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Frequencia> buscarHistoricoPorMatricula(String matricula) {
        String base = ParseadorDataHoraLegado.normalizarMatricula(matricula);
        if (base.isEmpty()) {
            return List.of();
        }

        List<String> candidatas = new ArrayList<>();
        candidatas.add(base);
        String comZeros = padMatriculaZeros(base, 8);
        String comEspacos = padMatriculaEsquerda(base, 8);
        if (!candidatas.contains(comZeros)) {
            candidatas.add(comZeros);
        }
        if (!candidatas.contains(comEspacos)) {
            candidatas.add(comEspacos);
        }

        for (String candidata : candidatas) {
            List<Frequencia> resultado = filtrarBatidasReais(consultar(SQL_MATRICULA_DIRETA, candidata));
            if (!resultado.isEmpty()) {
                return resultado;
            }
            resultado = filtrarBatidasReais(consultar(SQL_MATRICULA_TRIM, candidata));
            if (!resultado.isEmpty()) {
                return resultado;
            }
            resultado = filtrarBatidasReais(consultar(SQL_HISTORICO_LEGADO, candidata));
            if (!resultado.isEmpty()) {
                return resultado;
            }
        }

        return List.of();
    }

    private List<Frequencia> filtrarBatidasReais(List<Frequencia> lista) {
        return lista.stream()
                .filter(f -> !ParseadorDataHoraLegado.isDataPlaceholder(f.getData()))
                .collect(Collectors.toList());
    }

    private List<Frequencia> consultar(String sql, String matricula) {
        try {
            return jdbcTemplate.query(sql, this::mapearLinha, matricula);
        } catch (Exception e) {
            return List.of();
        }
    }

    private Frequencia mapearLinha(ResultSet rs, int numeroLinha) throws SQLException {
        Frequencia frequencia = new Frequencia();
        String id = rs.getString(1);
        frequencia.setId(id != null && !id.isBlank() ? id : "L" + numeroLinha);
        frequencia.setData(rs.getString(2));
        frequencia.setHora(rs.getString(3));
        frequencia.setDepartamentoFrequencia(rs.getString(4));
        frequencia.setMatriculaFrequencia(rs.getString(5));
        return frequencia;
    }

    private String padMatriculaEsquerda(String matricula, int tamanho) {
        if (matricula.length() >= tamanho) {
            return matricula;
        }
        return String.format("%" + tamanho + "s", matricula);
    }

    private String padMatriculaZeros(String matricula, int tamanho) {
        if (matricula.length() >= tamanho) {
            return matricula;
        }
        return "0".repeat(tamanho - matricula.length()) + matricula;
    }
}
