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
import too.planofinanceiro.entidades.Categoria;

public class CategoriaDaoJDBC implements Dao<Categoria>{
	
	private Connection conn;
	
	public CategoriaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insere(Categoria categoria) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO categoria("
					+ "	descricao)"
					+ "	VALUES (?);", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, categoria.getDescricao());
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next())
					categoria.setCodigo(rs.getInt(1));
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
	public void atualiza(Categoria categoria) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE categoria"
					+ "	SET descricao=?"
					+ "	WHERE codigo=?;");
			
			st.setString(1, categoria.getDescricao());
			st.setInt(2, categoria.getCodigo());
			
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Categoria buscaPorId(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, descricao"
					+ "	FROM categoria"
					+ " WHERE codigo=?;");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) 
				return instanciaCategoria(rs);
			
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
	public List<Categoria> buscaCompleta() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, descricao"
					+ "	FROM categoria;");
			
			rs = st.executeQuery();
			
			List<Categoria> lista = new ArrayList<Categoria>();
			
			while(rs.next()) 
				lista.add(instanciaCategoria(rs));
			
			return lista;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public Categoria buscaPorDescricao(String descricao) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, descricao"
					+ "	FROM categoria"
					+ " WHERE descricao=?;");
			
			st.setString(1, descricao);
			rs = st.executeQuery();
			
			if(rs.next()) 
				return instanciaCategoria(rs);
			
			return null;
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Categoria instanciaCategoria(ResultSet rs) throws SQLException {
		Categoria categoria = new Categoria();
		
		categoria.setCodigo(rs.getInt("codigo"));
		categoria.setDescricao(rs.getString("descricao"));

		return categoria;
	}
	
}//class CategoriaDaoJDBC
