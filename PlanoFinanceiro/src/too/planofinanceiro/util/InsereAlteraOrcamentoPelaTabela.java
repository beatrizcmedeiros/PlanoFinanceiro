package too.planofinanceiro.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import mos.io.InputOutput;
import too.planofinanceiro.dao.implementacao.CategoriaDaoJDBC;
import too.planofinanceiro.dao.implementacao.DespesaDaoJDBC;
import too.planofinanceiro.dao.implementacao.FormaDePagamentoDaoJDBC;
import too.planofinanceiro.dao.implementacao.OrcamentoDaoJDBC;
import too.planofinanceiro.entidades.Categoria;
import too.planofinanceiro.entidades.Despesa;
import too.planofinanceiro.entidades.FormaDePagamento;
import too.planofinanceiro.entidades.Orcamento;

public class InsereAlteraOrcamentoPelaTabela {
	
	public static void importarDadosDespesa(Connection conn, List<String> dados) {
		try {
			conn.setAutoCommit(false);
			
			Despesa novaDespesa = new Despesa();
			novaDespesa = validarDadosDespesa(conn, dados);
			if(novaDespesa != null) {
				salvarDadosBancoDespesa(conn, novaDespesa);
				
				Orcamento novoOrcamento = new Orcamento();
				novoOrcamento = validarDadosOrcamento(conn, dados, novaDespesa.getCodigo());
				if(novoOrcamento != null) { 						
					salvarDadosBancoOrcamento(conn, novoOrcamento);
					conn.setAutoCommit(true);
				}else
					InputOutput.showError("Verifique se os dados estão corretos.", "Inserir Despesa");
			}else
				InputOutput.showError("Verifique se os dados estão corretos.", "Inserir Despesa");
			
		} catch (SQLException e) {
				InputOutput.showError(e.getMessage(), "Importar Dados da Despesa");
		}
	}
	
	public static void alterarDadosInvestimento(Connection conn, List<String> dadosNovos) {
		try {
			conn.setAutoCommit(false);
			
			DespesaDaoJDBC despesaDao = new DespesaDaoJDBC(conn);			
			Despesa despesaNova = despesaDao.buscaPorDescricao(dadosNovos.get(3));
			
			if(despesaNova == null) {
				despesaNova = validarDadosDespesa(conn, dadosNovos);
			}
			
			Orcamento novoOrcamento = new Orcamento();
			novoOrcamento = validarDadosOrcamento(conn, dadosNovos, despesaNova.getCodigo());
			
			if(novoOrcamento != null) {
				alterarDadosBancoOrcamento(conn, novoOrcamento);
				conn.setAutoCommit(true);
			}else
				InputOutput.showError("Verifique se os dados estão corretos.", "Inserir Orçamento");
		} catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Importar Dados de Orçamento");
		}		
}
	
	private static Despesa validarDadosDespesa(Connection conn, List<String> dados) {
		Despesa despesa = new Despesa();
		String descricao = dados.get(3);
		String categoria = dados.get(4);
		boolean error = false;
		
		if(ValidacoesRegex.validarStringComNumero(descricao) == true)
			despesa.setDescricao(dados.get(3));
		else
			error = true;
		if(ValidacoesRegex.validarString(categoria) == true)
			despesa.setCodCategoria(tratamentoInformacaoCategoria(conn, categoria));
		else {
			if(error == false)
				error = true;
		}		
		
		if(error == true)
			return null;
		
		return despesa;
	}
	
	private static Orcamento validarDadosOrcamento(Connection conn, List<String> dados, int codigoDespesa) {
		Orcamento orcamento = new Orcamento();
		orcamento.setCodDespesa(codigoDespesa);
		orcamento.setSituacao(dados.get(6));
		boolean error = false;
		
		if(ValidacoesRegex.validarData(dados.get(0)) == true)
			orcamento.setDataDespesa(orcamento.formatarData(dados.get(0)));
		else
			error = true;
		
		if(ValidacoesRegex.validarDataDiaMes(dados.get(1)) == true)
			orcamento.setDataPagamento(orcamento.formatarDataPagamento(dados.get(1)));
		else {
			if(error == false)
				error = true;
		}
		
		if(error == false)
			orcamento.setMesAno(orcamento.formatarMesAno(dados.get(0), dados.get(1)));
		
		if(ValidacoesRegex.validarString(dados.get(2)) == true)
			orcamento.setCodPagamento(tratamentoInformacaoPagamento(conn, dados.get(2)));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarDouble(dados.get(5)) == true)
			orcamento.setValor(formatarNumero(dados.get(5)));
		else {
			if(error == false)
				error = true;
		}
		
		if(error == true)
			return null;
		return orcamento;
	}


	public static void salvarDadosBancoDespesa(Connection conn, Despesa despesa) {
		DespesaDaoJDBC novaDespesaDao = new DespesaDaoJDBC(conn);
		novaDespesaDao.insere(despesa);
	}
	
	public static void salvarDadosBancoOrcamento(Connection conn, Orcamento orcamento) {
		OrcamentoDaoJDBC novoOrcamentoDao = new OrcamentoDaoJDBC(conn);
		novoOrcamentoDao.insere(orcamento);
	}
	
	public static void alterarDadosBancoOrcamento(Connection conn, Orcamento novoOrcamento) {
		OrcamentoDaoJDBC novoOrcamentoDao = new OrcamentoDaoJDBC(conn);
		novoOrcamentoDao.atualiza(novoOrcamento);
	}
	
	public static int tratamentoInformacaoCategoria(Connection conn, String categoria) {
		CategoriaDaoJDBC categoriaDao = new CategoriaDaoJDBC(conn);
		
		if(categoriaDao.buscaPorDescricao(categoria) != null) 
			return categoriaDao.buscaPorDescricao(categoria).getCodigo();
		
		Categoria novaCategoria = new Categoria(categoria);
		categoriaDao.insere(novaCategoria);
		
		return novaCategoria.getCodigo();
	}
	
	public static int tratamentoInformacaoPagamento(Connection conn, String formaDePagamento) {
		FormaDePagamentoDaoJDBC formaDePagamentoDao = new FormaDePagamentoDaoJDBC(conn);
		
		if(formaDePagamentoDao.buscaPorDescricao(formaDePagamento) != null) 
			return formaDePagamentoDao.buscaPorDescricao(formaDePagamento).getCodigo();
		
		FormaDePagamento novaformaDePagamento = new FormaDePagamento(formaDePagamento);
		formaDePagamentoDao.insere(novaformaDePagamento);
		
		return formaDePagamentoDao.buscaPorDescricao(formaDePagamento).getCodigo();
	}
	
	public static double formatarNumero(String valor) {
		if(valor.contains(".") || valor.contains(",")) {
			if(valor.contains("."))
				valor = valor.replace(".", "").replace(",", ".");
			else
				valor = valor.replace(",", ".");
		}
		return Double.parseDouble(valor);
	}
	
}
