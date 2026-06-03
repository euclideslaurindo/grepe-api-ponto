package br.gov.pe.gre.grepe_api.service;

import br.gov.pe.gre.grepe_api.model.FrequenciaAjuste;
import br.gov.pe.gre.grepe_api.repository.FrequenciaAjusteRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FrequenciaAjusteService {
    
    @Autowired
    private FrequenciaAjusteRepository repository;
    
    //pra o servidor pedir o ajuste pra o ADM
    
    public FrequenciaAjuste solicitarAjuste (FrequenciaAjuste ajuste){
        ajuste.setStatus("PENDENTE");
        ajuste.setDataAprovacao(null);
        ajuste.setMatriculaAdmAprovador(null);
        return repository.save(ajuste);
    }
    //o adm ve a lista com os pendentes
    public List<FrequenciaAjuste> listarAjustesPendentes(){
        return repository.findByStatusOrderByDataEsquecidaDesc("PENDENTE");
    }
    
    //pra o adm aprovar
    public FrequenciaAjuste aprovarAjuste(Long idAjuste, String matriculaAdm) {
        FrequenciaAjuste ajuste = repository.findById(idAjuste).orElseThrow(() -> new RuntimeException ("Ajuste não encontrado."));
        
        ajuste.setStatus("APROVADA");
        ajuste.setMatriculaAdmAprovador(matriculaAdm);
        ajuste.setDataAprovacao(LocalDateTime.now());
        
        return repository.save(ajuste);
       
    }
    
    public FrequenciaAjuste negarAjuste(Long idAjuste, String matriculaAdm){
        FrequenciaAjuste ajuste = repository.findById(idAjuste).orElseThrow(() -> new RuntimeException ("Ajuste não encontrado."));
        
        ajuste.setStatus ("NEGADA");
        ajuste.setMatriculaAdmAprovador(matriculaAdm);
        ajuste.setDataAprovacao(LocalDateTime.now());
        
        return repository.save(ajuste);
    }
    
    
    
}