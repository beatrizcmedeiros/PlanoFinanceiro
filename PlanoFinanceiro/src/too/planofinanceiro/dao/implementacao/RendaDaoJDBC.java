package too.planofinanceiro.dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import mos.io.InputOutput;
import too.planofinanceiro.dao.Dao;
import too.planofinanceiro.db.DB;
import too.planofinanceiro.entidades.Renda;

public class RendaDaoJDBC implements Dao<Renda>{

	private Connection conn;

	public RendaDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insere(Renda renda) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO renda"
					+ "(descricao)"
					+ "	VALUES (?);", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, renda.getDescricao());
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next())
					renda.setCodigo(rs.getInt(1));
				DB.closeResultSet(rs);
			}else {
				InputOutput.showError("Erro: nenhuma linha foi afetada!", "Insere Renda");
			}
		} catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Insere Renda");
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void atualiza(Renda renda) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE renda"
					+ "	SET descricao=?"
					+ "	WHERE codigo=?;");
			
			st.setString(1, renda.getDescricao());
			st.setInt(2, renda.getCodigo());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Atualiza Renda");
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Renda buscaPorId(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, descricao"
					+ "	FROM renda"
					+ " WHERE codigo=?;");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) 
				return instanciaRenda(rs);
			
			return null;
		}catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Renda: Busca Por ID");
			return null;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public List<Renda> buscaCompleta() {
		return null;
	}
	
	public Renda buscaPorDescricao(String descricao) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, descricao"
					+ "	FROM renda"
					+ " WHERE descricao=?;");
			
			st.setString(1, descricao);
			rs = st.executeQuery();
			
			if(rs.next()) 
				return instanciaRenda(rs);
			
			return null;
		}catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Renda: Busca Por Descrição");
			return null;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Renda instanciaRenda(ResultSet rs) throws SQLException {
		Renda receita = new Renda();
		
		receita.setCodigo(rs.getInt("codigo"));
		receita.setDescricao(rs.getString("descricao"));
		
		return receita;
	}
	
}//class RendaDAO
