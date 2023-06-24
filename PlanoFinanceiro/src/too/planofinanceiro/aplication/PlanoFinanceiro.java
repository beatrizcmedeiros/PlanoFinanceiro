package too.planofinanceiro.aplication;

import java.sql.Connection;

import too.planofinanceiro.db.DB;
import too.planofinanceiro.gui.IgPlanoFinanceiro;

public class PlanoFinanceiro {

	public static void main(String[] args) {
		Connection conn = DB.getConnection();
		new IgPlanoFinanceiro(conn);
//		DB.closeConection();
	}
}//class PlanoFinanceiro
