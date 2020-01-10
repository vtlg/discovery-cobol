/**
 * 
 */
package br.gov.caixa.discovery.core.utils;

import java.util.regex.Pattern;

/**
 * @author c105118
 * 
 * Classe para atender as particularidades dos padrões de nomenclatura 
 * nos programas COBOL do SIAOI
 *   
 */
public class PatternsSIAOI extends Patterns {
	 
	public static Pattern COBOL_P_MOVE = Pattern
			.compile("^[\\s]{1,}MOVE[\\s]{1,}W(?<nomePrograma>[\\S]{1,})[\\s]{1,}.*W99-SUB-ROTINA$");
	
	public static Pattern COBOL_P_MOVE_ASPAS = Pattern
			.compile("^[\\s]{1,}MOVE[\\s]{1,}'(?<nomePrograma>[\\S]{1,})'[\\s]{1,}.*W99-SUB-ROTINA$");
	
	public static Pattern COBOL_P_CALL_AOI = Pattern
			.compile("^[\\s]{1,}CALL[\\s]{1,}W(?<nomePrograma>[\\S]{1,})[\\s]{1,}.*$");
	 

}
