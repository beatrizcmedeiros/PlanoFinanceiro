package too.planofinanceiro.arquivo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mos.io.InputOutput;
import mos.reader.Line;
import too.planofinanceiro.dao.implementacao.InvestimentoDaoJDBC;
import too.planofinanceiro.entidades.Investimento;
import too.planofinanceiro.util.ValidacoesRegex;

public class ArquivoInvestimento {
	
	List<String> linhasError;
	List<Integer> numLinhaError;
	
	public ArquivoInvestimento() {
		linhasError = new ArrayList<String>();
		numLinhaError = new ArrayList<Integer>();
	}
	
	public void importarDadosInvestimento(Connection conn, List<Line> linhasArquivo) {
		int aux = 0, count = 2;
		for(Line l : linhasArquivo) {
			if(aux == 0) {
				aux = 1;
			}else {
				try {
					conn.setAutoCommit(false);
					
					Investimento novoInvestimento = new Investimento();
					novoInvestimento = validarDadosInvestimento(conn, l);
					if(novoInvestimento != null) {
						salvarDadosBancoInvestimento(conn, novoInvestimento);
						conn.setAutoCommit(true);
					}else
						numLinhaError.add(count);
					
					count++;
					
					
				} catch (SQLException e) {
					InputOutput.showError(e.getMessage(), "Importar Dados de Investimento");
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
			
			InputOutput.showError(sb.toString(), "Arquivo Investimento");
		}
	}
	
	private Investimento validarDadosInvestimento(Connection conn, Line linha) {
		Investimento investimento = new Investimento();
		boolean error = false;
		
		if(ValidacoesRegex.validarString(linha.getData(0)) == true)
			investimento.setObjetivo(linha.getData(0));
		else
			error = true;
		
		if(ValidacoesRegex.validarString(linha.getData(1)) == true)
			investimento.setEstrategia(linha.getData(1));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarStringComNumero(linha.getData(2)) == true)
			investimento.setNome(linha.getData(2));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarDouble(linha.getData(3)) == true)
			investimento.setValorInvestido(formatarNumero(linha.getData(3)));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarDouble(linha.getData(4)) == true)
			investimento.setPosicao(formatarNumero(linha.getData(4)));
		else {
			if(error == false)
				error = true;
		}
		if(ValidacoesRegex.validarDouble(linha.getData(5)) == true)
			investimento.setRendimentoBruto(formatarNumero(linha.getData(5)));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarDouble(linha.getData(6)) == true)
			investimento.setRentabilidade(investimento.formatarRentabilidade(linha.getData(6)));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarData(linha.getData(7)) == true)
			investimento.setVencimento(investimento.formatarDataVencimento(linha.getData(7)));
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
		return investimento;
	}

	public void salvarDadosBancoInvestimento(Connection conn, Investimento investimento) {
		InvestimentoDaoJDBC novoInvestimentoDao = new InvestimentoDaoJDBC(conn);
		novoInvestimentoDao.insere(investimento);
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
}//class ArquivoInvestimento
