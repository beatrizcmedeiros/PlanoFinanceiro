package too.planofinanceiro.dao;

import too.planofinanceiro.dao.implementacao.DespesaDaoJDBC;
import too.planofinanceiro.dao.implementacao.InvestimentoDaoJDBC;
import too.planofinanceiro.dao.implementacao.RendaDaoJDBC;
import too.planofinanceiro.dao.implementacao.RendaMensalDaoJDBC;
import too.planofinanceiro.db.DB;

public class DaoFactory {
	
	public static Dao<?> createDespesaDao() {
		return new DespesaDaoJDBC(DB.getConnection());
	}
	
	public static Dao<?> createInvestimentoDao() {
		return new InvestimentoDaoJDBC(DB.getConnection());
	}
	
	public static Dao<?> createRendaDao() {
		return new RendaDaoJDBC(DB.getConnection());
	}
	
	public static Dao<?> createRendaMensalDao() {
		return new RendaMensalDaoJDBC(DB.getConnection());
	}
	
}//class DaoFactory
