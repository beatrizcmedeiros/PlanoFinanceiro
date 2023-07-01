package too.planofinanceiro.entidades;

import java.io.Serializable;

public class Despesa implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int codigo;
	private String descricao;
	private int codCategoria;
	
	public Despesa() {
	}

	public Despesa(String descricao, int codCategoria) {
		this.descricao = descricao;
		this.codCategoria = codCategoria;
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

	public int getCodCategoria() {
		return codCategoria;
	}

	public void setCodCategoria(int codCategoria) {
		this.codCategoria = codCategoria;
	}
}//class Despesa
