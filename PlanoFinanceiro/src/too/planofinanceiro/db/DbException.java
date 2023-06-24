package too.planofinanceiro.db;

public class DbException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	/*
	 * Exceção para acesso a dados, transmite a mensagem passada   
	 * por parametro para a super classe RuntimeException.
	 */
	public DbException(String msg) {
		super(msg);
	}
}//class DbException
