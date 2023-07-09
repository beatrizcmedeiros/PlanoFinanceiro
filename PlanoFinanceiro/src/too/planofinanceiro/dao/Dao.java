package too.planofinanceiro.dao;

import java.util.List;

public interface Dao<T> {
	
	void insere(T elemento);
	void atualiza(T novoElemento, T antigoElemento);
	T buscaPorId(int id);
	List<T> buscaCompleta();
	
}//interface Dao
