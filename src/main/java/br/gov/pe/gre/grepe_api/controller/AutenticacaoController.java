package br.gov.pe.gre.grepe_api.controller;

import br.gov.pe.gre.grepe_api.model.Funcionario;
import br.gov.pe.gre.grepe_api.service.AutenticacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    // Excelente prática: Injeção por construtor!
    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credenciais) {
        String matricula = credenciais.get("matricula");
        String senha = credenciais.get("senha");
        Map<String, Object> resposta = new HashMap<>();

        try {
            // 1. O Service vai no banco legadão e verifica a senha
            Funcionario funcionario = autenticacaoService.realizarLogin(matricula, senha);
            boolean primeiroAcesso = autenticacaoService.isPrimeiroAcesso(funcionario);
            
            // 2. Lógica para descobrir se é ADM (Ex: Se for do setor CTI ou RH)
            // Aqui você deve adaptar para a lógica exata do seu banco legado.
            boolean isAdm = false;
            if (funcionario.getDepartamento() != null && 
               (funcionario.getDepartamento().contains("CTI") || funcionario.getDepartamento().contains("RH"))) {
                isAdm = true;
            }

            // 3. Monta o pacote perfeito para o Frontend HTML
            resposta.put("sucesso", true);
            resposta.put("primeiroAcesso", primeiroAcesso);
            resposta.put("nome", funcionario.getNome()); 
            resposta.put("isAdmin", isAdm); // <-- O Frontend vai usar isso para liberar as telas!
            
            return ResponseEntity.ok(resposta);

        } catch (RuntimeException e) {
            resposta.put("sucesso", false);
            resposta.put("mensagem", e.getMessage()); // "Credenciais inválidas"
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
        }
    }

    @PostMapping("/trocar-senha")
    public ResponseEntity<Map<String, Object>> trocarSenha(@RequestBody Map<String, String> dados) {
        String matricula = dados.get("matricula");
        String senhaAtual = dados.get("senhaAntiga"); // A trava de segurança!
        String novaSenha = dados.get("novaSenha");
        Map<String, Object> resposta = new HashMap<>();
        
        // Validações de segurança antes de ir pro banco
        if (novaSenha == null || novaSenha.length() < 6) {
            resposta.put("sucesso", false);
            resposta.put("mensagem", "A nova senha deve ter no mínimo 6 caracteres.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
        }

        try {
            // Agora o Service exige a senha atual para autorizar a troca da nova
            autenticacaoService.trocarSenha(matricula, senhaAtual, novaSenha);
            
            resposta.put("sucesso", true);
            resposta.put("mensagem", "Senha atualizada com sucesso!");
            return ResponseEntity.ok(resposta);
            
        } catch (RuntimeException e) {
            resposta.put("sucesso", false);
            resposta.put("mensagem", e.getMessage()); // Ex: "Senha atual incorreta."
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
        }
    }
}