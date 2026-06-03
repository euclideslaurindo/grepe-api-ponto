package br.gov.pe.gre.grepe_api.service;

import br.gov.pe.gre.grepe_api.model.Funcionario;
import br.gov.pe.gre.grepe_api.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AutenticacaoService {

    private final FuncionarioRepository funcionarioRepository;

    public AutenticacaoService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    // 1. Verifica se a matrícula existe e se a senha digitada confere
    public Funcionario realizarLogin(String matricula, String senhaDigitada) {
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(matricula);

        if (funcionarioOpt.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada no sistema.");
        }

        Funcionario funcionario = funcionarioOpt.get();
        String senhaNoBanco = funcionario.getPass();

        // Verifica se a senha no banco é nula ou vazia (Primeiro Acesso)
        if (senhaNoBanco == null || senhaNoBanco.trim().isEmpty()) {
            // No primeiro acesso, a senha digitada DEVE ser igual à matrícula
            if (!senhaDigitada.equals(matricula)) {
                throw new RuntimeException("Senha incorreta."); 
            }
        } else {
            // Acesso normal (usuário já tem senha cadastrada no banco)
            if (!senhaDigitada.equals(senhaNoBanco)) {
                throw new RuntimeException("Senha incorreta.");
            }
        }

        return funcionario;
    }

    // 2. Se a senha for nula, vazia ou igual à matrícula, é o primeiro acesso
    public boolean isPrimeiroAcesso(Funcionario funcionario) {
        String senhaNoBanco = funcionario.getPass();
        
        // Retorna true se a senha no banco for nula, vazia ou se por acaso foi salva como a própria matrícula
        return senhaNoBanco == null || senhaNoBanco.trim().isEmpty() || senhaNoBanco.equals(funcionario.getMatricula());
    }

    // 3. Recebe a nova senha e a senha atual como trava de segurança
    public void trocarSenha(String matricula, String senhaAtual, String novaSenha) {
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findById(matricula);

        if (funcionarioOpt.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada.");
        }

        Funcionario funcionario = funcionarioOpt.get();

        // LÓGICA DE SEGURANÇA INTELIGENTE:
        // Se a senhaAtual foi enviada (fluxo normal), nós a validamos simulando um login.
        if (senhaAtual != null && !senhaAtual.trim().isEmpty()) {
            realizarLogin(matricula, senhaAtual); // Se a senha for falsa, este método já joga o erro na tela.
        } else {
            // Se a senhaAtual NÃO foi enviada (ex: veio da tela de Primeiro Acesso do Frontend)
            // Nós só permitimos a troca cega se o banco confirmar que realmente é o primeiro acesso dele.
            if (!isPrimeiroAcesso(funcionario)) {
                throw new RuntimeException("Acesso negado: Para alterar uma senha existente, é obrigatório informar a senha atual.");
            }
        }

        // Se passou pelas travas de segurança, atualiza no banco
        funcionario.setPass(novaSenha);
        funcionarioRepository.save(funcionario);
    }

    // 4. Implementação correta da busca avulsa
    public Funcionario buscarPorMatricula(String matricula) {
        return funcionarioRepository.findById(matricula)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado no sistema."));
    }
}