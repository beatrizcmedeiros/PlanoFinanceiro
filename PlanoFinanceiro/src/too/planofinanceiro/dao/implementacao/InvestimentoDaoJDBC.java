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
import too.planofinanceiro.entidades.Investimento;

public class InvestimentoDaoJDBC implements Dao<Investimento>{
	
	private Connection conn;
	
	public InvestimentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insere(Investimento investimento) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO investimento"
					+ "(objetivo, estrategia, nome, valor_investido, posicao, rendimento_bruto, rentabilidade, vencimento)"
					+ "	VALUES (?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, investimento.getObjetivo());
			st.setString(2, investimento.getEstrategia());
			st.setString(3, investimento.getNome());
			st.setDouble(4, investimento.getValorInvestido());
			st.setDouble(5, investimento.getPosicao());
			st.setDouble(6, investimento.getRendimentoBruto());
			st.setDouble(7, investimento.getRentabilidade());
			st.setDate(8, investimento.getVencimento());
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next())
					investimento.setCodigo(rs.getInt(1));
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
	public void atualiza(Investimento investimento) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE investimento"
					+ "	SET objetivo=?, estrategia=?, nome=?, valor_investido=?, posicao=?, rendimento_bruto=?, rentabilidade=?, vencimento=?"
					+ "	WHERE codigo=?;");
			
			st.setString(1, investimento.getObjetivo());
			st.setString(2, investimento.getEstrategia());
			st.setString(3, investimento.getNome());
			st.setDouble(4, investimento.getValorInvestido());
			st.setDouble(5, investimento.getPosicao());
			st.setDouble(6, investimento.getRendimentoBruto());
			st.setDouble(7, investimento.getRentabilidade());
			st.setDate(8, investimento.getVencimento());
			st.setInt(9, investimento.getCodigo());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Investimento buscaPorId(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, objetivo, estrategia, nome, valor_investido, posicao, rendimento_bruto, rentabilidade, vencimento"
					+ "	FROM investimento"
					+ " WHERE codigo=?;");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next())
				return instanciaInvestimento(rs);
			
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
	public List<Investimento> buscaCompleta() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT codigo, objetivo, estrategia, nome, valor_investido, posicao, rendimento_bruto, rentabilidade, vencimento"
					+ "	FROM investimento"
					+ " ORDER BY vencimento ASC;");
			
			rs = st.executeQuery();
			
			List<Investimento> lista = new ArrayList<Investimento>();
			
			while(rs.next()) 
				lista.add(instanciaInvestimento(rs));
			
			return lista;
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Investimento instanciaInvestimento(ResultSet rs) throws SQLException {
		Investimento investimento = new Investimento();
		
		investimento.setCodigo(rs.getInt("codigo"));
		investimento.setObjetivo(rs.getString("objetivo"));
		investimento.setEstrategia(rs.getString("estrategia"));
		investimento.setNome(rs.getString("nome"));
		investimento.setValorInvestido(rs.getDouble("valor_investido"));
		investimento.setPosicao(rs.getDouble("posicao"));
		investimento.setRendimentoBruto(rs.getDouble("rendimento_bruto"));
		investimento.setRentabilidade(rs.getDouble("rentabilidade"));
		investimento.setVencimento(rs.getDate("vencimento"));
		
		return investimento;
	}
}//class InvestimentoDAO
