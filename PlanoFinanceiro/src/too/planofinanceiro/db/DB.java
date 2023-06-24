package too.planofinanceiro.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	private static Connection conn = null; 
	
	/* Responsável por carregar as propriedades relacionadas a conexão do banco de dados
	 * e armazená-las em um objeto do tipo Properties.
	 * Caso dê algum erro no processo dispara um exceção.*/
	private static Properties loadProperties() {
		try(FileInputStream fs = new FileInputStream("arquivos/db.properties")) {
			Properties props = new Properties();
			props.load(fs);
			return props;
		}catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	/*Faz a conexão com o bando de dados e retorna a mesma.*/
	public static Connection getConnection() {
		if(conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);	//Instância um objeto do tipo conection.
			}catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
			
		}
		return conn;
	}
	
	/*Fecha a conexão com o banco de dados.*/
	public static void closeConection() {
		if(conn != null) {
			try {
				conn.close();
			}catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	/*Fecha o objeto do tipo Statement para prevenir vazamento de memória.*/
	public static void closeStatement(Statement st) {
		if( st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	/*Fecha o objeto do tipo ResultSet para prevenir vazamento de memória.*/
	public static void closeResultSet(ResultSet rs) {
		if( rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}//class DB
