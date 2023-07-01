package too.planofinanceiro.arquivo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mos.io.InputOutput;
import mos.reader.Line;
import too.planofinanceiro.dao.implementacao.RendaDaoJDBC;
import too.planofinanceiro.dao.implementacao.RendaMensalDaoJDBC;
import too.planofinanceiro.entidades.Renda;
import too.planofinanceiro.entidades.RendaMensal;
import too.planofinanceiro.util.ValidacoesRegex;

public class ArquivoReceita {
	
	List<String> linhasError;
	List<Integer> numLinhaError;
	
	public ArquivoReceita() {
		linhasError = new ArrayList<String>();
		numLinhaError = new ArrayList<Integer>();
	}
	
	public void importarDadosReceita(Connection conn, List<Line> linhasArquivo) {
		int aux = 0, count = 2;
		for(Line l : linhasArquivo) {
			if(aux == 0) {
				aux = 1;
			}else {
				try {
					conn.setAutoCommit(false);
					
					RendaMensal novaRendaMensal = new RendaMensal();
					novaRendaMensal = validarDadosRendaMensal(conn, l);
					if(novaRendaMensal != null) {
						salvarDadosBancoRendaMensal(conn, novaRendaMensal);
						conn.setAutoCommit(true);
					}else
						numLinhaError.add(count);
					
					count++;
				} catch (SQLException e) {
					InputOutput.showError(e.getMessage(), "Importar Dados da Receita");
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
			
			InputOutput.showError(sb.toString(), "Arquivo Receita");
		}
	}
	
	private RendaMensal validarDadosRendaMensal(Connection conn, Line linha) {
		RendaMensal rendaMensal = new RendaMensal();
		boolean error = false;
		
		if(ValidacoesRegex.validarString(linha.getData(0)) == true) {
			rendaMensal.setCodRenda(tratamentoInformacaoRenda(conn, linha.getData(0)));
		}
		
		if(ValidacoesRegex.validarData(linha.getData(1)) == true)
			rendaMensal.setData(rendaMensal.formatarData(linha.getData(1)));
		else 
			error = true;
		
		if(ValidacoesRegex.validarDouble(linha.getData(2)) == true)
			rendaMensal.setValor(Double.parseDouble(linha.getData(2)));
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
		
		return rendaMensal;
	}
	
	public void salvarDadosBancoRendaMensal(Connection conn, RendaMensal rendaMensal) {
		RendaMensalDaoJDBC novaRendaMensalDao = new RendaMensalDaoJDBC(conn);
		novaRendaMensalDao.insere(rendaMensal);
	}
	
	public int tratamentoInformacaoRenda(Connection conn, String renda) {
		RendaDaoJDBC rendaDao = new RendaDaoJDBC(conn);
		
		if(rendaDao.buscaPorDescricao(renda) != null) 
			return rendaDao.buscaPorDescricao(renda).getCodigo();
		
		Renda novaRenda = new Renda(renda);
		rendaDao.insere(novaRenda);
		
		return rendaDao.buscaPorDescricao(renda).getCodigo();
	}
}//class ArquivoReceita
