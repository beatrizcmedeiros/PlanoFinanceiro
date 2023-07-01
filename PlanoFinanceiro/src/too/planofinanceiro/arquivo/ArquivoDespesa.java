package too.planofinanceiro.arquivo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mos.io.InputOutput;
import mos.reader.Line;
import too.planofinanceiro.dao.implementacao.CategoriaDaoJDBC;
import too.planofinanceiro.dao.implementacao.DespesaDaoJDBC;
import too.planofinanceiro.dao.implementacao.FormaDePagamentoDaoJDBC;
import too.planofinanceiro.dao.implementacao.OrcamentoDaoJDBC;
import too.planofinanceiro.entidades.Categoria;
import too.planofinanceiro.entidades.Despesa;
import too.planofinanceiro.entidades.FormaDePagamento;
import too.planofinanceiro.entidades.Orcamento;
import too.planofinanceiro.util.ValidacoesRegex;

public class ArquivoDespesa {
	
	List<String> linhasError;
	List<Integer> numLinhaError;
	
	public ArquivoDespesa() {
		linhasError = new ArrayList<String>();
		numLinhaError = new ArrayList<Integer>();
	}
	
	public void importarDadosDespesa(Connection conn, List<Line> linhasArquivo) {
		int aux = 0, count = 2;
		for(Line l : linhasArquivo) {
			if(aux == 0) {
				aux = 1;
			}else {
				try {
					conn.setAutoCommit(false);
					
					Despesa novaDespesa = new Despesa();
					novaDespesa = validarDadosDespesa(conn, l);
					if(novaDespesa != null) {
						salvarDadosBancoDespesa(conn, novaDespesa);
						
						Orcamento novoOrcamento = new Orcamento();
						novoOrcamento = validarDadosOrcamento(conn, l, novaDespesa.getCodigo());
						if(novoOrcamento != null) { 						
							salvarDadosBancoOrcamento(conn, novoOrcamento);
							conn.setAutoCommit(true);
						}else
							numLinhaError.add(count);
					}else
						numLinhaError.add(count);
					
					count++;
				} catch (SQLException e) {
						InputOutput.showError(e.getMessage(), "Importar Dados da Despesa");
				}
			}
		}
		
		if(linhasError.size() > 0) {
			int[] array = new int[numLinhaError.size()];
	        for (int i = 0; i < numLinhaError.size(); i++) {
	            array[i] = numLinhaError.get(i);
	        }
	        
			StringBuilder sb = new StringBuilder();
			int i = 0;
			
			sb.append("ERRO NA IMPORTAÇÃO NA(S) LINHA(S) \n");
			for(String s : linhasError) {
				sb.append(String.format("%d:  ", array[i++]));
				sb.append(s);
				sb.append("\n");
			}
			
			InputOutput.showError(sb.toString(), "Arquivo Despesa");
		}
	}
	
	private Despesa validarDadosDespesa(Connection conn, Line linha) {
		Despesa despesa = new Despesa();
		String descricao = linha.getData(3);
		String categoria = linha.getData(4);
		boolean error = false;
		
		if(ValidacoesRegex.validarStringComNumero(descricao) == true)
			despesa.setDescricao(linha.getData(3));
		else
			error = true;
		if(ValidacoesRegex.validarString(categoria) == true)
			despesa.setCodCategoria(tratamentoInformacaoCategoria(conn, categoria));
		else {
			if(error == false)
				error = true;
		}
		
		if(error == true) {
			StringBuilder sb = new StringBuilder();
			for(String s : linha.getLine()) 
				sb.append(s + ";");
			linhasError.add(sb.toString());
			return null;
		}
		
		return despesa;
	}
	
	private Orcamento validarDadosOrcamento(Connection conn, Line linha, int codigoDespesa) {
		Orcamento orcamento = new Orcamento();
		orcamento.setCodDespesa(codigoDespesa);
		boolean error = false;
		
		if(ValidacoesRegex.validarData(linha.getData(0)) == true)
			orcamento.setDataDespesa(orcamento.formatarData(linha.getData(0)));
		else
			error = true;
		
		if(ValidacoesRegex.validarDataDiaMes(linha.getData(1)) == true)
			orcamento.setDataPagamento(orcamento.formatarDataPagamento(linha.getData(1)));
		else {
			if(error == false)
				error = true;
		}
		
		if(error == false)
			orcamento.setMesAno(orcamento.formatarMesAno(linha.getData(0), linha.getData(1)));
		
		if(ValidacoesRegex.validarString(linha.getData(2)) == true)
			orcamento.setCodPagamento(tratamentoInformacaoPagamento(conn, linha.getData(2)));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarDouble(linha.getData(5)) == true)
			orcamento.setValor(formatarNumero(linha.getData(5)));
		else {
			if(error == false)
				error = true;
		}
		
		if(linha.getData(6) == null)
			orcamento.setSituacao(null);
		else {
			if(ValidacoesRegex.validarString(linha.getData(6)) == true)
				orcamento.setSituacao(linha.getData(6));
			else {
				if(error == false)
					error = true;
			}
		}
		
		if(error == true) {
			StringBuilder sb = new StringBuilder();
			for(String s : linha.getLine()) 
				sb.append(s + ";");
			linhasError.add(sb.toString());
			return null;
		}
		
		return orcamento;
			
	}


	public void salvarDadosBancoDespesa(Connection conn, Despesa despesa) {
		DespesaDaoJDBC novaDespesaDao = new DespesaDaoJDBC(conn);
		novaDespesaDao.insere(despesa);
	}
	
	public void salvarDadosBancoOrcamento(Connection conn, Orcamento orcamento) {
		OrcamentoDaoJDBC novoOrcamentoDao = new OrcamentoDaoJDBC(conn);
		novoOrcamentoDao.insere(orcamento);
	}
	
	public int tratamentoInformacaoCategoria(Connection conn, String categoria) {
		CategoriaDaoJDBC categoriaDao = new CategoriaDaoJDBC(conn);
		
		if(categoriaDao.buscaPorDescricao(categoria) != null) 
			return categoriaDao.buscaPorDescricao(categoria).getCodigo();
		
		Categoria novaCategoria = new Categoria(categoria);
		categoriaDao.insere(novaCategoria);
		
		return novaCategoria.getCodigo();
	}
	
	public int tratamentoInformacaoPagamento(Connection conn, String formaDePagamento) {
		FormaDePagamentoDaoJDBC formaDePagamentoDao = new FormaDePagamentoDaoJDBC(conn);
		
		if(formaDePagamentoDao.buscaPorDescricao(formaDePagamento) != null) 
			return formaDePagamentoDao.buscaPorDescricao(formaDePagamento).getCodigo();
		
		FormaDePagamento novaformaDePagamento = new FormaDePagamento(formaDePagamento);
		formaDePagamentoDao.insere(novaformaDePagamento);
		
		return formaDePagamentoDao.buscaPorDescricao(formaDePagamento).getCodigo();
	}
	
	public double formatarNumero(String valor) {
		if(valor.contains(".") || valor.contains(",")) {
			if(valor.contains("."))
				valor = valor.replace(".", "").replace(",", ".");
			else
				valor = valor.replace(",", ".");
		}
		return Double.parseDouble(valor);
	}
	
}//class ArquivoDespesa
