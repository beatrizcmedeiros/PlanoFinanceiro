package too.planofinanceiro.entidades;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import mos.io.InputOutput;

public class Investimento implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int codigo;
	private String objetivo, estrategia, nome;
	private double valorInvestido, posicao, rendimentoBruto, rentabilidade;
	private Date vencimento;

	public Investimento() {
	}
	
	public Investimento(String objetivo, String estrategia, String nome, double valorInvestido, double posicao,
			double rendimentoBruto, String rentabilidade, String vencimento) {
		this.objetivo = objetivo;
		this.estrategia = estrategia;
		this.nome = nome;
		this.valorInvestido = valorInvestido;
		this.posicao = posicao;
		this.rendimentoBruto = rendimentoBruto;
		this.rentabilidade = formatarRentabilidade(rentabilidade);
		this.vencimento = formatarDataVencimento(vencimento);
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getValorInvestido() {
		return valorInvestido;
	}

	public void setValorInvestido(double valorInvestido) {
		this.valorInvestido = valorInvestido;
	}

	public double getPosicao() {
		return posicao;
	}

	public void setPosicao(double posicao) {
		this.posicao = posicao;
	}

	public double getRendimentoBruto() {
		return rendimentoBruto;
	}

	public void setRendimentoBruto(double rendimentoBruto) {
		this.rendimentoBruto = rendimentoBruto;
	}

	public double getRentabilidade() {
		return rentabilidade;
	}

	public void setRentabilidade(double rentabilidade) {
		this.rentabilidade = rentabilidade;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

	public double formatarRentabilidade(String rentabilidade) {
		rentabilidade = rentabilidade.replace(",", ".");
	    String[] aux = rentabilidade.split("%");
	    double valorRentabilidade = Double.parseDouble(aux[0]);
	    return Math.round(valorRentabilidade * 100.0) / 100.0;
	}
	
	public Date formatarDataVencimento(String vencimento) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	    
	    try {
	        java.util.Date utilDate = formato.parse(vencimento);
	        long milliseconds = utilDate.getTime();
	        return new java.sql.Date(milliseconds);
	    } catch (ParseException e) {
	    	InputOutput.showError("Erro ao formatar data de vencimento.", "Investimento");
	    }
		return null;
	}
	
	public String formatarData(Date data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

	    try {
	        String vencimentoString = formato.format(data);
	        return vencimentoString;
	    } catch (Exception e) {
	    	InputOutput.showError("Erro ao formatar data.", "Investimento: Formata Data");
	    }
	    return null;
	}	
}//class Investimento
