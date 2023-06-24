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
import too.planofinanceiro.entidades.FormaDePagamento;

public class FormaDePagamentoDaoJDBC implements Dao<FormaDePagamento> {
	
	private Connection conn;
	
	public FormaDePagamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insere(FormaDePagamento formaDePagamento) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO forma_pagamento("
					+ "	descricao)"
					+ "	VALUES (?);", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, formaDePagamento.getDescricao());
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next())
					formaDePagamento.setCodigo(rs.getInt(1));
				DB.closeResultSet(rs);
			}else {
				try {
					conn.rollback();
				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
				throw new DbException("Erro: nenhuma linha foi afetada!");
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new DbException(e1.getMessage());
			}
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void atualiza(FormaDePagamento formaDePagamento) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE forma_pagamento"
					+ "	SET descricao=?"
					+ "	WHERE codigo=?;");
			
			st.setString(1, formaDePagamento.getDescricao());
			st.setInt(2, formaDePagamento.getCodigo());
			
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public FormaDePagamento buscaPorId(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, descricao"
					+ "	FROM categoria"
					+ " WHERE codigo=?;");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) 
				return instanciaFormaDePagamento(rs);
			
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
	public List<FormaDePagamento> buscaCompleta() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, descricao"
					+ "	FROM forma_pagamento;");
			
			rs = st.executeQuery();
			
			List<FormaDePagamento> lista = new ArrayList<FormaDePagamento>();
			
			while(rs.next()) 
				lista.add(instanciaFormaDePagamento(rs));
			
			return lista;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public FormaDePagamento buscaPorDescricao(String descricao) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, descricao"
					+ "	FROM forma_pagamento"
					+ " WHERE descricao=?;");
			
			st.setString(1, descricao);
			rs = st.executeQuery();
			
			if(rs.next()) 
				return instanciaFormaDePagamento(rs);
			
			return null;
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private FormaDePagamento instanciaFormaDePagamento(ResultSet rs) throws SQLException {
		FormaDePagamento formaDePagamento = new FormaDePagamento();
		
		formaDePagamento.setCodigo(rs.getInt("codigo"));
		formaDePagamento.setDescricao(rs.getString("descricao"));

		return formaDePagamento;
	}
}//class FormaDePagamentoDaoJDBC
