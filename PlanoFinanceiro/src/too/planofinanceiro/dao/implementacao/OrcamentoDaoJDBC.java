package too.planofinanceiro.dao.implementacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mos.io.InputOutput;
import too.planofinanceiro.dao.Dao;
import too.planofinanceiro.db.DB;
import too.planofinanceiro.entidades.Orcamento;
import too.planofinanceiro.entidades.TabelaOrcamento;

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
			
			st.setString(1, orcamento.getMesAno());
			st.setInt(2, orcamento.getCodDespesa());
			st.setDate(3, orcamento.getDataDespesa());
			st.setDate(4, orcamento.getDataPagamento());
			st.setInt(5, orcamento.getCodPagamento());
			st.setDouble(6, orcamento.getValor());
			st.setString(7, orcamento.getSituacao());
			
			st.executeUpdate();
	
		} catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Insere Orçamento");
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
			st.setString(6, orcamento.getMesAno());
			st.setInt(7, orcamento.getCodDespesa());
			
			st.executeUpdate();
	
		} catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Atualiza Orçamento");
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
			InputOutput.showError(e.getMessage(), "Orçamento: Busca Completa");
			return null;
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
			InputOutput.showError(e.getMessage(), "Orçamento: Busca Por Mes e Ano da Despesa");
			return null;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public List<TabelaOrcamento> buscaPorCategoriaEmes(int codCategoria, int mes, int ano){
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT o.data_despesa, EXTRACT(DAY FROM o.data_pagamento) AS dia_pagamento,"
					+ " (SELECT descricao FROM forma_pagamento WHERE codigo = o.cod_forma_pagamento) AS descricao_pagamento,"
					+ " d.descricao, o.valor, o.situacao"
					+ " FROM despesa d"
					+ " JOIN orcamento o ON d.codigo = o.cod_despesa"
					+ " WHERE d.cod_categoria = ?"
					+ " AND EXTRACT(MONTH FROM o.data_pagamento) = ?"
					+ " AND EXTRACT(YEAR FROM o.data_pagamento) = ?;");
			
			st.setInt(1, codCategoria);
			st.setInt(2, mes);
			st.setInt(3, ano);
			
			rs = st.executeQuery();
			
			List<TabelaOrcamento> lista = new ArrayList<TabelaOrcamento>();
			
			while(rs.next()) 
				lista.add(instanciaTabelaOrcamento(rs));
			
			return lista;
			
		}catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Orçamento: Busca Por Mes, Ano e Categoria");
			return null;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public List<TabelaOrcamento> buscaCompletaPorMes(int mes) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT o.data_despesa, EXTRACT(DAY FROM o.data_pagamento) AS dia_pagamento,"
					+ " (SELECT descricao FROM forma_pagamento WHERE codigo = o.cod_forma_pagamento) AS descricao_pagamento,"
					+ " d.descricao, o.valor, o.situacao"
					+ " FROM despesa d"
					+ " JOIN orcamento o ON d.codigo = o.cod_despesa"
					+ " WHERE EXTRACT(MONTH FROM o.data_pagamento) = ?"
					+ " AND EXTRACT(YEAR FROM o.data_pagamento) = ?;");
			
			Calendar calendario = Calendar.getInstance();
	        int ano = calendario.get(Calendar.YEAR);
	        
			st.setInt(1, mes);
			st.setInt(2, ano);
			
			rs = st.executeQuery();
			
			List<TabelaOrcamento> lista = new ArrayList<TabelaOrcamento>();
			
			while(rs.next()) 
				lista.add(instanciaTabelaOrcamento(rs));
			
			return lista;
			
		}catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Orçamento: Busca Completa dos Dados da Tabela por Mês");
			return null;
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
			InputOutput.showError(e.getMessage(), "Orçamento: Busca por Data");
			return null;
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
			InputOutput.showError(e.getMessage(), "Orçamento: Busca por Descrição");
			return null;
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
			InputOutput.showError(e.getMessage(), "Orçamento: Busca por Valor");
			return null;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public double valorDespesaTotalPorMes(int mes, int ano) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT SUM(valor) as totalDespesaMes"
					+ "	FROM orcamento"
					+ " WHERE EXTRACT(MONTH FROM data_pagamento) = ? AND EXTRACT(YEAR FROM data_pagamento) = ?;");

			st.setInt(1, mes);
			st.setInt(2, ano);
			rs = st.executeQuery();
			
			while (rs.next())
				return rs.getDouble("totalDespesaMes");
			return 0;
		}catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Valor das Despesas Totais Por Mês");
			return 0;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public double totalPago(int mes, int ano) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT SUM(valor) as totalPago"
					+ "	FROM orcamento"
					+ " WHERE EXTRACT(MONTH FROM data_pagamento) = ? AND EXTRACT(YEAR FROM data_pagamento) = ?"
					+ " AND situacao = 'Paga';");

			st.setInt(1, mes);
			st.setInt(2, ano);
			rs = st.executeQuery();
			
			while (rs.next())
				return rs.getDouble("totalPago");
			return 0;
		}catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Total Pago");
			return 0;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public double totalAPagar(int mes, int ano) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT SUM(valor) as totalAPagar"
					+ "	FROM orcamento"
					+ " WHERE EXTRACT(MONTH FROM data_pagamento) = ? AND EXTRACT(YEAR FROM data_pagamento) = ?"
					+ " AND situacao is null;");

			st.setInt(1, mes);
			st.setInt(2, ano);
			rs = st.executeQuery();
			
			while (rs.next())
				return rs.getDouble("totalAPagar");
			return 0;
		}catch (SQLException e) {
			InputOutput.showError(e.getMessage(), "Total A Pagar");
			return 0;
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Orcamento instanciaOrcamento(ResultSet rs) throws SQLException {
		Orcamento orcamento = new Orcamento();
		
		orcamento.setMesAno(rs.getString("mes_ano"));
		orcamento.setCodDespesa(rs.getInt("cod_despesa"));
		orcamento.setDataDespesa(rs.getDate("data_despesa"));
		orcamento.setDataPagamento(rs.getDate("data_pagamento"));
		orcamento.setCodPagamento(rs.getInt("cod_forma_pagamento"));
		orcamento.setValor(rs.getDouble("valor"));
		orcamento.setSituacao(rs.getString("situacao"));
		
		return orcamento;
	}
	
	private TabelaOrcamento instanciaTabelaOrcamento(ResultSet rs) throws SQLException {
		TabelaOrcamento tabelaOrcamento = new TabelaOrcamento();
		
		tabelaOrcamento.setData(rs.getDate("data_despesa"));
		tabelaOrcamento.setDia(rs.getInt("dia_pagamento"));
		tabelaOrcamento.setTipo(rs.getString("descricao_pagamento"));
		tabelaOrcamento.setDescricao(rs.getString("descricao"));
		tabelaOrcamento.setValor(rs.getDouble("valor"));
		
		if(rs.getString("situacao") == null)
			tabelaOrcamento.setPaga(false);
		else
			tabelaOrcamento.setPaga(true);
			
		return tabelaOrcamento;
	}

}//class OrcamentoDaoJDBC
