package br.gov.caixa.discovery.core.extratores;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.modelos.Atributo;
import br.gov.caixa.discovery.core.tipos.TipoAmbiente;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.tipos.TipoAtributo;
import br.gov.caixa.discovery.core.utils.Configuracao;

public class TEST_Copybook {

	private static Extrator converter = null;
	private static String pasta = "D:\\ti\\git\\discovery\\discovery\\discovery-core\\src\\test\\resources\\";
	private static String arquivo = pasta + "copybook_generico.cbl";
	private static Artefato artefato = null;
	private static String[] argumentos = { " --ambiente PRD --sistema SIPCS " };

	@BeforeClass
	public static void setUp() throws Exception {
		Configuracao.carregar(argumentos);
		converter = new Extrator();

		converter.inicializar(arquivo, TipoArtefato.COPYBOOK);
		List<Artefato> listaArtefatos = converter.converter();
		artefato = listaArtefatos.get(0);
	}

	@Test
	public void test_ambiente() {
		assertTrue(TipoAmbiente.PRD.equals(artefato.getAmbiente()));
	}

	@Test
	public void test_sistema() {
		assertTrue("SIPCS".equals(artefato.getSistema()));
	}

	@Test
	public void test_nome() {
		assertTrue("COPYBOOK_GENERICO".equals(artefato.getNome()));
	}

	@Test
	public void test_tabelas() {

	}

	@Test
	public void test_copybooks() {

	}

	@Test
	public void test_variavel() {
		List<Artefato> listaArtefato = new ArrayList<>();

		// ***
		Artefato artefato01 = new Artefato("DCLMPDT024", null, null, TipoArtefato.COPYBOOK_VARIAVEL, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo1_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "01", null, null);
		Atributo atributo1_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "01 DCLMPDT024.", null, null);
		artefato01.adicionarAtributo(atributo1_0);
		artefato01.adicionarAtributo(atributo1_1);
		// ***
		Artefato artefato02 = new Artefato("CODENT", null, null, TipoArtefato.COPYBOOK_VARIAVEL, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo2_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "10", null, null);
		Atributo atributo2_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "10 CODENT PIC X(4).", null, null);
		Atributo atributo2_2 = new Atributo(TipoAtributo.TIPO, "X(4)", null, null);
		artefato02.adicionarAtributo(atributo2_0);
		artefato02.adicionarAtributo(atributo2_1);
		artefato02.adicionarAtributo(atributo2_2);
		// ***
		Artefato artefato03 = new Artefato("CODMAR", null, null, TipoArtefato.COPYBOOK_VARIAVEL, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo3_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "10", null, null);
		Atributo atributo3_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "10 CODMAR PIC S9(2)V USAGE COMP-3.",
				null, null);
		Atributo atributo3_2 = new Atributo(TipoAtributo.TIPO, "S9(2)V", null, null);
		artefato03.adicionarAtributo(atributo3_0);
		artefato03.adicionarAtributo(atributo3_1);
		artefato03.adicionarAtributo(atributo3_2);
		// ***
		Artefato artefato04 = new Artefato("PARAM-CSC", null, null, TipoArtefato.COPYBOOK_VARIAVEL, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo4_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "01", null, null);
		Atributo atributo4_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "01 PARAM-CSC.", null, null);
		artefato04.adicionarAtributo(atributo4_0);
		artefato04.adicionarAtributo(atributo4_1);
		// ***
		Artefato artefato05 = new Artefato("AREA-ENTRADA-AECMCSC", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo5_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "03", null, null);
		Atributo atributo5_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "03 AREA-ENTRADA-AECMCSC.", null, null);
		artefato05.adicionarAtributo(atributo5_0);
		artefato05.adicionarAtributo(atributo5_1);
		// ***
		Artefato artefato06 = new Artefato("ENTORNO-AECMCSC", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo6_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "05", null, null);
		Atributo atributo6_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "05 ENTORNO-AECMCSC PIC X.", null, null);
		Atributo atributo6_2 = new Atributo(TipoAtributo.TIPO, "X", null, null);
		artefato06.adicionarAtributo(atributo6_0);
		artefato06.adicionarAtributo(atributo6_1);
		artefato06.adicionarAtributo(atributo6_2);
		// ***
		Artefato artefato07 = new Artefato("CLAVE-GE-AECMCSC", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo7_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "05", null, null);
		Atributo atributo7_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "05 CLAVE-GE-AECMCSC.", null, null);
		artefato07.adicionarAtributo(atributo7_0);
		artefato07.adicionarAtributo(atributo7_1);
		// ***
		Artefato artefato08 = new Artefato("IDENTIFIC-AECMCSC", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo8_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "07", null, null);
		Atributo atributo8_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "07 IDENTIFIC-AECMCSC PIC X(05).", null,
				null);
		Atributo atributo8_2 = new Atributo(TipoAtributo.TIPO, "X(05)", null, null);
		artefato08.adicionarAtributo(atributo8_0);
		artefato08.adicionarAtributo(atributo8_1);
		artefato08.adicionarAtributo(atributo8_2);
		// ***
		Artefato artefato09 = new Artefato("DATOS-DE-SCRIPT", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo9_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "05", null, null);
		Atributo atributo9_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "05 DATOS-DE-SCRIPT.", null, null);
		artefato09.adicionarAtributo(atributo9_0);
		artefato09.adicionarAtributo(atributo9_1);
		// ***
		Artefato artefato10 = new Artefato("EL-COMANDO", null, null, TipoArtefato.COPYBOOK_VARIAVEL, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo10_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "09", null, null);
		Atributo atributo10_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"09 EL-COMANDO OCCURS 8, INDEXED BY IND-COMANDO.", null, null);
		artefato10.adicionarAtributo(atributo10_0);
		artefato10.adicionarAtributo(atributo10_1);
		// ***
		Artefato artefato11 = new Artefato("ETIQUETA-COMANDO", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo11_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "12", null, null);
		Atributo atributo11_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "12 ETIQUETA-COMANDO PIC X(01).", null,
				null);
		Atributo atributo11_2 = new Atributo(TipoAtributo.TIPO, "X(01)", null, null);
		artefato11.adicionarAtributo(atributo11_0);
		artefato11.adicionarAtributo(atributo11_1);
		artefato11.adicionarAtributo(atributo11_2);
		// ***
		Artefato artefato12 = new Artefato("RETORNO", null, null, TipoArtefato.COPYBOOK_VARIAVEL, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo12_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "05", null, null);
		Atributo atributo12_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "05 RETORNO.", null, null);
		artefato12.adicionarAtributo(atributo12_0);
		artefato12.adicionarAtributo(atributo12_1);
		// ***
		Artefato artefato13 = new Artefato("RETORNO-CRIP-CSC", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo13_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "07", null, null);
		Atributo atributo13_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "07 RETORNO-CRIP-CSC PIC 9(08) COMP.",
				null, null);
		artefato13.adicionarAtributo(atributo13_0);
		artefato13.adicionarAtributo(atributo13_1);
		// ***
		Artefato artefato14 = new Artefato("D3-NSECFIC", null, null, TipoArtefato.COPYBOOK_VARIAVEL, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo14_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "10", null, null);
		Atributo atributo14_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "10 D3-NSECFIC PIC S9(10)V.", null,
				null);
		Atributo atributo14_2 = new Atributo(TipoAtributo.TIPO, "S9(10)V", null, null);
		artefato14.adicionarAtributo(atributo14_0);
		artefato14.adicionarAtributo(atributo14_1);
		artefato14.adicionarAtributo(atributo14_2);
		// ***
		Artefato artefato15 = new Artefato("LIMCT-PORPAGOA", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo15_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "10", null, null);
		Atributo atributo15_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "10 LIMCT-PORPAGOA PIC 9(03)V9(04).",
				null, null);
		Atributo atributo15_2 = new Atributo(TipoAtributo.TIPO, "9(03)V9(04)", null, null);
		artefato15.adicionarAtributo(atributo15_0);
		artefato15.adicionarAtributo(atributo15_1);
		artefato15.adicionarAtributo(atributo15_2);
		// ***
		Artefato artefato16 = new Artefato("SW-CHECK-LUHN-VALIDO", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo16_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "10", null, null);
		Atributo atributo16_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "10 SW-CHECK-LUHN-VALIDO PIC 9(01).",
				null, null);
		Atributo atributo16_2 = new Atributo(TipoAtributo.TIPO, "9(01)", null, null);
		artefato16.adicionarAtributo(atributo16_0);
		artefato16.adicionarAtributo(atributo16_1);
		artefato16.adicionarAtributo(atributo16_2);
		// ***
		Artefato artefato17 = new Artefato("CHECK-LUHN-VALIDO", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo17_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "88", null, null);
		Atributo atributo17_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "88 CHECK-LUHN-VALIDO VALUE 1.", null,
				null);
		Atributo atributo17_2 = new Atributo(TipoAtributo.VALOR_PADRAO, "1", null, null);
		artefato17.adicionarAtributo(atributo17_0);
		artefato17.adicionarAtributo(atributo17_1);
		artefato17.adicionarAtributo(atributo17_2);
		// ***
		Artefato artefato18 = new Artefato("(AMMIGINV)", null, null, TipoArtefato.COPYBOOK_VARIAVEL, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo18_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "03", null, null);
		Atributo atributo18_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "03 (AMMIGINV).", null, null);
		artefato18.adicionarAtributo(atributo18_0);
		artefato18.adicionarAtributo(atributo18_1);
		// ***
		Artefato artefato19 = new Artefato("(AMMIGINV)-CODENT", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo19_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "10", null, null);
		Atributo atributo19_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "10 (AMMIGINV)-CODENT PIC X(04).", null,
				null);
		Atributo atributo19_2 = new Atributo(TipoAtributo.TIPO, "X(04)", null, null);
		artefato19.adicionarAtributo(atributo19_0);
		artefato19.adicionarAtributo(atributo19_1);
		artefato19.adicionarAtributo(atributo19_2);
		// ***
		Artefato artefato20 = new Artefato("DTCCAL-R-DT-CRIAC-ARQ", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo20_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "10", null, null);
		Atributo atributo20_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"10 DTCCAL-R-DT-CRIAC-ARQ REDEFINES DTCCAL-DT-CRIAC-ARQ.", null, null);
		Atributo atributo20_2 = new Atributo(TipoAtributo.COBOL_REDEFINE, "DTCCAL-DT-CRIAC-ARQ", null, null);
		artefato20.adicionarAtributo(atributo20_0);
		artefato20.adicionarAtributo(atributo20_1);
		artefato20.adicionarAtributo(atributo20_2);
		// ***
		Artefato artefato21 = new Artefato(":T:-MESSAGE-STATUS", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo21_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "01", null, null);
		Atributo atributo21_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"01 :T:-MESSAGE-STATUS EXTERNAL PIC S9(4) BINARY.", null, null);
		Atributo atributo21_2 = new Atributo(TipoAtributo.TIPO, "S9(4)", null, null);
		artefato21.adicionarAtributo(atributo21_0);
		artefato21.adicionarAtributo(atributo21_1);
		artefato21.adicionarAtributo(atributo21_2);
		// ***
		Artefato artefato22 = new Artefato(":T:-DE-MAP", null, null, TipoArtefato.COPYBOOK_VARIAVEL, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo22_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "01", null, null);
		Atributo atributo22_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "01 :T:-DE-MAP EXTERNAL.", null, null);
		artefato22.adicionarAtributo(atributo22_0);
		artefato22.adicionarAtributo(atributo22_1);
		// ***
		Artefato artefato23 = new Artefato(":T:-DE-MAP-ENTRY", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo23_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "05", null, null);
		Atributo atributo23_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"05 :T:-DE-MAP-ENTRY OCCURS 128 TIMES INDEXED BY :T:-D.", null, null);
		artefato23.adicionarAtributo(atributo23_0);
		artefato23.adicionarAtributo(atributo23_1);
		// ***
		Artefato artefato24 = new Artefato(":T:-DE-FIRST-SUBFLD", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo24_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "10", null, null);
		Atributo atributo24_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "10 :T:-DE-FIRST-SUBFLD INDEX.", null,
				null);
		artefato24.adicionarAtributo(atributo24_0);
		artefato24.adicionarAtributo(atributo24_1);
		// ***
		Artefato artefato25 = new Artefato(":T:-DE-NAMES", null, null, TipoArtefato.COPYBOOK_VARIAVEL, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo25_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "01", null, null);
		Atributo atributo25_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "01 :T:-DE-NAMES REDEFINES :T:-DE-MAP.",
				null, null);
		Atributo atributo25_2 = new Atributo(TipoAtributo.COBOL_REDEFINE, ":T:-DE-MAP", null, null);
		artefato25.adicionarAtributo(atributo25_0);
		artefato25.adicionarAtributo(atributo25_1);
		artefato25.adicionarAtributo(atributo25_2);
		// ***
		Artefato artefato26 = new Artefato("TAZGHSM-OFFSET-P", null, null, TipoArtefato.COPYBOOK_VARIAVEL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo26_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "01", null, null);
		Atributo atributo26_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "01 TAZGHSM-OFFSET-P.", null, null);
		artefato26.adicionarAtributo(atributo26_0);
		artefato26.adicionarAtributo(atributo26_1);
		// ***
		Artefato artefato27 = new Artefato("FILLER", null, null, TipoArtefato.COPYBOOK_VARIAVEL, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo27_0 = new Atributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL, "15", null, null);
		Atributo atributo27_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "15 FILLER PIC X VALUE X'00'.", null,
				null);
		Atributo atributo27_2 = new Atributo(TipoAtributo.TIPO, "X", null, null);
		Atributo atributo27_3 = new Atributo(TipoAtributo.VALOR_PADRAO, "X'00'", null, null);

		artefato27.adicionarAtributo(atributo27_0);
		artefato27.adicionarAtributo(atributo27_1);
		artefato27.adicionarAtributo(atributo27_2);
		artefato27.adicionarAtributo(atributo27_3);
		// ***
		// ***
		listaArtefato.add(artefato01);
		listaArtefato.add(artefato02);
		listaArtefato.add(artefato03);
		listaArtefato.add(artefato04);
		listaArtefato.add(artefato05);
		listaArtefato.add(artefato06);
		listaArtefato.add(artefato07);
		listaArtefato.add(artefato08);
		listaArtefato.add(artefato09);
		listaArtefato.add(artefato10);
		listaArtefato.add(artefato11);
		listaArtefato.add(artefato12);
		listaArtefato.add(artefato13);
		listaArtefato.add(artefato14);
		listaArtefato.add(artefato15);
		listaArtefato.add(artefato16);
		listaArtefato.add(artefato17);
		listaArtefato.add(artefato18);
		listaArtefato.add(artefato19);
		listaArtefato.add(artefato20);
		listaArtefato.add(artefato21);
		listaArtefato.add(artefato22);
		listaArtefato.add(artefato23);
		listaArtefato.add(artefato24);
		listaArtefato.add(artefato25);
		listaArtefato.add(artefato26);
		listaArtefato.add(artefato27);
		// ***

		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(artefato.getArtefatosRelacionados().contains(entryArtefato));
			Artefato subentryArtefato = artefato.getArtefatosRelacionados().stream()
					.filter(p -> p.equals(entryArtefato)).collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = 0;
		for (Artefato subentry : artefato.getArtefatosRelacionados()) {
			if (!TipoArtefato.COPYBOOK_VARIAVEL.equals(subentry.getTipoArtefato())) {
				continue;
			}
			contador++;
		}
		assertEquals(listaArtefato.size(), contador);
	}

}
