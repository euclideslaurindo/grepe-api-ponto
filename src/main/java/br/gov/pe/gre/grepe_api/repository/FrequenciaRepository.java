package br.gov.pe.gre.grepe_api.repository;

/**
 *
 * @author euclides.souza
 */
import br.gov.pe.gre.grepe_api.model.Frequencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FrequenciaRepository extends JpaRepository<Frequencia, String> {

    List<Frequencia> findByMatriculaFrequencia(String matriculaFrequencia);

    @Query("SELECT f FROM Frequencia f WHERE TRIM(f.matriculaFrequencia) = TRIM(:matricula)")
    List<Frequencia> findByMatriculaNormalizada(@Param("matricula") String matricula);
}
