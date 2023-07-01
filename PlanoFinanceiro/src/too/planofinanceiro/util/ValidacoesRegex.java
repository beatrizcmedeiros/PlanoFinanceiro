package too.planofinanceiro.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacoesRegex {
	
	public static boolean validarString(String string) {
        String padrao = "^[\\p{L}\\s\\/\\-\\.]+$"; // Apenas letras maiúsculas e minúsculas são permitidas

        Pattern pattern = Pattern.compile(padrao);
        Matcher matcher = pattern.matcher(string);

        return matcher.matches();
    }
	
	public static boolean validarStringComNumero(String string) {
		String padrao = "^[\\p{L}\\s\\/\\-\\.\\d]+$"; // Apenas letras maiúsculas, minúsculas e números são permitidos

        Pattern pattern = Pattern.compile(padrao);
        Matcher matcher = pattern.matcher(string);

        return matcher.matches();
    }
	
	public static boolean validarDouble(double valor) {
        String entrada = String.format("%.2f", valor);
        String padrao = "^[-+]?\\d+(\\.\\d+)?$"; // Valor double válido

        Pattern pattern = Pattern.compile(padrao);
        Matcher matcher = pattern.matcher(entrada);

        return matcher.matches();
    }
	
	public static boolean validarDouble(String valor) {
		if(valor.contains("%")) {
			String[] aux = valor.split("%"); 
			valor = aux[0];
		}
		valor = valor.replace(".", "").replace(",", ".");

        String padrao = "^[-+]?\\d+(\\.\\d+)?$"; // Valor double válido

        Pattern pattern = Pattern.compile(padrao);
        Matcher matcher = pattern.matcher(valor);

        return matcher.matches();
    }
	
	public static boolean validarData (String data) {
        String padrao = "^(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$"; // Data válida no formato dd/mm/yyyy

        Pattern pattern = Pattern.compile(padrao);
        Matcher matcher = pattern.matcher(data);

        return matcher.matches();
    }
	
	public static boolean validarDataDiaMes (String data){
        String padrao = "^(0[1-9]|[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])$"; // Dia e mês válidos no formato dd/mm

        Pattern pattern = Pattern.compile(padrao);
        Matcher matcher = pattern.matcher(data);

        return matcher.matches();
    }
	
}//class Validacoes
