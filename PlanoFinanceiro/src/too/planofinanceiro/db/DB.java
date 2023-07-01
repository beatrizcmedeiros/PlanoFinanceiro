package too.planofinanceiro.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import mos.io.InputOutput;

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
			InputOutput.showError(e.getMessage(), "Banco de Dados: Propriedades");
			return null;
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
				InputOutput.showError(e.getMessage(), "Banco de Dados: Abrir Conexão");
				return null;
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
				InputOutput.showError(e.getMessage(), "Banco de Dados: Fechar Conexão");
			}
		}
	}
	
	/*Fecha o objeto do tipo Statement para prevenir vazamento de memória.*/
	public static void closeStatement(Statement st) {
		if( st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				InputOutput.showError(e.getMessage(), "Fecha Objeto: Statement");
			}
		}
	}
	
	/*Fecha o objeto do tipo ResultSet para prevenir vazamento de memória.*/
	public static void closeResultSet(ResultSet rs) {
		if( rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				InputOutput.showError(e.getMessage(), "Fecha Objeto: ResultSet");
			}
		}
	}
}//class DB
