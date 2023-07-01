package too.planofinanceiro.entidades;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import mos.io.InputOutput;

public class Orcamento implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String mesAno;
	private int codDespesa;
	private Date dataDespesa, dataPagamento;
	private int codPagamento;
	private double valor;
	private String situacao;

	public Orcamento() {
	}

	public Orcamento(int codDespesa, String dataDespesa, String dataPagamento, int codPagamento,
			double valor, String situacao) {
		this.mesAno = formatarMesAno(dataDespesa, dataPagamento);
		this.codDespesa = codDespesa;
		this.dataDespesa = formatarData(dataDespesa);
		this.dataPagamento = formatarDataPagamento(dataPagamento);
		this.codPagamento = codPagamento;
		this.valor = valor;
		this.situacao = situacao;
	}
	
	public String getMesAno() {
		return mesAno;
	}

	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}

	public int getCodDespesa() {
		return codDespesa;
	}

	public void setCodDespesa(int codDespesa) {
		this.codDespesa = codDespesa;
	}

	public Date getDataDespesa() {
		return dataDespesa;
	}

	public void setDataDespesa(Date dataDespesa) {
		this.dataDespesa = dataDespesa;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public int getCodPagamento() {
		return codPagamento;
	}

	public void setCodPagamento(int codPagamento) {
		this.codPagamento = codPagamento;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public java.sql.Date formatarData(String data) {
	    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	   
	    try {
	        java.util.Date utilDate = formato.parse(data);
	        long milliseconds = utilDate.getTime();
	        return new java.sql.Date(milliseconds);
	    } catch (ParseException e) {
	    	InputOutput.showError("Erro ao formatar data.", "Orçamento");
	    }
	    return null;
	}
	
	public String formatarMesAno(String dataDespesa, String dataPagamento) {
		String[] data = dataPagamento.split("/");
		int mes = Integer.parseInt(data[1]);
		String dataPagamentoAux = data[1] + "/" + getAno(dataDespesa, mes);
		
        return dataPagamentoAux;
	}
	
	public int getAno(String dataDespesa, int mesPagamento) {
		String[] data = dataDespesa.split("/");
		int mesDespesa = Integer.parseInt(data[1]);
		
		Calendar calendario = Calendar.getInstance();
		if (mesPagamento > mesDespesa) 
	        return calendario.get(Calendar.YEAR);
		
		calendario.add(Calendar.YEAR, 0);
        return calendario.get(Calendar.YEAR);
	}
	
	public java.sql.Date formatarDataPagamento(String dataPagamento) {		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM");
		Calendar cal = Calendar.getInstance();
			    
		try {
		    java.util.Date utilDate = formato.parse(dataPagamento);
		    cal.setTime(utilDate);
		    cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
		    long milliseconds = cal.getTimeInMillis();
		    return new java.sql.Date(milliseconds);
		} catch (ParseException e) {
			InputOutput.showError("Erro ao formatar data de pagamento.", "Orçamento");
		}
		return null;
	}	
	
}//class Orcamento
