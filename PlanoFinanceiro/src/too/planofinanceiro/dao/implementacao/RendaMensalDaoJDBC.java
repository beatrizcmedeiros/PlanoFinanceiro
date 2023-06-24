package too.planofinanceiro.dao.implementacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import too.planofinanceiro.dao.Dao;
import too.planofinanceiro.db.DB;
import too.planofinanceiro.db.DbException;
import too.planofinanceiro.entidades.RendaMensal;

public class RendaMensalDaoJDBC implements Dao<RendaMensal>{

	private Connection conn;

	public RendaMensalDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insere(RendaMensal rendaMensal) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO renda_mensal"
					+ "(cod_renda, data, valor)"
					+ "	VALUES ((SELECT codigo FROM renda WHERE codigo=?),?, ?);");
			
			st.setInt(1, rendaMensal.getCodRenda());
			st.setDate(2, rendaMensal.getData());
			st.setDouble(3, rendaMensal.getValor());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void atualiza(RendaMensal rendaMensal) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE renda_mensal"
					+ "	SET valor=?"
					+ "	WHERE cod_renda=?"
					+ " AND data=?;");
			
			st.setDouble(1, rendaMensal.getValor());
			st.setInt(2, rendaMensal.getCodRenda());
			st.setDate(3, rendaMensal.getData());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public RendaMensal buscaPorId(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT cod_renda, data, valor"
					+ "	FROM renda_mensal"
					+ " WHERE codigo=?;");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) 
				return instanciaRendaMensal(rs);
			
			return null;
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<RendaMensal> buscaCompleta() {
		return null;
	}
	
	public List<RendaMensal> buscaPorData(Date data) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
						
			st = conn.prepareStatement("SELECT cod_renda, data, valor"
					+ "	FROM renda_mensal"
					+ " WHERE data=?;");
			
			st.setDate(1, data);
			rs = st.executeQuery();
			
			List<RendaMensal> lista = new ArrayList<RendaMensal>();
			
			while(rs.next()) 
				lista.add(instanciaRendaMensal(rs));
			
			return lista;
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private RendaMensal instanciaRendaMensal(ResultSet rs) throws SQLException {
		RendaMensal receita = new RendaMensal();
		
		receita.setCodRenda(rs.getInt("cod_renda"));
		receita.setData(rs.getDate("data"));
		receita.setValor(rs.getDouble("valor"));
		
		return receita;
	}
}//class RendaMensalDAO
