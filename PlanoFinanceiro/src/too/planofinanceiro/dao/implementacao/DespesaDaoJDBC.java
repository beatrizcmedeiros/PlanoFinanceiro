package too.planofinanceiro.dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import too.planofinanceiro.dao.Dao;
import too.planofinanceiro.db.DB;
import too.planofinanceiro.db.DbException;
import too.planofinanceiro.entidades.Despesa;

public class DespesaDaoJDBC implements Dao<Despesa>{
	
	private Connection conn;
	
	public DespesaDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insere(Despesa despesa) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO despesa"
					+ "(descricao, cod_categoria)"
					+ "	VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, despesa.getDescricao());
			st.setInt(2, despesa.getCodCategoria());
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next())
					despesa.setCodigo(rs.getInt(1));
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Erro: nenhuma linha foi afetada!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void atualiza(Despesa despesa) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE despesa"
					+ "	SET descricao=?, cod_categoria=?"
					+ "	WHERE codigo=?;");
			
			st.setString(1, despesa.getDescricao());
			st.setInt(2, despesa.getCodCategoria());
			st.setInt(3, despesa.getCodigo());
			
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}


	@Override
	public Despesa buscaPorId(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, descricao, cod_categoria"
					+ "	FROM despesa"
					+ " WHERE cod_renda=?;");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) 
				return instanciaDespesa(rs);
			
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
	public List<Despesa> buscaCompleta() {
		return null;
	}

	public List<Despesa> buscaPorCategoria(int codCategoria) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, descricao, cod_categoria"
					+ "	FROM despesa"
					+ " WHERE cod_categoria=?;");
			
			st.setInt(1, codCategoria);
			rs = st.executeQuery();
			
			List<Despesa> lista = new ArrayList<Despesa>();
			
			while(rs.next()) 
				lista.add(instanciaDespesa(rs));
			
			return lista;
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}		
	
	private Despesa instanciaDespesa(ResultSet rs) throws SQLException {
		Despesa despesa = new Despesa();
		
		despesa.setCodigo(rs.getInt("codigo"));
		despesa.setDescricao(rs.getString("descricao"));
		despesa.setCodCategoria(rs.getInt("cod_categoria"));

		return despesa;
	}
}//class DespesaDAO


