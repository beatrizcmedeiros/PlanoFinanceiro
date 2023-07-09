package too.planofinanceiro.dao.implementacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mos.io.InputOutput;
import too.planofinanceiro.dao.Dao;
import too.planofinanceiro.db.DB;
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
			InputOutput.showError(e.getMessage(), "Insere Renda Mensal");
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void atualiza(RendaMensal rendaMensal, RendaMensal nSovaRendaMensal) {
//		PreparedStatement st = null;
//		try {
//			st = conn.prepareStatement("UPDATE renda_mensal"
//					+ "	SET valor=?"
//					+ "	WHERE cod_renda=?"
//					+ " AND data=?;");
//			
//			st.setDouble(1, rendaMensal.getValor());
//			st.setInt(2, rendaMensal.getCodRenda());
//			st.setDate(3, rendaMensal.getData());
//			
//			st.executeUpdate();
//			
//		} catch (SQLException e) {
//			InputOutput.showError(e.getMessage(), "Atualiza Renda Mensal");
//		}
//		finally {
//			DB.closeStatement(st);
//		}
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
			InputOutput.showError(e.getMessage(), "Renda Mensal: Busca Por ID");
			return null;
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
			InputOutput.showError(e.getMessage(), "Renda Mensal: Busca Por Data");
			return null;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public double valorReceitaTotalPorMes(int mes, int ano) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT SUM(valor) as totalMes"
					+ "	FROM renda_mensal"
					+ " WHERE EXTRACT(MONTH FROM data) = ? AND EXTRACT(YEAR FROM data) = ?;");
			
			st.setInt(1, mes);
			st.setInt(2, ano);
			rs = st.executeQuery();
			
			while (rs.next()) 
				return rs.getDouble("totalMes");
			return 0;
		}catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Renda Mensal: Receita Total Por Mês");
			return 0;
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
	
	public static Date formataData(String dataString) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        try {
            java.util.Date utilDate = formato.parse(dataString);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            return sqlDate;
        } catch (ParseException e) {
        	InputOutput.showError(e.getMessage(), "Renda Mensal: Formata Data");
			return null;
        }
    }
}//class RendaMensalDAO
