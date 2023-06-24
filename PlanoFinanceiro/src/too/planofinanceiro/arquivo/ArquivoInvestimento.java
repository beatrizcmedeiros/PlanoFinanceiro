package too.planofinanceiro.arquivo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import mos.reader.Line;
import too.planofinanceiro.dao.implementacao.InvestimentoDaoJDBC;
import too.planofinanceiro.db.DbException;
import too.planofinanceiro.entidades.Investimento;

public class ArquivoInvestimento {
	
	public ArquivoInvestimento() {
	}
	
	public void importarDadosInvestimento(Connection conn, List<Line> linhasArquivo) {
		int aux = 0;
		for(Line l : linhasArquivo) {
			if(aux == 0) {
				aux = 1;
			}else {
				try {
					conn.setAutoCommit(false);
					
					Investimento novoInvestimento = new Investimento();
					
					novoInvestimento.setObjetivo(l.getData(0));
					novoInvestimento.setEstrategia(l.getData(1));
					novoInvestimento.setNome(l.getData(2));
					novoInvestimento.setValorInvestido(formatarNumero(l.getData(3)));
					novoInvestimento.setPosicao(formatarNumero(l.getData(4)));
					novoInvestimento.setRendimentoBruto(formatarNumero(l.getData(5)));
					novoInvestimento.setRentabilidade(novoInvestimento.formatarRentabilidade(l.getData(6)));
					novoInvestimento.setVencimento(novoInvestimento.formatarDataVencimento(l.getData(7)));
					
					salvarDadosBancoInvestimento(conn, novoInvestimento);
					
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			}
		}
	}
	
	public void salvarDadosBancoInvestimento(Connection conn, Investimento investimento) {
		InvestimentoDaoJDBC novoInvestimentoDao = new InvestimentoDaoJDBC(conn);
		novoInvestimentoDao.insere(investimento);
	}
	
	public double formatarNumero(String valor) {
		if(valor.contains(".")) {
		 valor = valor.replace(".", "").replace(",", ".");
		}
		valor = valor.replace(",", ".");
		return Double.parseDouble(valor);
	}
}//class ArquivoInvestimento
