package too.planofinanceiro.arquivo;

import java.util.List;

import mos.reader.Line;
import mos.reader.Reader;

public class Arquivo {

	List<Line> linhas;

	public Arquivo() {
	}

	public List<Line> importar(String nomeArquivo) {
		List<Line> linhasArquivo = Reader.read(nomeArquivo, Reader.SEMICOLON);
		
		if(linhasArquivo == null) 
			return null;
		return linhasArquivo;
	}
	
	/*
	 * Baseado no tamanho da primeira linha define qual o tipo de arquivo estará sendo travalhado.
	 * return 1 para arquivo de receita, 2 para despesa, 3 para investimento e 0 para caso não seja compativel com nenhum deles.
	 */
	public int definirTipoArquivo(List<Line> linhasArquivo) {
		for(Line l : linhasArquivo) {
			if(l.quantityOfData() == 3)
				return 1;
			else if(l.quantityOfData() == 7)
				return 2;
			else if(l.quantityOfData() == 8)
				return 3;
			else
				return 0;
		}
		return 0;
	}
	
}//class Arquivo
