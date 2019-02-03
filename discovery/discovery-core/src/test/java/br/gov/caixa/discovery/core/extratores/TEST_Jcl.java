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

public class TEST_Jcl {

	private static Extrator converter = null;
	private static String pasta = "D:\\ti\\git\\discovery\\discovery\\discovery-core\\src\\test\\resources\\";
	private static String arquivo = pasta + "jcl_generico.cbl";
	private static Artefato artefato = null;
	private static String[] argumentos = { " --ambiente PRD --sistema SIPCS " };

	@BeforeClass
	public static void setUp() throws Exception {
		Configuracao.carregar(argumentos);
		converter = new Extrator();
		converter.inicializar(arquivo, TipoArtefato.JCL);
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
		assertTrue("jcl_generico".equals(artefato.getNome()));
	}

	@Test
	public void test_nome_interno() {
		assertTrue("PCS3C650".equals(artefato.getNomeInterno()));
	}

	@Test
	public void test_steps() {
		List<Artefato> listaArtefato = new ArrayList<>();

		Artefato artefato01 = new Artefato("STEP01", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato02 = new Artefato("STEP02", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato03 = new Artefato("STEP03", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato04 = new Artefato("STEP04", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato05 = new Artefato("STEP05", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato06 = new Artefato("STEP06", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato07 = new Artefato("STEP07", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato08 = new Artefato("STEP08", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato09 = new Artefato("STEP09", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato10 = new Artefato("STEP10", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato11 = new Artefato("STEP11", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato12 = new Artefato("STEP12", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato13 = new Artefato("STEP13", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
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
				.filter(p -> TipoArtefato.JCL_STEP.equals(p.getTipoArtefato())).collect(Collectors.toList()).size();

		assertEquals(listaArtefato.size(), contador);
	}

	@Test
	public void test_programas_utilitarios() {
		List<Artefato> listaArtefato = new ArrayList<>();

		Artefato artefato01 = new Artefato("IDCAMS", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato02 = new Artefato("IEFBR14", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato03 = new Artefato("IKJEFT1A", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato04 = new Artefato("PCSPBD06", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato05 = new Artefato("SORT", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato06 = new Artefato("IDCAMS", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato07 = new Artefato("IDCAMS", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato08 = new Artefato("ECEPB609", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato09 = new Artefato("ICEGENER", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato10 = new Artefato("CNTPCSSP", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato11 = new Artefato("DMBATCH", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato12 = new Artefato("CTMAPI", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato13 = new Artefato("PCSBT663", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato14 = new Artefato("IKJEFT01", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato15 = new Artefato("IKJEFT01", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
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

		// ***
		List<Artefato> listaExtracao = getArtefatosRelacionados(artefato);

		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(listaExtracao.contains(entryArtefato));
			Artefato subentryArtefato = listaExtracao.stream().filter(p -> p.equals(entryArtefato))
					.collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = listaExtracao.stream().filter(p -> TipoArtefato.UTILITARIO.equals(p.getTipoArtefato()))
				.collect(Collectors.toList()).size();
		contador += listaExtracao.stream().filter(p -> TipoArtefato.PROGRAMA_COBOL.equals(p.getTipoArtefato()))
				.collect(Collectors.toList()).size();
		contador += listaExtracao.stream().filter(p -> TipoArtefato.DESCONHECIDO.equals(p.getTipoArtefato()))
				.collect(Collectors.toList()).size();

		assertEquals(listaArtefato.size(), contador);
	}

	@Test
	public void test_declaracoes_sql() {
		List<Artefato> listaArtefato = new ArrayList<>();

		Artefato artefato01 = new Artefato("DECLARACAO-SQL-1", null, null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		Atributo atributo01_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"SELECT CO_ENTIDADE, CO_AGENCIA_INCLUSAO, CO_CONTA_CONTRATO, NU_ANO_EXTRATO FROM PCS.PCSTBD58_EXTRATO_ANUAL;",
				null, "ARTEFATO");
		artefato01.adicionarAtributo(atributo01_1);
		// ***
		Artefato artefato02 = new Artefato("DECLARACAO-SQL-2", null, null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		Atributo atributo02_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"SELECT CODENT , CENTALTA , CUENTA , NUMSECHIS , PAN , CODCOM , CHAR(FECHAMOD,ISO) , HORAMOD , TIPACCES , PROCESO , DESPROCESO , DATOMOD , MOTIVO , CANALMOD , CODENTUMO , CODOFIUMO , USUARIOUMO , CODTERMUMO , CONTCUR , TABLA , REGANT , REGACT FROM PCS.MPDT068 WHERE MOTIVO IN ('RO','RM', 'MB' ,'MM','MO') AND TABLA IN ('174') ORDER BY CODENT, CENTALTA, CUENTA, NUMSECHIS WITH UR;",
				null, "ARTEFATO");
		artefato02.adicionarAtributo(atributo02_1);
		// ***
		listaArtefato.add(artefato01);
		listaArtefato.add(artefato02);
		// ***
		List<Artefato> listaExtracao = getArtefatosRelacionados(artefato);

		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(listaExtracao.contains(entryArtefato));
			Artefato subentryArtefato = listaExtracao.stream().filter(p -> p.equals(entryArtefato))
					.collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = listaExtracao.stream().filter(p -> TipoArtefato.DECLARACAO_SQL.equals(p.getTipoArtefato()))
				.collect(Collectors.toList()).size();

		assertEquals(listaArtefato.size(), contador);
	}

	@Test
	public void test_tabelas() {
		List<Artefato> listaArtefato = new ArrayList<>();

		Artefato artefato01 = new Artefato("PCSTBD58_EXTRATO_ANUAL", null, null, TipoArtefato.TABELA,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato02 = new Artefato("MPDT068", null, null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		listaArtefato.add(artefato01);
		listaArtefato.add(artefato02);
		// ***
		List<Artefato> listaExtracao = getArtefatosRelacionados(artefato);

		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(listaExtracao.contains(entryArtefato));
			Artefato subentryArtefato = listaExtracao.stream().filter(p -> p.equals(entryArtefato))
					.collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = listaExtracao.stream().filter(p -> {
			return TipoArtefato.TABELA.equals(p.getTipoArtefato());
		}).collect(Collectors.toList()).size();

		assertEquals(listaArtefato.size(), contador);
	}

	@Test
	public void test_dsn() {
		List<Artefato> listaArtefato = new ArrayList<>();

		Artefato artefato01 = new Artefato("PCS.MZ.BZX0.MDA1B4C2.B446.S03.FRESPOK", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato02 = new Artefato("PCS.MZ.BDT2.PCS650.ST10.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato03 = new Artefato("PCS.MZ.BZX0.JBD22.PBD06.DS4B0.SORT", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato04 = new Artefato("PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato05 = new Artefato("PCS.MZ.BDS2.JBD22.PBD08.DS4B0.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato06 = new Artefato("PCS.MZ.BDS2.JBD22.PBD08.DS102.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato07 = new Artefato("PCS.MZ.BZX0.MC01B473.B473.S02.FSUNEC01", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato08 = new Artefato("PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato09 = new Artefato("PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato10 = new Artefato("PCS.MZ.BZX0.JBD22.PBD06.DS4B0.SORT", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato11 = new Artefato("PCS.MZ.BZX0.MOUTSORT.MONE.S01.FPAG0001", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato12 = new Artefato("PCS.MZ.BZX0.MDA1B4Q7.C761.S01.SELECT", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato13 = new Artefato("PCS.MZ.BZX0.MDA1BD5C.C761.S02.SELECT", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato14 = new Artefato("PCS.MZ.BZX0.MDA1BD5C.C761.S02.SIERED5D", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato15 = new Artefato("PCS.MZ.BZX0.MDA1BD5D.C761.S02.SELECT", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato16 = new Artefato("PCS.MZ.BDD2.MDA1BD5D.C761.S02.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato17 = new Artefato("PCS.MZ.BHX0.PBD5D.RELAT.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato18 = new Artefato("PCS.MZ.BZX0.MRJT0201.MIGRACAO.ACUM", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato19 = new Artefato("PCS.MZ.BDN2.MA03B414.B417.S16.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato20 = new Artefato("CNT.PCS.MZ.BGT1.IGAP0323.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Artefato artefato21 = new Artefato("PCS.MZ.BDS2.DATA.PROCBAT.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato22 = new Artefato("PCS.MZ.BDS2.P3C010.ARQCTBL.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato23 = new Artefato("PCS.MZ.BDS2.BT663.MC.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato24 = new Artefato("DB2.%%DB2GRP.RUNLIB.LOAD", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato25 = new Artefato("%%CARDLIB(UNLOAD)", null, null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato26 = new Artefato("PCS.MZ.BZX0.M001B907.B907.S05.FTAB58", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato27 = new Artefato("DB2.%%DB2GRP.RUNLIB.LOAD", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato28 = new Artefato("%%CARDLIB(UNLOAD)", null, null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato29 = new Artefato("PCS.MZ.BZX0.PCS1S302.MPDT174", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato30 = new Artefato("IBM.CND.SDGALINK", null, null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato31 = new Artefato("PRD.V01.PROCESS.CNT.PLEX02", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato32 = new Artefato("CND.P6.MSG", null, null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato33 = new Artefato("CND.P6.NETMAP", null, null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato34 = new Artefato("%%VCNDT%%..NETMAP", null, null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		Artefato artefato35 = new Artefato("FDL.MZ.BDD2.MDA1BD5D.C761.S02.D%%ODATE", null, null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIFDL", null, null, TipoRelacionamento.INTERFACE);

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
		listaArtefato.add(artefato28);
		listaArtefato.add(artefato29);
		listaArtefato.add(artefato30);
		listaArtefato.add(artefato31);
		listaArtefato.add(artefato32);
		listaArtefato.add(artefato33);
		listaArtefato.add(artefato34);
		listaArtefato.add(artefato35);

		// ***

		List<Artefato> listaExtracao = getArtefatosRelacionados(artefato).stream().filter((p) -> {
			// if (TipoArtefato.DSN.equals(p.getTipoArtefato()))
			// System.err.println(p);
			return TipoArtefato.DSN.equals(p.getTipoArtefato());
		}).collect(Collectors.toList());

		for (Artefato entryArtefato : listaArtefato) {
			// System.out.println(entryArtefato);
			assertTrue(listaExtracao.contains(entryArtefato));
		}

		int contador = listaExtracao.size();

		assertEquals(listaArtefato.size(), contador);
	}

	private List<Artefato> getArtefatosRelacionados(Artefato artefato) {
		List<Artefato> output = new ArrayList<>();

		if (artefato == null) {
			return output;
		}

		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			output.add(entry);
			output.addAll(getArtefatosRelacionados(entry));
		}

		return output;
	}

}
