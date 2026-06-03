
package br.gov.pe.gre.grepe_api.repository;

import br.gov.pe.gre.grepe_api.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//  esse daq gerencia a conexão com a tabela de funcionários legada
// para validar se a matrícula digitada realmente pertence a um funcionário ativo antes de buscar o ponto.
@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {
    
}

