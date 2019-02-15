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
import br.gov.caixa.discovery.core.tipos.TipoRelacionamento;
import br.gov.caixa.discovery.core.utils.Configuracao;

public class TEST_ProgramaCobol {

	private static Extrator converter = null;
	private static String pasta = "D:\\ti\\git\\discovery\\discovery\\discovery-core\\src\\test\\resources\\";
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
		Atributo atributo1_0 = new Atributo(TipoAtributo.COBOL_DDNAME, "E1DQ9730", null, "ARTEFATO");
		Atributo atributo1_1 = new Atributo(TipoAtributo.RECORDING_MODE, "F", null, "ARTEFATO");
		Atributo atributo1_2 = new Atributo(TipoAtributo.BLOCK_CONTAINS, "0", null, "ARTEFATO");
		Atributo atributo1_3 = new Atributo(TipoAtributo.RECORD_CONTAINS, "500 CHARACTERS.", null, "ARTEFATO");
		Atributo atributo1_4 = new Atributo(TipoAtributo.OPTIONAL, "FALSE", null, "ARTEFATO");

		artefato01.adicionarAtributo(atributo1_0);
		artefato01.adicionarAtributo(atributo1_1);
		artefato01.adicionarAtributo(atributo1_2);
		artefato01.adicionarAtributo(atributo1_3);
		artefato01.adicionarAtributo(atributo1_4);
		// ***
		Artefato artefato02 = new Artefato("ENTRADA2", null, null, TipoArtefato.FILE_DESCRIPTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo2_0 = new Atributo(TipoAtributo.COBOL_DDNAME, "E2DQ9730", null, "ARTEFATO");
		Atributo atributo2_1 = new Atributo(TipoAtributo.RECORDING_MODE, "F", null, "ARTEFATO");
		Atributo atributo2_2 = new Atributo(TipoAtributo.BLOCK_CONTAINS, "0", null, "ARTEFATO");
		Atributo atributo2_3 = new Atributo(TipoAtributo.RECORD_CONTAINS, "160 CHARACTERS.", null, "ARTEFATO");
		Atributo atributo2_4 = new Atributo(TipoAtributo.OPTIONAL, "FALSE", null, "ARTEFATO");
		Atributo atributo2_5 = new Atributo(TipoAtributo.DATA_RECORD, "RG-FENTRADA1", null, "ARTEFATO");

		artefato02.adicionarAtributo(atributo2_0);
		artefato02.adicionarAtributo(atributo2_1);
		artefato02.adicionarAtributo(atributo2_2);
		artefato02.adicionarAtributo(atributo2_3);
		artefato02.adicionarAtributo(atributo2_4);
		artefato02.adicionarAtributo(atributo2_5);
		// ***
		Artefato artefato03 = new Artefato("SALIDA1", null, null, TipoArtefato.FILE_DESCRIPTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo3_0 = new Atributo(TipoAtributo.COBOL_DDNAME, "S1DQ9730", null, "ARTEFATO");
		Atributo atributo3_1 = new Atributo(TipoAtributo.RECORDING_MODE, "F", null, "ARTEFATO");
		Atributo atributo3_2 = new Atributo(TipoAtributo.BLOCK_CONTAINS, "0", null, "ARTEFATO");
		Atributo atributo3_3 = new Atributo(TipoAtributo.RECORD_CONTAINS, "395 CHARACTERS.", null, "ARTEFATO");
		Atributo atributo3_4 = new Atributo(TipoAtributo.OPTIONAL, "FALSE", null, "ARTEFATO");

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
		Artefato artefato01 = new Artefato("DECLARACAO-SQL-1", "DECLARACAO-SQL-1", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo01_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "EXEC SQL INCLUDE SQLCA END-EXEC.", null,
				"ARTEFATO");
		artefato01.adicionarAtributo(atributo01_0);
		// ***
		Artefato artefato02 = new Artefato("DECLARACAO-SQL-2", "DECLARACAO-SQL-2", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo02_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "EXEC SQL INCLUDE PCSTBH02 END-EXEC.",
				null, "ARTEFATO");
		artefato02.adicionarAtributo(atributo02_0);
		// ***
		Artefato artefato03 = new Artefato("DECLARACAO-SQL-3", "DECLARACAO-SQL-3", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo03_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA, "EXEC SQL INCLUDE MPDT083 END-EXEC.",
				null, "ARTEFATO");
		artefato03.adicionarAtributo(atributo03_0);
		// ***
		Artefato artefato04 = new Artefato("DECLARACAO-SQL-4", "DECLARACAO-SQL-4", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo04_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL UPDATE MPDT083 SET NUMSEC = :DCLMPDT083.NUMSEC, INDICA = :DCLMPDT083.INDICA, PROGRAMA = :DCLMPDT083.PROGRAMA, CADENA = :DCLMPDT083.CADENA, DATOS = :DCLMPDT083.DATOS WHERE (NUMSEC = :DCLMPDT083.NUMSEC) AND (INDICA = :DCLMPDT083.INDICA) AND (PROGRAMA = :DCLMPDT083.PROGRAMA) AND (CADENA = :DCLMPDT083.CADENA) END-EXEC.",
				null, "ARTEFATO");
		artefato04.adicionarAtributo(atributo04_0);
		// ***
		Artefato artefato05 = new Artefato("DECLARACAO-SQL-5", "DECLARACAO-SQL-5", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, "ARTEFATO");
		Atributo atributo05_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL SELECT CO_ENTIDADE , CO_EMPSA_PARCEIRA , CO_PRDTO_EMPRESA , NO_PRDTO_PARCEIRA , IC_SITUACAO , CO_CODENTUMO , CO_CODOFIUMO , CO_USUARIOUMO , CO_CODTERMUMO , TS_CONTCUR INTO :PCSTBH02.CO-ENTIDADE , :PCSTBH02.CO-EMPSA-PARCEIRA , :PCSTBH02.CO-PRDTO-EMPRESA , :PCSTBH02.NO-PRDTO-PARCEIRA , :PCSTBH02.IC-SITUACAO , :PCSTBH02.CO-CODENTUMO , :PCSTBH02.CO-CODOFIUMO , :PCSTBH02.CO-USUARIOUMO , :PCSTBH02.CO-CODTERMUMO , :PCSTBH02.TS-CONTCUR FROM PCSTBH02_PRDO_PRCA WHERE CO_ENTIDADE = :CT-0104 AND CO_EMPSA_PARCEIRA = :WS-PARCEI-PAG AND CO_PRDTO_EMPRESA = :WS-PRODU-PAG END-EXEC.",
				null, "ARTEFATO");
		artefato05.adicionarAtributo(atributo05_0);
		// ***
		Artefato artefato06 = new Artefato("DECLARACAO-SQL-6", "DECLARACAO-SQL-6", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo06_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL SELECT CURRENT TIMESTAMP INTO :WS-FECHA-CURR FROM SYSIBM.SYSDUMMY1 END-EXEC.", null, "ARTEFATO");
		artefato06.adicionarAtributo(atributo06_0);
		// ***
		Artefato artefato07 = new Artefato("DECLARACAO-SQL-7", "DECLARACAO-SQL-7", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo07_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL SELECT T743.CODPORIEMI INTO :DCLMPDT743.CODPORIEMI FROM PCS.MPDT743 T743 INNER JOIN PCS.MPDT007 T007 ON T743.CODENT = T007.CODENT AND T743.PRODUCTO = T007.PRODUCTO AND T743.SUBPRODU = T007.SUBPRODU AND T743.CODCOSIF = 3 INNER JOIN PCS.MPDT013 T013 ON T007.CODENT = T013.CODENT AND T007.CENTALTA = T013.CENTALTA AND T007.CUENTA = T013.CUENTA INNER JOIN PCS.MPDT414 T414 ON T013.CODENT = T414.CODENT AND T013.IDENTCLI = T414.IDENTCLI WHERE T007.CODENT = :WS-CODENT-GDA AND T007.CENTALTA = :WS-CENTALTA-GDA AND T007.CUENTA = :WS-CUENTA-GDA AND T013.CALPART = 'TI' WITH UR END-EXEC.",
				null, "ARTEFATO");
		artefato07.adicionarAtributo(atributo07_0);
		// ***
		Artefato artefato08 = new Artefato("DECLARACAO-SQL-8", "DECLARACAO-SQL-8", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo08_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL SELECT MAX(A.FECFAC) INTO :DCLMPDT251.FECFAC FROM PCS.MPDT251 A JOIN PCS.MPDT044 B ON A.TIPOFAC = B.TIPOFAC AND A.INDNORCOR = B.INDNORCOR WHERE A.CODENT = :DCLMPDT251.CODENT AND A.CENTALTA = :DCLMPDT251.CENTALTA AND A.CUENTA = :DCLMPDT251.CUENTA AND A.CLAMON = :DCLMPDT251.CLAMON AND B.TIPOFACSIST = 67 AND B.SIGNO = '-' AND B.INDFACINF = 'N' WITH UR END-EXEC.",
				null, "ARTEFATO");
		artefato08.adicionarAtributo(atributo08_0);
		// ***
		Artefato artefato09 = new Artefato("DECLARACAO-SQL-9", "DECLARACAO-SQL-9", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo09_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL SELECT T9.PAN, T178.CODBLQ, T178.TEXBLQ INTO :DCLMPDT009.PAN, :DCLMPDT178.CODBLQ :WS-CODBLQ-NULO , :DCLMPDT178.TEXBLQ :WS-TEXBLQ-NULO FROM MPDT009 T9 LEFT OUTER JOIN MPDT178 T178 ON T9.CODENT = T178.CODENT AND T9.CENTALTA = T178.CENTALTA AND T9.CUENTA = T178.CUENTA WHERE T9.CODENT = :DCLMPDT009.CODENT AND T9.CENTALTA = :DCLMPDT009.CENTALTA AND T9.CUENTA = :DCLMPDT009.CUENTA AND T9.INDULTTAR = 'S' AND T9.NUMBENCTA = 1 END-EXEC.",
				null, "ARTEFATO");
		artefato09.adicionarAtributo(atributo09_0);
		// ***
		Artefato artefato10 = new Artefato("DECLARACAO-SQL-10", "DECLARACAO-SQL-10", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo10_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL DECLARE CUR_402_013 CURSOR FOR SELECT B.CODENT, B.CENTALTA, B.CUENTA, B.NUMBENCTA, B.CALPART, B.FECBAJA FROM MPDT402 A JOIN MPDT013 B ON A.CODENT = B.CODENT AND A.IDENTCLI = B.IDENTCLI AND A.CENTALTA = B.CENTALTA AND A.CUENTA = B.CUENTA WHERE A.CODENT = :DCLMPDT402.CODENT AND A.IDENTCLI = :DCLMPDT402.IDENTCLI AND A.TIPCONT = :DCLMPDT402.TIPCONT AND B.FECBAJA = :CT-FECINI ORDER BY B.CODENT,B.CENTALTA,B.CUENTA,B.NUMBENCTA FETCH FIRST 16 ROWS ONLY OPTIMIZE FOR 1 ROW END-EXEC.",
				null, "ARTEFATO");
		artefato10.adicionarAtributo(atributo10_0);
		// ***
		Artefato artefato11 = new Artefato("DECLARACAO-SQL-11", "DECLARACAO-SQL-11", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo11_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL DELETE FROM PCSTBH02_PRDO_PRCA WHERE CO_ENTIDADE = :CT-0104 AND CO_EMPSA_PARCEIRA = :WS-PARCEI-PAG AND CO_PRDTO_EMPRESA = :WS-PRODU-PAG END-EXEC.",
				null, "ARTEFATO");
		artefato11.adicionarAtributo(atributo11_0);
		// ***
		Artefato artefato12 = new Artefato("DECLARACAO-SQL-12", "DECLARACAO-SQL-12", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo12_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL INSERT INTO MPDT083 (NUMSEC, INDICA, PROGRAMA, CADENA, DATOS) VALUES (:TB-083NUMSEC, :TB-083INDICA, :TB-083PROGRAMA, :TB-083CADENA, :TB-083DATOS) FOR :IND-TB083 ROWS END-EXEC.",
				null, "ARTEFATO");
		artefato12.adicionarAtributo(atributo12_0);
		// ***
		Artefato artefato13 = new Artefato("DECLARACAO-SQL-13", "DECLARACAO-SQL-13", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo13_0 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"EXEC SQL DECLARE CUR_FDLTB005 CURSOR FOR SELECT CO_ENTIDADE ,CO_PERFIL_ACESSO ,NU_TIPO_OPERACAO_FIDELIZACAO ,IC_ACAO ,DE_ACAO ,CO_TERMINAL_ATUALIZACAO ,CO_USUARIO_ATUALIZACAO ,TS_ULTIMA_ALTERACAO FROM FDL.FDLTB005_ACAO_ASCDA_PRFL_ACSSO WHERE CO_ENTIDADE =:FDLTB005.CO-ENTIDADE AND CO_PERFIL_ACESSO =:FDLTB005.CO-PERFIL-ACESSO FOR UPDATE END-EXEC.",
				null, "ARTEFATO");
		artefato13.adicionarAtributo(atributo13_0);

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

		Artefato artefato01 = new Artefato("MPDT083", "MPDT083", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO, "DESCONHECIDO",
				null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato02 = new Artefato("PCSTBH02_PRDO_PRCA", "PCSTBH02_PRDO_PRCA", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato03 = new Artefato("SYSDUMMY1", "SYSDUMMY1", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato04 = new Artefato("MPDT251", "MPDT251", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO, "DESCONHECIDO",
				null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato05 = new Artefato("MPDT044", "MPDT044", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO, "DESCONHECIDO",
				null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato06 = new Artefato("MPDT743", "MPDT743", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO, "DESCONHECIDO",
				null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato07 = new Artefato("MPDT013", "MPDT013", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO, "DESCONHECIDO",
				null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato08 = new Artefato("MPDT414", "MPDT414", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO, "DESCONHECIDO",
				null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato09 = new Artefato("MPDT009", "MPDT009", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO, "DESCONHECIDO",
				null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato10 = new Artefato("MPDT178", "MPDT178", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO, "DESCONHECIDO",
				null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato11 = new Artefato("MPDT402", "MPDT402", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO, "DESCONHECIDO",
				null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato12 = new Artefato("MPDT013", "MPDT013", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO, "DESCONHECIDO",
				null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato13 = new Artefato("FDLTB005_ACAO_ASCDA_PRFL_ACSSO", "FDLTB005_ACAO_ASCDA_PRFL_ACSSO", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO, "DESCONHECIDO",
				null, null, TipoRelacionamento.NORMAL);
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

		// ***

		List<Artefato> listaExtracao = artefato.getArtefatosRelacionados().stream()
				.filter(p -> TipoArtefato.TABELA.equals(p.getTipoArtefato())).collect(Collectors.toList());
		
		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(listaExtracao.contains(entryArtefato));
			Artefato subentryArtefato = artefato.getArtefatosRelacionados().stream()
					.filter(p -> p.equals(entryArtefato)).collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = listaExtracao.size();

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
		Artefato artefato01 = new Artefato("PCSPSD49", "CT-PCSPSD49", null, TipoArtefato.PROGRAMA_COBOL,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null);
		// ***
		Artefato artefato02 = new Artefato("ATRG005", "CT-ATRG005", null, TipoArtefato.PROGRAMA_COBOL,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null);
		// ***
		Artefato artefato03 = new Artefato("ATR980", "CT-ATR980", null, TipoArtefato.PROGRAMA_COBOL, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato04 = new Artefato("MQOPEN", "'MQOPEN'", null, TipoArtefato.PROGRAMA_COBOL, TipoAmbiente.DESCONHECIDO,
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
		Artefato artefato01 = new Artefato("SYNCPOINT", "SYNCPOINT", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		// ***
		Artefato artefato02 = new Artefato("ASKTIME", "ASKTIME", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo2_0 = new Atributo(TipoAtributo.CICS_ABSTIME, "WS-ABSTIME", null, "RELACIONAMENTO");
		artefato02.adicionarAtributo(atributo2_0);
		// ***
		Artefato artefato03 = new Artefato("FORMATTIME", "FORMATTIME", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo3_0 = new Atributo(TipoAtributo.CICS_ABSTIME, "WS-ABSTIME", null, "RELACIONAMENTO");
		Atributo atributo3_1 = new Atributo(TipoAtributo.CICS_TIME, "WS-HORA-EDI", null, "RELACIONAMENTO");
		artefato03.adicionarAtributo(atributo3_0);
		artefato03.adicionarAtributo(atributo3_1);
		// ***
		Artefato artefato04 = new Artefato("RETRIEVE", "RETRIEVE", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo4_0 = new Atributo(TipoAtributo.CICS_INTO, "MQM-TRIGGER-MESSAGE", null, "RELACIONAMENTO");
		artefato04.adicionarAtributo(atributo4_0);
		// ***
		Artefato artefato05 = new Artefato("RETURN", "RETURN", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		// ***
		Artefato artefato06 = new Artefato("START", "START", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo6_0 = new Atributo(TipoAtributo.CICS_TRANSACTION_ID, "CT-PCX5", null, "RELACIONAMENTO");
		Atributo atributo6_1 = new Atributo(TipoAtributo.CICS_FROM, "WS-MENSAJE", null, "RELACIONAMENTO");
		Atributo atributo6_2 = new Atributo(TipoAtributo.CICS_LENGTH, "LENGTH OF WS-MENSAJE", null, "RELACIONAMENTO");
		artefato06.adicionarAtributo(atributo6_0);
		artefato06.adicionarAtributo(atributo6_1);
		artefato06.adicionarAtributo(atributo6_2);
		// ***
		Artefato artefato07 = new Artefato("READQ", "READQ", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo7_0 = new Atributo(TipoAtributo.CICS_QNAME, "WS-TS-ALTERACAO", null, "RELACIONAMENTO");
		Atributo atributo7_1 = new Atributo(TipoAtributo.CICS_INTO, "WS-CONTEUDO-TS", null, "RELACIONAMENTO");
		Atributo atributo7_2 = new Atributo(TipoAtributo.CICS_LENGTH, "LENGTH OF WS-CONTEUDO-TS", null, "RELACIONAMENTO");
		Atributo atributo7_3 = new Atributo(TipoAtributo.CICS_ITEM, "WS-ITEM-TS", null, "RELACIONAMENTO");
		Atributo atributo7_4 = new Atributo(TipoAtributo.CICS_NUMITEMS, "WS-NUM-ITENS-TS", null, "RELACIONAMENTO");

		artefato07.adicionarAtributo(atributo7_0);
		artefato07.adicionarAtributo(atributo7_1);
		artefato07.adicionarAtributo(atributo7_2);
		artefato07.adicionarAtributo(atributo7_3);
		artefato07.adicionarAtributo(atributo7_4);
		// ***
		Artefato artefato08 = new Artefato("WRITEQ", "WRITEQ", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo8_0 = new Atributo(TipoAtributo.CICS_QNAME, "WS-TS-ALTERACAO", null, "RELACIONAMENTO");
		Atributo atributo8_1 = new Atributo(TipoAtributo.CICS_FROM, "WS-CONTEUDO-TS", null, "RELACIONAMENTO");
		Atributo atributo8_2 = new Atributo(TipoAtributo.CICS_LENGTH, "LENGTH OF WS-CONTEUDO-TS", null, "RELACIONAMENTO");
		Atributo atributo8_3 = new Atributo(TipoAtributo.CICS_NUMITEMS, "WS-NUM-ITENS-TS", null, "RELACIONAMENTO");

		artefato08.adicionarAtributo(atributo8_0);
		artefato08.adicionarAtributo(atributo8_1);
		artefato08.adicionarAtributo(atributo8_2);
		artefato08.adicionarAtributo(atributo8_3);
		// ***
		Artefato artefato09 = new Artefato("CT-ATE790", "CT-ATE790", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo9_0 = new Atributo(TipoAtributo.CICS_COMMAREA, "WS-DATOS", null, "RELACIONAMENTO");
		Atributo atributo9_1 = new Atributo(TipoAtributo.CICS_LENGTH, "LENGTH OF WS-DATOS", null, "RELACIONAMENTO");
		artefato09.adicionarAtributo(atributo9_0);
		artefato09.adicionarAtributo(atributo9_1);
		// ***
		Artefato artefato10 = new Artefato("WS-PCSPOS10", "WS-PCSPOS10", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo10_0 = new Atributo(TipoAtributo.CICS_COMMAREA, "WS-COMMAREA-PCSPOS10", null, "RELACIONAMENTO");
		Atributo atributo10_1 = new Atributo(TipoAtributo.CICS_LENGTH, "LENGTH OF WS-COMMAREA-PCSPOS10", null, "RELACIONAMENTO");
		artefato10.adicionarAtributo(atributo10_0);
		artefato10.adicionarAtributo(atributo10_1);
		// ***
		Artefato artefato11 = new Artefato("CONDITION", "CONDITION", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo11_0 = new Atributo(TipoAtributo.CICS_ENDDATA, "9999-ERROR-CICS", null, "RELACIONAMENTO");
		Atributo atributo11_1 = new Atributo(TipoAtributo.CICS_ERROR, "9999-ERROR-CICS", null, "RELACIONAMENTO");
		artefato11.adicionarAtributo(atributo11_0);
		artefato11.adicionarAtributo(atributo11_1);
		// ***
		Artefato artefato12 = new Artefato("ABEND", "ABEND", null, TipoArtefato.CICS_TRANSACTION, TipoAmbiente.PRD,
				"SIPCS", null, null);
		Atributo atributo12_0 = new Atributo(TipoAtributo.CICS_LABEL, "9999-ERROR-CICS", null, "RELACIONAMENTO");
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
		List<Artefato> listaExtracao = artefato.getArtefatosRelacionados().stream()
				.filter(p -> TipoArtefato.CICS_TRANSACTION.equals(p.getTipoArtefato())).collect(Collectors.toList());
		
		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(listaExtracao.contains(entryArtefato));
			Artefato subentryArtefato = artefato.getArtefatosRelacionados().stream()
					.filter(p -> p.equals(entryArtefato)).collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = listaExtracao.size();

		assertTrue(listaArtefato.size() <= contador);
	}

}
