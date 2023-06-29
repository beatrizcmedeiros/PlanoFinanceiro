package too.planofinanceiro.entidades;

import java.sql.Date;

public class TabelaOrcamento {
	private Date data;
	private int dia;
	private String tipo;
	private String descricao;
	private double valor;
	private boolean paga;
	
	public TabelaOrcamento() {
	}

	public TabelaOrcamento(Date data, int dia, String tipo, String descricao, double valor, boolean paga) {
		this.data = data;
		this.dia = dia;
		this.tipo = tipo;
		this.descricao = descricao;
		this.valor = valor;
		this.paga = paga;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public boolean isPaga() {
		return paga;
	}

	public void setPaga(boolean paga) {
		this.paga = paga;
	}
}
