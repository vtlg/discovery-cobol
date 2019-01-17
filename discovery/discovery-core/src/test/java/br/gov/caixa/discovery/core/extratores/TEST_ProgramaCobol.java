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

public class TEST_ProgramaCobol {

	private static Extrator converter = null;
	private static String pasta = "D:\\ti\\git\\discovery\\discovery\\discovery-core\\src\\test\\resources\\programas_cobol\\";
	private static String arquivo = pasta + "programa_cobol_generico.cbl";
	private static Artefato artefato = null;
	private static String[] argumentos = { " --ambiente PRD --sistema SIPCS " };

	@BeforeClass
	public static void setUp() throws Exception {
		Configuracao.carregar(argumentos);
		converter = new Extrator();
		converter.inicializar(arquivo, TipoArtefato.PROGRAMA_COBOL);
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
		assertTrue("programa_cobol_generico".equals(artefato.getNome()));
	}

	@Test
	public void test_nome_interno() {
		assertTrue("NOMEINTERNO".equals(artefato.getNomeInterno()));
	}

	@Test
	public void test_operacao_arquivos() {
		List<Artefato> listaArtefato = new ArrayList<>();

		// ***
		Artefato artefato01 = new Artefato("ENTRADA1", null, null, TipoArtefato.FILE_DESCRIPTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo1_0 = new Atributo(TipoAtributo.COBOL_DDNAME, "E1DQ9730", null, null);
		Atributo atributo1_1 = new Atributo(TipoAtributo.RECORDING_MODE, "F", null, null);
		Atributo atributo1_2 = new Atributo(TipoAtributo.BLOCK_CONTAINS, "0", null, null);
		Atributo atributo1_3 = new Atributo(TipoAtributo.RECORD_CONTAINS, "500 CHARACTERS.", null, null);
		Atributo atributo1_4 = new Atributo(TipoAtributo.OPTIONAL, "FALSE", null, null);

		artefato01.adicionarAtributo(atributo1_0);
		artefato01.adicionarAtributo(atributo1_1);
		artefato01.adicionarAtributo(atributo1_2);
		artefato01.adicionarAtributo(atributo1_3);
		artefato01.adicionarAtributo(atributo1_4);
		// ***
		Artefato artefato02 = new Artefato("ENTRADA2", null, null, TipoArtefato.FILE_DESCRIPTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo2_0 = new Atributo(TipoAtributo.COBOL_DDNAME, "E2DQ9730", null, null);
		Atributo atributo2_1 = new Atributo(TipoAtributo.RECORDING_MODE, "F", null, null);
		Atributo atributo2_2 = new Atributo(TipoAtributo.BLOCK_CONTAINS, "0", null, null);
		Atributo atributo2_3 = new Atributo(TipoAtributo.RECORD_CONTAINS, "160 CHARACTERS.", null, null);
		Atributo atributo2_4 = new Atributo(TipoAtributo.OPTIONAL, "FALSE", null, null);
		Atributo atributo2_5 = new Atributo(TipoAtributo.DATA_RECORD, "RG-FENTRADA1", null, null);

		artefato02.adicionarAtributo(atributo2_0);
		artefato02.adicionarAtributo(atributo2_1);
		artefato02.adicionarAtributo(atributo2_2);
		artefato02.adicionarAtributo(atributo2_3);
		artefato02.adicionarAtributo(atributo2_4);
		artefato02.adicionarAtributo(atributo2_5);
		// ***
		Artefato artefato03 = new Artefato("SALIDA1", null, null, TipoArtefato.FILE_DESCRIPTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo3_0 = new Atributo(TipoAtributo.COBOL_DDNAME, "S1DQ9730", null, null);
		Atributo atributo3_1 = new Atributo(TipoAtributo.RECORDING_MODE, "F", null, null);
		Atributo atributo3_2 = new Atributo(TipoAtributo.BLOCK_CONTAINS, "0", null, null);
		Atributo atributo3_3 = new Atributo(TipoAtributo.RECORD_CONTAINS, "395 CHARACTERS.", null, null);
		Atributo atributo3_4 = new Atributo(TipoAtributo.OPTIONAL, "FALSE", null, null);

		artefato03.adicionarAtributo(atributo3_0);
		artefato03.adicionarAtributo(atributo3_1);
		artefato03.adicionarAtributo(atributo3_2);
		artefato03.adicionarAtributo(atributo3_3);
		artefato03.adicionarAtributo(atributo3_4);
		// ***
		listaArtefato.add(artefato01);
		listaArtefato.add(artefato02);
		listaArtefato.add(artefato03);

		// ***

		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(artefato.getArtefatosRelacionados().contains(entryArtefato));
			Artefato subentryArtefato = artefato.getArtefatosRelacionados().stream()
					.filter(p -> p.equals(entryArtefato)).collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = artefato.getArtefatosRelacionados().stream()
				.filter(p -> TipoArtefato.FILE_DESCRIPTION.equals(p.getTipoArtefato())).collect(Collectors.toList())
				.size();

		assertEquals(listaArtefato.size(), contador);
	}

	@Test
	public void test_declaracao_sql() {
		List<Artefato> listaArtefato = new ArrayList<>();

		// ***
		Artefato artefato10 = new Artefato("DECLARACAO-SQL-1", null, null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo10_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "EXEC SQL INCLUDE SQLCA END-EXEC.", null,
				null);
		artefato10.adicionarAtributo(atributo10_0);
		// ***
		Artefato artefato11 = new Artefato("DECLARACAO-SQL-2", null, null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo11_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "EXEC SQL INCLUDE PCSTBH02 END-EXEC.",
				null, null);
		artefato11.adicionarAtributo(atributo11_0);
		// ***
		Artefato artefato12 = new Artefato("DECLARACAO-SQL-3", null, null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo12_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "EXEC SQL INCLUDE MPDT083 END-EXEC.",
				null, null);
		artefato12.adicionarAtributo(atributo12_0);
		// ***
		Artefato artefato13 = new Artefato("DECLARACAO-SQL-4", null, null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo13_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL UPDATE MPDT083 SET NUMSEC = :DCLMPDT083.NUMSEC, INDICA = :DCLMPDT083.INDICA, PROGRAMA = :DCLMPDT083.PROGRAMA, CADENA = :DCLMPDT083.CADENA, DATOS = :DCLMPDT083.DATOS WHERE (NUMSEC = :DCLMPDT083.NUMSEC) AND (INDICA = :DCLMPDT083.INDICA) AND (PROGRAMA = :DCLMPDT083.PROGRAMA) AND (CADENA = :DCLMPDT083.CADENA) END-EXEC.",
				null, null);
		artefato13.adicionarAtributo(atributo13_0);
		// ***
		Artefato artefato14 = new Artefato("DECLARACAO-SQL-5", null, null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo14_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL SELECT CO_ENTIDADE , CO_EMPSA_PARCEIRA , CO_PRDTO_EMPRESA , NO_PRDTO_PARCEIRA , IC_SITUACAO , CO_CODENTUMO , CO_CODOFIUMO , CO_USUARIOUMO , CO_CODTERMUMO , TS_CONTCUR INTO :PCSTBH02.CO-ENTIDADE , :PCSTBH02.CO-EMPSA-PARCEIRA , :PCSTBH02.CO-PRDTO-EMPRESA , :PCSTBH02.NO-PRDTO-PARCEIRA , :PCSTBH02.IC-SITUACAO , :PCSTBH02.CO-CODENTUMO , :PCSTBH02.CO-CODOFIUMO , :PCSTBH02.CO-USUARIOUMO , :PCSTBH02.CO-CODTERMUMO , :PCSTBH02.TS-CONTCUR FROM PCSTBH02_PRDO_PRCA WHERE CO_ENTIDADE = :CT-0104 AND CO_EMPSA_PARCEIRA = :WS-PARCEI-PAG AND CO_PRDTO_EMPRESA = :WS-PRODU-PAG END-EXEC.",
				null, null);
		artefato14.adicionarAtributo(atributo14_0);
		// ***
		Artefato artefato15 = new Artefato("DECLARACAO-SQL-6", null, null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo15_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL SELECT CURRENT TIMESTAMP INTO :WS-FECHA-CURR FROM SYSIBM.SYSDUMMY1 END-EXEC.", null, null);
		artefato15.adicionarAtributo(atributo15_0);
		// ***
		Artefato artefato16 = new Artefato("DECLARACAO-SQL-7", null, null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo16_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL DELETE FROM PCSTBH02_PRDO_PRCA WHERE CO_ENTIDADE = :CT-0104 AND CO_EMPSA_PARCEIRA = :WS-PARCEI-PAG AND CO_PRDTO_EMPRESA = :WS-PRODU-PAG END-EXEC.",
				null, null);
		artefato16.adicionarAtributo(atributo16_0);
		// ***
		Artefato artefato17 = new Artefato("DECLARACAO-SQL-8", null, null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo17_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL INSERT INTO MPDT083 (NUMSEC, INDICA, PROGRAMA, CADENA, DATOS) VALUES (:TB-083NUMSEC, :TB-083INDICA, :TB-083PROGRAMA, :TB-083CADENA, :TB-083DATOS) FOR :IND-TB083 ROWS END-EXEC.",
				null, null);
		artefato17.adicionarAtributo(atributo17_0);

		listaArtefato.add(artefato10);
		listaArtefato.add(artefato11);
		listaArtefato.add(artefato12);
		listaArtefato.add(artefato13);
		listaArtefato.add(artefato14);
		listaArtefato.add(artefato15);
		listaArtefato.add(artefato16);
		listaArtefato.add(artefato17);

		// ***

		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(artefato.getArtefatosRelacionados().contains(entryArtefato));
			Artefato subentryArtefato = artefato.getArtefatosRelacionados().stream()
					.filter(p -> p.equals(entryArtefato)).collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = artefato.getArtefatosRelacionados().stream()
				.filter(p -> TipoArtefato.DECLARACAO_SQL.equals(p.getTipoArtefato())).collect(Collectors.toList())
				.size();

		assertEquals(listaArtefato.size(), contador);
	}

	@Test
	public void test_tabelas() {
		List<Artefato> listaArtefato = new ArrayList<>();

		Artefato artefato01 = new Artefato("MPDT083", null, null, TipoArtefato.TABELA, TipoAmbiente.PRD, "DESCONHECIDO",
				null, null);
		// ***
		Artefato artefato02 = new Artefato("PCSTBH02_PRDO_PRCA", null, null, TipoArtefato.TABELA, TipoAmbiente.PRD,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato03 = new Artefato("SYSDUMMY1", null, null, TipoArtefato.TABELA, TipoAmbiente.PRD,
				"DESCONHECIDO", null, null);
		// ***
		listaArtefato.add(artefato01);
		listaArtefato.add(artefato02);
		listaArtefato.add(artefato03);

		// ***

		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(artefato.getArtefatosRelacionados().contains(entryArtefato));
			Artefato subentryArtefato = artefato.getArtefatosRelacionados().stream()
					.filter(p -> p.equals(entryArtefato)).collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = artefato.getArtefatosRelacionados().stream()
				.filter(p -> TipoArtefato.TABELA.equals(p.getTipoArtefato())).collect(Collectors.toList()).size();

		assertEquals(listaArtefato.size(), contador);
	}

	@Test
	public void test_copybooks() {
		List<Artefato> listaArtefato = new ArrayList<>();

		// ***
		Artefato artefato01 = new Artefato("ATSQLERR", null, null, TipoArtefato.COPYBOOK, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato02 = new Artefato("PCSDS182", null, null, TipoArtefato.COPYBOOK, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato03 = new Artefato("PCSDSD49", null, null, TipoArtefato.COPYBOOK, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato04 = new Artefato("PCSDS001", null, null, TipoArtefato.COPYBOOK, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);

		// ***
		listaArtefato.add(artefato01);
		listaArtefato.add(artefato02);
		listaArtefato.add(artefato03);
		listaArtefato.add(artefato04);
		// ***

		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(artefato.getArtefatosRelacionados().contains(entryArtefato));
			Artefato subentryArtefato = artefato.getArtefatosRelacionados().stream()
					.filter(p -> p.equals(entryArtefato)).collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = artefato.getArtefatosRelacionados().stream()
				.filter(p -> TipoArtefato.COPYBOOK.equals(p.getTipoArtefato())).collect(Collectors.toList()).size();

		assertEquals(listaArtefato.size(), contador);
	}

	@Test
	public void test_programas() {
		List<Artefato> listaArtefato = new ArrayList<>();

		// ***
		Artefato artefato01 = new Artefato("PCSPSD49", null, null, TipoArtefato.PROGRAMA_COBOL,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null);
		// ***
		Artefato artefato02 = new Artefato("ATRG005", null, null, TipoArtefato.PROGRAMA_COBOL,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null);
		// ***
		Artefato artefato03 = new Artefato("ATR980", null, null, TipoArtefato.PROGRAMA_COBOL, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato04 = new Artefato("MQOPEN", null, null, TipoArtefato.PROGRAMA_COBOL, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		listaArtefato.add(artefato01);
		listaArtefato.add(artefato02);
		listaArtefato.add(artefato03);
		listaArtefato.add(artefato04);

		// ***

		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(artefato.getArtefatosRelacionados().contains(entryArtefato));
			Artefato subentryArtefato = artefato.getArtefatosRelacionados().stream()
					.filter(p -> p.equals(entryArtefato)).collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = artefato.getArtefatosRelacionados().stream()
				.filter(p -> TipoArtefato.PROGRAMA_COBOL.equals(p.getTipoArtefato())).collect(Collectors.toList())
				.size();

		assertEquals(listaArtefato.size(), contador);
	}

	@Test
	public void test_execucao_cics() {
		List<Artefato> listaArtefato = new ArrayList<>();

		// ***
		Artefato artefato01 = new Artefato("SYNCPOINT", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		// ***
		Artefato artefato02 = new Artefato("ASKTIME", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo2_0 = new Atributo(TipoAtributo.CICS_ABSTIME, "WS-ABSTIME", null, null);
		artefato02.adicionarAtributo(atributo2_0);
		// ***
		Artefato artefato03 = new Artefato("FORMATTIME", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo3_0 = new Atributo(TipoAtributo.CICS_ABSTIME, "WS-ABSTIME", null, null);
		Atributo atributo3_1 = new Atributo(TipoAtributo.CICS_TIME, "WS-HORA-EDI", null, null);
		artefato03.adicionarAtributo(atributo3_0);
		artefato03.adicionarAtributo(atributo3_1);
		// ***
		Artefato artefato04 = new Artefato("RETRIEVE", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo4_0 = new Atributo(TipoAtributo.CICS_INTO, "MQM-TRIGGER-MESSAGE", null, null);
		artefato04.adicionarAtributo(atributo4_0);
		// ***
		Artefato artefato05 = new Artefato("RETURN", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		// ***
		Artefato artefato06 = new Artefato("START", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo6_0 = new Atributo(TipoAtributo.CICS_TRANSACTION_ID, "CT-PCX5", null, null);
		Atributo atributo6_1 = new Atributo(TipoAtributo.CICS_FROM, "WS-MENSAJE", null, null);
		Atributo atributo6_2 = new Atributo(TipoAtributo.CICS_LENGTH, "LENGTH OF WS-MENSAJE", null, null);
		artefato06.adicionarAtributo(atributo6_0);
		artefato06.adicionarAtributo(atributo6_1);
		artefato06.adicionarAtributo(atributo6_2);
		// ***
		Artefato artefato07 = new Artefato("READQ", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo7_0 = new Atributo(TipoAtributo.CICS_QNAME, "WS-TS-ALTERACAO", null, null);
		Atributo atributo7_1 = new Atributo(TipoAtributo.CICS_INTO, "WS-CONTEUDO-TS", null, null);
		Atributo atributo7_2 = new Atributo(TipoAtributo.CICS_LENGTH, "LENGTH OF WS-CONTEUDO-TS", null, null);
		Atributo atributo7_3 = new Atributo(TipoAtributo.CICS_ITEM, "WS-ITEM-TS", null, null);
		Atributo atributo7_4 = new Atributo(TipoAtributo.CICS_NUMITEMS, "WS-NUM-ITENS-TS", null, null);

		artefato07.adicionarAtributo(atributo7_0);
		artefato07.adicionarAtributo(atributo7_1);
		artefato07.adicionarAtributo(atributo7_2);
		artefato07.adicionarAtributo(atributo7_3);
		artefato07.adicionarAtributo(atributo7_4);
		// ***
		Artefato artefato08 = new Artefato("WRITEQ", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo8_0 = new Atributo(TipoAtributo.CICS_QNAME, "WS-TS-ALTERACAO", null, null);
		Atributo atributo8_1 = new Atributo(TipoAtributo.CICS_FROM, "WS-CONTEUDO-TS", null, null);
		Atributo atributo8_2 = new Atributo(TipoAtributo.CICS_LENGTH, "LENGTH OF WS-CONTEUDO-TS", null, null);
		Atributo atributo8_3 = new Atributo(TipoAtributo.CICS_NUMITEMS, "WS-NUM-ITENS-TS", null, null);

		artefato08.adicionarAtributo(atributo8_0);
		artefato08.adicionarAtributo(atributo8_1);
		artefato08.adicionarAtributo(atributo8_2);
		artefato08.adicionarAtributo(atributo8_3);
		// ***
		Artefato artefato09 = new Artefato("CT-ATE790", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo9_0 = new Atributo(TipoAtributo.CICS_COMMAREA, "WS-DATOS", null, null);
		Atributo atributo9_1 = new Atributo(TipoAtributo.CICS_LENGTH, "LENGTH OF WS-DATOS", null, null);
		artefato09.adicionarAtributo(atributo9_0);
		artefato09.adicionarAtributo(atributo9_1);
		// ***
		Artefato artefato10 = new Artefato("WS-PCSPOS10", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo10_0 = new Atributo(TipoAtributo.CICS_COMMAREA, "WS-COMMAREA-PCSPOS10", null, null);
		Atributo atributo10_1 = new Atributo(TipoAtributo.CICS_LENGTH, "LENGTH OF WS-COMMAREA-PCSPOS10", null, null);
		artefato10.adicionarAtributo(atributo10_0);
		artefato10.adicionarAtributo(atributo10_1);
		// ***
		Artefato artefato11 = new Artefato("CONDITION", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo11_0 = new Atributo(TipoAtributo.CICS_ENDDATA, "9999-ERROR-CICS", null, null);
		Atributo atributo11_1 = new Atributo(TipoAtributo.CICS_ERROR, "9999-ERROR-CICS", null, null);
		artefato11.adicionarAtributo(atributo11_0);
		artefato11.adicionarAtributo(atributo11_1);
		// ***
		Artefato artefato12 = new Artefato("ABEND", null, null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo12_0 = new Atributo(TipoAtributo.CICS_LABEL, "9999-ERROR-CICS", null, null);
		artefato12.adicionarAtributo(atributo12_0);
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

		// ***

		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(artefato.getArtefatosRelacionados().contains(entryArtefato));
			Artefato subentryArtefato = artefato.getArtefatosRelacionados().stream()
					.filter(p -> p.equals(entryArtefato)).collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = artefato.getArtefatosRelacionados().stream()
				.filter(p -> TipoArtefato.CICS_TRANSACTION.equals(p.getTipoArtefato())).collect(Collectors.toList())
				.size();

		assertTrue(listaArtefato.size() <= contador);
	}

}
