package too.planofinanceiro.arquivo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import mos.reader.Line;
import too.planofinanceiro.dao.implementacao.RendaDaoJDBC;
import too.planofinanceiro.dao.implementacao.RendaMensalDaoJDBC;
import too.planofinanceiro.db.DbException;
import too.planofinanceiro.entidades.Renda;
import too.planofinanceiro.entidades.RendaMensal;

public class ArquivoReceita {
	
	public ArquivoReceita() {
	}
	
	public void importarDadosReceita(Connection conn, List<Line> linhasArquivo) {
		int aux = 0;
		for(Line l : linhasArquivo) {
			if(aux == 0) {
				aux = 1;
			}else {
				try {
					conn.setAutoCommit(false);
					
					Renda novaRenda = new Renda();
					novaRenda.setDescricao(l.getData(0));
					salvarDadosBancoRenda(conn, novaRenda);
					
					RendaMensal novaRendaMensal = new RendaMensal();
					novaRendaMensal.setCodRenda(novaRenda.getCodigo());
					novaRendaMensal.setData(novaRendaMensal.formatarData(l.getData(1)));
					novaRendaMensal.setValor(Double.parseDouble(l.getData(2)));
					salvarDadosBancoRendaMensal(conn, novaRendaMensal);
					
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			}
		}
	}
	
	public void salvarDadosBancoRenda(Connection conn, Renda renda) {
		RendaDaoJDBC novaRendaDao = new RendaDaoJDBC(conn);
		novaRendaDao.insere(renda);
	}
	
	public void salvarDadosBancoRendaMensal(Connection conn, RendaMensal rendaMensal) {
		RendaMensalDaoJDBC novaRendaMensalDao = new RendaMensalDaoJDBC(conn);
		novaRendaMensalDao.insere(rendaMensal);
	}
}//class ArquivoReceita
