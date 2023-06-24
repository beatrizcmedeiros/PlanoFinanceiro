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
import too.planofinanceiro.entidades.Orcamento;

public class OrcamentoDaoJDBC implements Dao<Orcamento>{
	
	private Connection conn;
	
	public OrcamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insere(Orcamento orcamento) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO orcamento"
					+ "(mes_ano, cod_despesa, data_despesa, data_pagamento, cod_forma_pagamento, valor, situacao)"
					+ "	VALUES (?, ?, ?, ?, ?, ?, ?);");
			
			st.setDate(1, orcamento.getMesAno());
			st.setInt(2, orcamento.getCodDespesa());
			st.setDate(3, orcamento.getDataDespesa());
			st.setDate(4, orcamento.getDataPagamento());
			st.setInt(5, orcamento.getCodPagamento());
			st.setDouble(6, orcamento.getValor());
			st.setString(7, orcamento.getSituacao());
			
			st.executeUpdate();
	
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void atualiza(Orcamento orcamento) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE orcamento"
					+ "	SET data_despesa=?, data_pagamento=?, cod_forma_pagamento=?, valor=?, situacao=?"
					+ "	WHERE mes_ano=?"
					+ " AND cod_despesa=?;");
			
			st.setDate(1, orcamento.getDataDespesa());
			st.setDate(2, orcamento.getDataPagamento());
			st.setInt(3, orcamento.getCodPagamento());
			st.setDouble(4, orcamento.getValor());
			st.setString(5, orcamento.getSituacao());
			st.setDate(6, orcamento.getMesAno());
			st.setInt(7, orcamento.getCodDespesa());
			
			st.executeUpdate();
	
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Orcamento buscaPorId(int id) {
		return null;
	}

	@Override
	public List<Orcamento> buscaCompleta() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT mes_ano, cod_despesa, data_despesa, data_pagamento, cod_forma_pagamento, valor, situacao"
					+ "	FROM orcamento"
					+ " ORDER BY data_despesa ASC;");
			
			rs = st.executeQuery();
			
			List<Orcamento> lista = new ArrayList<Orcamento>();
			
			while(rs.next()) 
				lista.add(instanciaOrcamento(rs));
			
			return lista;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public Orcamento buscaPorMesAnoDespesa(String mesAno, int codDespesa) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT mes_ano, cod_despesa, data_despesa, data_pagamento, cod_forma_pagamento, valor, situacao"
					+ "	FROM orcamento"
					+ " WHERE mes_ano LIKE '?'"
					+ "	AND cod_despesa = ?;");
			
			st.setString(1, mesAno);
			st.setInt(2, codDespesa);
			rs = st.executeQuery();
			
			if(rs.next()) 
				return instanciaOrcamento(rs);
			
			return null;
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public List<Orcamento> buscaPorData(Date data) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT mes_ano, cod_despesa, data_despesa, data_pagamento, cod_forma_pagamento, valor, situacao"
					+ "	FROM orcamento"
					+ " WHERE data_despesa = '?';");
			
			st.setDate(1, data);
			
			rs = st.executeQuery();
			
			List<Orcamento> lista = new ArrayList<Orcamento>();
			
			while(rs.next()) 
				lista.add(instanciaOrcamento(rs));
			
			return lista;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public List<Orcamento> buscaPorDescricao(String descricao) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT o.mes_ano, o.cod_despesa, o.data_despesa, o.data_pagamento, o.cod_forma_pagamento, o.valor, o.situacao, d.descricao, d.codigo, d.descricao"
					+ "	FROM orcamento as o,"
					+ "		despesa as d"
					+ "	WHERE o.cod_despesa=d.codigo"
					+ "	AND d.descricao=?;");
			
			st.setString(1, descricao);
			
			rs = st.executeQuery();
			
			List<Orcamento> lista = new ArrayList<Orcamento>();
			
			while(rs.next()) 
				lista.add(instanciaOrcamento(rs));
			
			return lista;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public List<Orcamento> buscaPorValor(double valor) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT mes_ano, cod_despesa, data_despesa, data_pagamento, cod_forma_pagamento, valor, situacao"
					+ "	FROM orcamento"
					+ " WHERE valor = '?';");
			
			st.setDouble(1, valor);
			
			rs = st.executeQuery();
			
			List<Orcamento> lista = new ArrayList<Orcamento>();
			
			while(rs.next()) 
				lista.add(instanciaOrcamento(rs));
			
			return lista;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Orcamento instanciaOrcamento(ResultSet rs) throws SQLException {
		Orcamento orcamento = new Orcamento();
		
		orcamento.setMesAno(rs.getDate("mes_ano"));
		orcamento.setCodDespesa(rs.getInt("cod_despesa"));
		orcamento.setDataDespesa(rs.getDate("data_despesa"));
		orcamento.setDataPagamento(rs.getDate("data_pagamento"));
		orcamento.setCodPagamento(rs.getInt("cod_forma_pagamento"));
		orcamento.setValor(rs.getDouble("valor"));
		orcamento.setSituacao(rs.getString("situacao"));
		
		return orcamento;
	}

}//class OrcamentoDaoJDBC