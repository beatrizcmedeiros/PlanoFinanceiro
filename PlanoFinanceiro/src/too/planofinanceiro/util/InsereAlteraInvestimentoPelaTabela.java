package too.planofinanceiro.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import mos.io.InputOutput;
import too.planofinanceiro.dao.implementacao.InvestimentoDaoJDBC;
import too.planofinanceiro.entidades.Investimento;

public class InsereAlteraInvestimentoPelaTabela {
		
	public static void importarDadosInvestimento(Connection conn, List<String> dados) {
			try {
				conn.setAutoCommit(false);
				
				Investimento novoInvestimento = new Investimento();
				novoInvestimento = validarDadosInvestimento(conn, dados);
				if(novoInvestimento != null) {
					salvarDadosBancoInvestimento(conn, novoInvestimento);
					conn.setAutoCommit(true);
				}else
					InputOutput.showError("Verifique se os dados estão corretos.", "Inserir Investimento");
			} catch (SQLException e) {
				InputOutput.showError(e.getMessage(), "Importar Dados de Investimento");
			}		
	}
	
	public static void alterarDadosInvestimento(Connection conn, List<String> dadosAntigos, List<String> dadosNovos) {
		try {
			conn.setAutoCommit(false);
			Investimento investimento = new Investimento();
			investimento = validarDadosInvestimento(conn, dadosAntigos);
			
			Investimento novoInvestimento = new Investimento();
			novoInvestimento = validarDadosInvestimento(conn, dadosNovos);
			
			if(novoInvestimento != null) {
				alterarDadosBancoInvestimento(conn, investimento, novoInvestimento);
				conn.setAutoCommit(true);
			}else
				InputOutput.showError("Verifique se os dados estão corretos.", "Inserir Investimento");
		} catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Importar Dados de Investimento");
		}		
	}
	
	private static Investimento validarDadosInvestimento(Connection conn, List<String> dados) {
		Investimento investimento = new Investimento();
		boolean error = false;
		
		if(ValidacoesRegex.validarString(dados.get(0)) == true)
			investimento.setObjetivo(dados.get(0));
		else
			error = true;
		
		if(ValidacoesRegex.validarString(dados.get(1)) == true)
			investimento.setEstrategia(dados.get(1));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarStringComNumero(dados.get(2)) == true)
			investimento.setNome(dados.get(2));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarDouble(dados.get(3)) == true)
			investimento.setValorInvestido(formatarNumero(dados.get(3)));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarDouble(dados.get(4)) == true)
			investimento.setPosicao(formatarNumero(dados.get(4)));
		else {
			if(error == false)
				error = true;
		}
		if(ValidacoesRegex.validarDouble(dados.get(5)) == true)
			investimento.setRendimentoBruto(formatarNumero(dados.get(5)));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarDouble(dados.get(6)) == true)
			investimento.setRentabilidade(investimento.formatarRentabilidade(dados.get(6)));
		else {
			if(error == false)
				error = true;
		}
		
		if(ValidacoesRegex.validarData(dados.get(7)) == true)
			investimento.setVencimento(investimento.formatarDataVencimento(dados.get(7)));
		else {
			if(error == false)
				error = true;
		}
		
		if(error == true)
			return null;
		
		return investimento;
	}

	public static void salvarDadosBancoInvestimento(Connection conn, Investimento investimento) {
		InvestimentoDaoJDBC novoInvestimentoDao = new InvestimentoDaoJDBC(conn);
		novoInvestimentoDao.insere(investimento);
	}
	
	public static void alterarDadosBancoInvestimento(Connection conn, Investimento investimento, Investimento novoInvestimento) {
		InvestimentoDaoJDBC novoInvestimentoDao = new InvestimentoDaoJDBC(conn);
		novoInvestimentoDao.atualiza(investimento, novoInvestimento);;
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
}//class InsercaoInvestimentoPelaTabela
