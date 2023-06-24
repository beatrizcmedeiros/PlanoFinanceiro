package too.planofinanceiro.entidades;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RendaMensal implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int codRenda;
	private Date data;
	private double valor;

	public RendaMensal() {
	}

	public RendaMensal(int cod_renda, Date data, double valor) {
		this.codRenda = cod_renda;
		this.data = data;
		this.valor = valor;
	}

	public int getCodRenda() {
		return codRenda;
	}

	public void setCodRenda(int cod_renda) {
		this.codRenda = cod_renda;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public java.sql.Date formatarData(String data) {
	    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	   
	    try {
	        java.util.Date utilDate = formato.parse(data);
	        long milliseconds = utilDate.getTime();
	        return new java.sql.Date(milliseconds);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}//class RendaMensal
