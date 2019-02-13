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
import br.gov.caixa.discovery.ejb.tipos.Tabelas;

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

		Artefato artefato01 = new Artefato("STEP01", "STEP01", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato02 = new Artefato("STEP02", "STEP02", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato03 = new Artefato("STEP03", "STEP03", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato04 = new Artefato("STEP04", "STEP04", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato05 = new Artefato("STEP05", "STEP05", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato06 = new Artefato("STEP06", "STEP06", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato07 = new Artefato("STEP07", "STEP07", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato08 = new Artefato("STEP08", "STEP08", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato09 = new Artefato("STEP09", "STEP09", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato10 = new Artefato("STEP10", "STEP10", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato11 = new Artefato("STEP11", "STEP11", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato12 = new Artefato("STEP12", "STEP12", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato13 = new Artefato("STEP13", "STEP13", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato14 = new Artefato("STEP14", "STEP14", null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
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
		listaArtefato.add(artefato14);

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

		Artefato artefato01 = new Artefato("IDCAMS", "IDCAMS", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato02 = new Artefato("IEFBR14", "IEFBR14", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato03 = new Artefato("IKJEFT1A", "IKJEFT1A", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato04 = new Artefato("PCSPBD06", "PCSPBD06", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato05 = new Artefato("SORT", "SORT", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato06 = new Artefato("IDCAMS", "IDCAMS", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato07 = new Artefato("IDCAMS", "IDCAMS", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato08 = new Artefato("ECEPB609", "ECEPB609", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato09 = new Artefato("ICEGENER", "ICEGENER", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato10 = new Artefato("CNTPCSSP", "CNTPCSSP", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato11 = new Artefato("DMBATCH", "DMBATCH", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato12 = new Artefato("CTMAPI", "CTMAPI", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato13 = new Artefato("PCSBT663", "PCSBT663", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato14 = new Artefato("IKJEFT01", "IKJEFT01", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato15 = new Artefato("IKJEFT01", "IKJEFT01", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato16 = new Artefato("DMBATCH", "DMBATCH", null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
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
		listaArtefato.add(artefato16);

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

		Artefato artefato01 = new Artefato("DECLARACAO-SQL-1", "DECLARACAO-SQL-1", null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		Atributo atributo01_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"SELECT CO_ENTIDADE, CO_AGENCIA_INCLUSAO, CO_CONTA_CONTRATO, NU_ANO_EXTRATO FROM PCS.PCSTBD58_EXTRATO_ANUAL;",
				null, "ARTEFATO");
		artefato01.adicionarAtributo(atributo01_1);
		// ***
		Artefato artefato02 = new Artefato("DECLARACAO-SQL-2", "DECLARACAO-SQL-2", null, TipoArtefato.DECLARACAO_SQL,
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

		Artefato artefato01 = new Artefato("PCSTBD58_EXTRATO_ANUAL", "PCSTBD58_EXTRATO_ANUAL", null, TipoArtefato.TABELA,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		// ***
		Artefato artefato02 = new Artefato("MPDT068", "MPDT068", null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO,
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

		Artefato artefato01 = new Artefato("PCS.MZ.BZX0.MDA1B4C2.B446.S03.FRESPOK", "PCS.MZ.BZX0.MDA1B4C2.B446.S03.FRESPOK", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato02 = new Artefato("PCS.MZ.BDT2.PCS650.ST10.D%%ODATE", "PCS.MZ.BDT2.PCS650.ST10.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato03 = new Artefato("PCS.MZ.BZX0.JBD22.PBD06.DS4B0.SORT", "PCS.MZ.BZX0.JBD22.PBD06.DS4B0.SORT", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato04 = new Artefato("PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE", "PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato05 = new Artefato("PCS.MZ.BDS2.JBD22.PBD08.DS4B0.D%%ODATE", "PCS.MZ.BDS2.JBD22.PBD08.DS4B0.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato06 = new Artefato("PCS.MZ.BDS2.JBD22.PBD08.DS102.D%%ODATE", "PCS.MZ.BDS2.JBD22.PBD08.DS102.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato07 = new Artefato("PCS.MZ.BZX0.MC01B473.B473.S02.FSUNEC01", "PCS.MZ.BZX0.MC01B473.B473.S02.FSUNEC01", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato08 = new Artefato("PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE", "PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato09 = new Artefato("PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE", "PCS.MZ.BDS2.JBD22.PBD06.DS4B0.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato10 = new Artefato("PCS.MZ.BZX0.JBD22.PBD06.DS4B0.SORT", "PCS.MZ.BZX0.JBD22.PBD06.DS4B0.SORT", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato11 = new Artefato("PCS.MZ.BZX0.MOUTSORT.MONE.S01.FPAG0001", "PCS.MZ.BZX0.MOUTSORT.MONE.S01.FPAG0001", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato12 = new Artefato("PCS.MZ.BZX0.MDA1B4Q7.C761.S01.SELECT", "PCS.MZ.BZX0.MDA1B4Q7.C761.S01.SELECT", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato13 = new Artefato("PCS.MZ.BZX0.MDA1BD5C.C761.S02.SELECT", "PCS.MZ.BZX0.MDA1BD5C.C761.S02.SELECT", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato14 = new Artefato("PCS.MZ.BZX0.MDA1BD5C.C761.S02.SIERED5D", "PCS.MZ.BZX0.MDA1BD5C.C761.S02.SIERED5D", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato15 = new Artefato("PCS.MZ.BZX0.MDA1BD5D.C761.S02.SELECT", "PCS.MZ.BZX0.MDA1BD5D.C761.S02.SELECT", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato16 = new Artefato("PCS.MZ.BDD2.MDA1BD5D.C761.S02.D%%ODATE", "PCS.MZ.BDD2.MDA1BD5D.C761.S02.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato17 = new Artefato("PCS.MZ.BHX0.PBD5D.RELAT.D%%ODATE", "PCS.MZ.BHX0.PBD5D.RELAT.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato18 = new Artefato("PCS.MZ.BZX0.MRJT0201.MIGRACAO.ACUM", "PCS.MZ.BZX0.MRJT0201.MIGRACAO.ACUM", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato19 = new Artefato("PCS.MZ.BDN2.MA03B414.B417.S16.D%%ODATE", "PCS.MZ.BDN2.MA03B414.B417.S16.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato20 = new Artefato("CNT.PCS.MZ.BGT1.IGAP0323.D%%ODATE", "CNT.PCS.MZ.BGT1.IGAP0323.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo20_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "SUGAP", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato20.adicionarAtributo(atributo20_1);
		
		Artefato artefato21 = new Artefato("PCS.MZ.BDS2.DATA.PROCBAT.D%%ODATE", "PCS.MZ.BDS2.DATA.PROCBAT.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato22 = new Artefato("PCS.MZ.BDS2.P3C010.ARQCTBL.D%%ODATE", "PCS.MZ.BDS2.P3C010.ARQCTBL.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato23 = new Artefato("PCS.MZ.BDS2.BT663.MC.D%%ODATE", "PCS.MZ.BDS2.BT663.MC.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato24 = new Artefato("DB2.%%DB2GRP.RUNLIB.LOAD", "DB2.%%DB2GRP.RUNLIB.LOAD", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato25 = new Artefato("%%CARDLIB(UNLOAD)", "%%CARDLIB(UNLOAD)", null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato26 = new Artefato("PCS.MZ.BZX0.M001B907.B907.S05.FTAB58", "PCS.MZ.BZX0.M001B907.B907.S05.FTAB58", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato27 = new Artefato("DB2.%%DB2GRP.RUNLIB.LOAD", "DB2.%%DB2GRP.RUNLIB.LOAD", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato28 = new Artefato("%%CARDLIB(UNLOAD)", "%%CARDLIB(UNLOAD)", null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato29 = new Artefato("PCS.MZ.BZX0.PCS1S302.MPDT174", "PCS.MZ.BZX0.PCS1S302.MPDT174", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato30 = new Artefato("IBM.CND.SDGALINK", "%%VCNDC", null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato31 = new Artefato("PRD.V01.PROCESS.CNT.PLEX02", "%%CNT", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato32 = new Artefato("CND.P6.MSG", "%%VCNDP%%..MSG", null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato33 = new Artefato("CND.P6.NETMAP", "%%VCNDP%%..NETMAP", null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato34 = new Artefato("%%VCNDT%%..NETMAP", "%%VCNDT%%..NETMAP", null, TipoArtefato.DSN, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato35 = new Artefato("FDL.MZ.BDD2.MDA1BD5D.C761.S02.D%%ODATE", "FDL.MZ.BDD2.MDA1BD5D.C761.S02.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIFDL", null, null, TipoRelacionamento.INTERFACE);
		
		
		Artefato artefato36 = new Artefato("PCS.MZ.BDS2.RETOREL1.PCS3C014.D%%ODATE", "PCS.MZ.BDS2.RETOREL1.PCS3C014.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato37 = new Artefato("CNT.PCS.MZ.BDS2.INELO104.OS3C014.D%%ODATE", "CNT.PCS.MZ.BDS2.INELO104.OS3C014.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo37_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "EXTERNO", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato37.adicionarAtributo(atributo37_1);
		
		
		Artefato artefato38 = new Artefato("PCS.MZ.BDA2.PC833.S1DQ831.D%%ODATE", "PCS.MZ.BDA2.PC833.S1DQ831.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato39 = new Artefato("CNT.FIN.MZ.BDC2.IPCSQ831.D%%ODATE", "CNT.FIN.MZ.BDC2.IPCSQ831.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SISFIN", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo39_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "SIPCS", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato39.adicionarAtributo(atributo39_1);
		
		Artefato artefato40 = new Artefato("PCS.MZ.BZX0.MA01B424.B496.S02.FSOLPIN1", "PCS.MZ.BZX0.MA01B424.B496.S02.FSOLPIN1", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato41 = new Artefato("CNT.RAN.MZ.BAT1.IPCS4A8E.D%%ODATE", "CNT.RAN.MZ.BAT1.IPCS4A8E.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIRAN", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo41_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "SIPCS", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato41.adicionarAtributo(atributo41_1);

		
		Artefato artefato42 = new Artefato("PCS.MZ.BDN2.MA02B414.B417.S22.D%%ODATE", "PCS.MZ.BDN2.MA02B414.B417.S22.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato43 = new Artefato("CNT.PCS.MZ.BGT1.IGAP0224.D%%ODATE", "CNT.PCS.MZ.BGT1.IGAP0224.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo43_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "SUGAP", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato43.adicionarAtributo(atributo43_1);

		Artefato artefato44 = new Artefato("PCS.MZ.BDG2.B511.S31.FRENCIX1.D%%ODATE.ZIP", "PCS.MZ.BDG2.B511.S31.FRENCIX1.D%%ODATE.ZIP", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato45 = new Artefato("CNT.PCS.MZ.BZX0.FRENCIX1.D%%ODATE.ZIP", "CNT.PCS.MZ.BZX0.FRENCIX1.D%%ODATE.ZIP", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo45_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "SIPCS", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato45.adicionarAtributo(atributo45_1);
		
		Artefato artefato46 = new Artefato("PCS.MZ.BDG2.B511.S31.FRENCIX1.D%%ODATE", "PCS.MZ.BDG2.B511.S31.FRENCIX1.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato47 = new Artefato("CNT.PCS.MZ.BZX0.FRENCIX1.D%%ODATE", "CNT.PCS.MZ.BZX0.FRENCIX1.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo47_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "SIPCS", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato47.adicionarAtributo(atributo47_1);
		
		Artefato artefato48 = new Artefato("CNT.PCS.MZ.BDS2.INELOAGL.RE3EMISS.D%%ODATE", "CNT.PCS.MZ.BDS2.INELOAGL.RE3EMISS.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo48_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "EXTERNO", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato48.adicionarAtributo(atributo48_1);
		
		Artefato artefato49 = new Artefato("CNT.PCS.MZ.BDS2.INELOAGE.R2EMISS.D%%ODATE", "CNT.PCS.MZ.BDS2.INELOAGE.R2EMISS.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo49_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "EXTERNO", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato49.adicionarAtributo(atributo49_1);
		
		Artefato artefato50 = new Artefato("PCS.MZ.BDS2.AGEN.LIQD.D%%ODATE", "PCS.MZ.BDS2.AGEN.LIQD.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato51 = new Artefato("PCS.MZ.BZX0.IOCR0424.JCB00024", "PCS.MZ.BZX0.IOCR0424.JCB00024", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato52 = new Artefato("CNT.PCS.MZ.BDC2.IOCR0424.D%%ODATE", "CNT.PCS.MZ.BDC2.IOCR0424.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo52_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "SUGAP", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato52.adicionarAtributo(atributo52_1);
		
		Artefato artefato53 = new Artefato("PCS.MZ.BZX0.IOCR0424.JCB00024.TRABALHO", "PCS.MZ.BZX0.IOCR0424.JCB00024.TRABALHO", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato54 = new Artefato("PCS.MZ.BDB2.P2C808.DS897.LTE020.D%%ODATE", "PCS.MZ.BDB2.P2C808.DS897.LTE020.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato55 = new Artefato("CNT.CAC.MZ.BAT1.IPCSM897.D%%ODATE", "CNT.CAC.MZ.BAT1.IPCSM897.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SICAC", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo55_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "SIPCS", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato55.adicionarAtributo(atributo55_1);
		
		Artefato artefato56 = new Artefato("SIPUBPCS.MZ.BHX0.P393.ANALIT.BOLETO.D%%ODATE", "SIPUBPCS.MZ.BHX0.P393.ANALIT.BOLETO.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo56_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "SIGDB", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato56.adicionarAtributo(atributo56_1);
		
		Artefato artefato57 = new Artefato("CNT.PCS.MZ.BDC2.ICAIXSEG.D%%ODATE", "CNT.PCS.MZ.BDC2.ICAIXSEG.D%%ODATE", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo57_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "EXTERNO", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato57.adicionarAtributo(atributo57_1);
		
		
		Artefato artefato58 = new Artefato("PCS.MZ.BDQ2.PC015.DS114.D%%ODATE.GDB", "PCS.MZ.BDQ2.PC015.DS114.D%%ODATE.GDB", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);

		Artefato artefato59 = new Artefato("CNT.PCS.MZ.BDS2.IJCB.SETTSRPT.BRL.D%%DTANT", "CNT.PCS.MZ.BDS2.IJCB.SETTSRPT.BRL.D%%DTANT", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo59_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "EXTERNO", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato59.adicionarAtributo(atributo59_1);
		
		
		Artefato artefato60 = new Artefato("PCS.V00.SAT.MPDT999.UNLOAD", "PCS.V00.SAT.MPDT999.UNLOAD", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato61 = new Artefato("CEE.SCEERUN", "CEE.SCEERUN", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato62 = new Artefato("CNT.PCS.MZ.BDS2.IJCB.ACTVRPT.USD.D%%DTANT", "CNT.PCS.MZ.BDS2.IJCB.ACTVRPT.USD.D%%DTANT", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo62_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "EXTERNO", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato62.adicionarAtributo(atributo62_1);
		
		Artefato artefato63 = new Artefato("CNT.PCS.MZ.BDS2.IJCB.ACTVRPT.BRL.D%%DTANT", "CNT.PCS.MZ.BDS2.IJCB.ACTVRPT.BRL.D%%DTANT", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.INTERFACE);
		Atributo atributo63_1 = new Atributo(TipoAtributo.SISTEMA_DESTINO, "EXTERNO", null, Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
		artefato63.adicionarAtributo(atributo63_1);
		
		Artefato artefato64 = new Artefato("PCS.MZ.BZX0.MPJ32100.FCCON001", "PCS.MZ.BZX0.MPJ32100.FCCON001", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "SIPCS", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato65 = new Artefato("ZIP.LOAD", "ZIP.LOAD", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato66 = new Artefato("TCP.NETRC.PLEX%%AMB", "TCP.NETRC.PLEX%%AMB", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
		Artefato artefato67 = new Artefato("NDS.CTM.V01.PCS.D%%ODATE.PLEX02", "NDS.CTM.V01.PCS.D%%ODATE.PLEX02", null, TipoArtefato.DSN,
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null, TipoRelacionamento.NORMAL);
		
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
		listaArtefato.add(artefato36);
		listaArtefato.add(artefato37);
		listaArtefato.add(artefato38);
		listaArtefato.add(artefato39);
		listaArtefato.add(artefato40);
		listaArtefato.add(artefato41);
		listaArtefato.add(artefato42);
		listaArtefato.add(artefato43);
		listaArtefato.add(artefato44);
		listaArtefato.add(artefato45);
		listaArtefato.add(artefato46);
		listaArtefato.add(artefato47);
		listaArtefato.add(artefato48);
		listaArtefato.add(artefato49);
		listaArtefato.add(artefato50);
		listaArtefato.add(artefato51);
		listaArtefato.add(artefato52);
		listaArtefato.add(artefato53);
		listaArtefato.add(artefato54);
		listaArtefato.add(artefato55);
		listaArtefato.add(artefato56);
		listaArtefato.add(artefato57);
		listaArtefato.add(artefato58);
		listaArtefato.add(artefato59);
		listaArtefato.add(artefato60);
		listaArtefato.add(artefato61);
		listaArtefato.add(artefato62);
		listaArtefato.add(artefato63);
		listaArtefato.add(artefato64);
		listaArtefato.add(artefato65);
		listaArtefato.add(artefato66);
		listaArtefato.add(artefato67);

		// ***

		List<Artefato> listaExtracao = getArtefatosRelacionados(artefato).stream().filter((p) -> {
			return TipoArtefato.DSN.equals(p.getTipoArtefato());
		}).collect(Collectors.toList());

		for (Artefato entryArtefato : listaArtefato) {
			assertTrue(listaExtracao.contains(entryArtefato));

			Artefato subentryArtefato = listaExtracao.stream().filter(p -> p.equals(entryArtefato))
					.collect(Collectors.toList()).get(0);

			for (Atributo entryAtributo : entryArtefato.getAtributos()) {
				assertTrue(subentryArtefato.getAtributos().contains(entryAtributo));
			}
		}

		int contador = listaExtracao.size();

		assertEquals(listaArtefato.size(), contador);
	}

	private List<Artefato> getArtefatosRelacionados(Artefato artefato) {
		List<Artefato> output = new ArrayList<>();

		if (artefato == null || artefato.getArtefatosRelacionados() == null || artefato.getArtefatosRelacionados().size() == 0) {
			return output;
		}

	
		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			output.add(entry);
			output.addAll(getArtefatosRelacionados(entry));
		}

		return output;
	}

}
