package br.gov.caixa.discovery.core.extratores;

import static org.junit.Assert.*;

import java.util.regex.Matcher;

import org.junit.Test;

import br.gov.caixa.discovery.core.utils.PatternsSIAOI;

public class PatternsSIAOITest {

	@Test
	public void comandoMovePossuiSubRotinaStatica() {
		String texto = "            MOVE WAOISB600            TO W99-SUB-ROTINA";
		Matcher m =  PatternsSIAOI.COBOL_P_MOVE.matcher(texto);
		assertTrue(m.matches()); 
	}
	
	@Test
	public void comandoMoveGetNomeSubRotinaStatica() {
		String texto = "            MOVE WAOISDMON            TO W99-SUB-ROTINA";
		Matcher m =  PatternsSIAOI.COBOL_P_MOVE.matcher(texto);
		if(m.matches())
			System.out.println(m.group("nomePrograma"));
		assertTrue(m.group("nomePrograma").equals("AOISDMON"));
	}
	
	@Test
	public void comandoMoveGetNomeSubRotinaStaticaAspas() {
		String texto = "            MOVE 'AOISBMSG'      TO W99-SUB-ROTINA";
		Matcher m =  PatternsSIAOI.COBOL_P_MOVE_ASPAS.matcher(texto);
		if(m.matches())
			System.out.println(m.group("nomePrograma"));
		assertTrue(m.group("nomePrograma").equals("AOISBMSG"));
	}
	
	@Test
	public void comandoCallGetNomeSubRotinaStatica() {
		String texto = "           CALL WAOISB901 USING ENT-COD-FUNCAO,";
		Matcher m =  PatternsSIAOI.COBOL_P_CALL_AOI.matcher(texto);
		if(m.matches())
			System.out.println(m.group("nomePrograma"));
		assertTrue(m.group("nomePrograma").equals("AOISB901"));
	}

}
