package too.planofinanceiro.entidades;

import java.io.Serializable;

public class Renda implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int codigo;
	private String descricao;
	
	public Renda() {
	}

	public Renda(String descricao) {
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}//class Renda
