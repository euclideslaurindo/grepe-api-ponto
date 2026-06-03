package br.gov.pe.gre.grepe_api.repository;

import br.gov.pe.gre.grepe_api.model.FrequenciaAjuste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FrequenciaAjusteRepository extends JpaRepository<FrequenciaAjuste, Long> {
    
    List<FrequenciaAjuste> findByMatriculaFrequenciaAndStatus(String matricula, String status);
    
    List<FrequenciaAjuste> findByStatusOrderByDataEsquecidaDesc(String status);
}