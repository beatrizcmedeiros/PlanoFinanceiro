package too.planofinanceiro.arquivo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import mos.reader.Line;
import too.planofinanceiro.dao.implementacao.CategoriaDaoJDBC;
import too.planofinanceiro.dao.implementacao.DespesaDaoJDBC;
import too.planofinanceiro.dao.implementacao.FormaDePagamentoDaoJDBC;
import too.planofinanceiro.dao.implementacao.OrcamentoDaoJDBC;
import too.planofinanceiro.db.DbException;
import too.planofinanceiro.entidades.Categoria;
import too.planofinanceiro.entidades.Despesa;
import too.planofinanceiro.entidades.FormaDePagamento;
import too.planofinanceiro.entidades.Orcamento;

public class ArquivoDespesa {
	
	public ArquivoDespesa() {
	}
	
	public void importarDadosDespesa(Connection conn, List<Line> linhasArquivo) {
		int aux = 0;
		for(Line l : linhasArquivo) {
			if(aux == 0) {
				aux = 1;
			}else {
				try {
					conn.setAutoCommit(false);
					
					Despesa novaDespesa = new Despesa();
					novaDespesa.setDescricao(l.getData(3));
					
					novaDespesa.setCodCategoria(tratamentoInformacaoCategoria(conn, l.getData(4)));
					
					salvarDadosBancoDespesa(conn, novaDespesa);
					
					Orcamento novoOrcamento = new Orcamento();
					novoOrcamento.setMesAno(novoOrcamento.formatarMesAno(l.getData(0), l.getData(1)));
					novoOrcamento.setCodDespesa(novaDespesa.getCodigo());
					novoOrcamento.setDataDespesa(novoOrcamento.formatarData(l.getData(0)));
					novoOrcamento.setDataPagamento(novoOrcamento.formatarDataPagamento(l.getData(1)));
					
					novoOrcamento.setCodPagamento(tratamentoInformacaoPagamento(conn, l.getData(2)));
					
					novoOrcamento.setValor(formatarNumero(l.getData(5)));
					
					if(l.getData(6) == null)
						novoOrcamento.setSituacao(null);
					else
						novoOrcamento.setSituacao(l.getData(6));
					salvarDadosBancoOrcamento(conn, novoOrcamento);
					
					conn.setAutoCommit(true);
				} catch (SQLException e) {
						throw new DbException(e.getMessage());
				}
			}
		}
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
		valor = valor.replace(",", ".");
		return Double.parseDouble(valor);
	}
	
}//class ArquivoDespesa
