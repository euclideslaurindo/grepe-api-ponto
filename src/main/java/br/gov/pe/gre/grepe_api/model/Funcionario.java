package br.gov.pe.gre.grepe_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="funcionarios")
public class Funcionario {
    @Id
    @Column(name = "matricula", length =8, unique = true, nullable=false)
    private String matricula;
    
    @Column( name = "nome", length = 60)
    private String nome;
    
    @Column(name = "nomeAux")
    private String nomeAux;
    
    @Column (name = "departamento", length = 20)
    private String departamento;
    
    @Column (name = "funcao", length = 20)
    private String funcao;
    
    @Column (name = "ch", length = 3)
    private String ch;
    
    @Column (name = "hora1", length = 5)
    private String hora1;
    
    @Column (name = "hora2", length =5)
    private String hora2;
    
    @Column (name ="hora3", length = 5)
    private String hora3;
    
    @Column (name ="hora4", length=5)
    private String hora4;
    
    @Column (name="hora5", length =5)
    private String hora5;
    
    @Column (name= "hora6", length =5)
    private String hora6;
    
    @Column(name = "pass")
    private String pass;
    
    public Funcionario (){       
    }
    
    public String getMatricula(){
        return matricula;
    }
    
    public void setMatricula(String matricula){
        this.matricula = matricula;     
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeAux() {
        return nomeAux;
    }

    public void setNomeAux(String nomeAux) {
        this.nomeAux = nomeAux;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getHora1() {
        return hora1;
    }

    public void setHora1(String hora1) {
        this.hora1 = hora1;
    }

    public String getHora2() {
        return hora2;
    }

    public void setHora2(String hora2) {
        this.hora2 = hora2;
    }

    public String getHora3() {
        return hora3;
    }

    public void setHora3(String hora3) {
        this.hora3 = hora3;
    }

    public String getHora4() {
        return hora4;
    }

    public void setHora4(String hora4) {
        this.hora4 = hora4;
    }

    public String getHora5() {
        return hora5;
    }

    public void setHora5(String hora5) {
        this.hora5 = hora5;
    }

    public String getHora6() {
        return hora6;
    }

    public void setHora6(String hora6) {
        this.hora6 = hora6;
    }
    
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}