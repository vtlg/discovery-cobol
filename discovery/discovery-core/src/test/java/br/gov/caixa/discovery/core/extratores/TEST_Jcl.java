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
				null);
		// ***
		Artefato artefato02 = new Artefato("STEP02", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
		// ***
		Artefato artefato03 = new Artefato("STEP03", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
		// ***
		Artefato artefato04 = new Artefato("STEP04", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
		// ***
		Artefato artefato05 = new Artefato("STEP05", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
		// ***
		Artefato artefato06 = new Artefato("STEP06", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
		// ***
		Artefato artefato07 = new Artefato("STEP07", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
		// ***
		Artefato artefato08 = new Artefato("STEP08", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
		// ***
		Artefato artefato09 = new Artefato("STEP09", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
		// ***
		Artefato artefato10 = new Artefato("STEP10", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
		// ***
		Artefato artefato11 = new Artefato("STEP11", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
		// ***
		Artefato artefato12 = new Artefato("STEP12", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
		// ***
		Artefato artefato13 = new Artefato("STEP13", null, null, TipoArtefato.JCL_STEP, TipoAmbiente.PRD, "SIPCS", null,
				null);
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
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato02 = new Artefato("IEFBR14", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato03 = new Artefato("IKJEFT1A", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato04 = new Artefato("PCSPBD06", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato05 = new Artefato("SORT", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato06 = new Artefato("IDCAMS", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato07 = new Artefato("IDCAMS", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato08 = new Artefato("ECEPB609", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato09 = new Artefato("ICEGENER", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato10 = new Artefato("CNTPCSSP", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato11 = new Artefato("DMBATCH", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato12 = new Artefato("CTMAPI", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato13 = new Artefato("PCSBT663", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato14 = new Artefato("IKJEFT01", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
		// ***
		Artefato artefato15 = new Artefato("IKJEFT01", null, null, TipoArtefato.DESCONHECIDO, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
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
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo01_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"SELECT CO_ENTIDADE, CO_AGENCIA_INCLUSAO, CO_CONTA_CONTRATO, NU_ANO_EXTRATO FROM PCS.PCSTBD58_EXTRATO_ANUAL;",
				null, null);
		artefato01.adicionarAtributo(atributo01_1);
		// ***
		Artefato artefato02 = new Artefato("DECLARACAO-SQL-2", null, null, TipoArtefato.DECLARACAO_SQL,
				TipoAmbiente.PRD, "SIPCS", null, null);
		Atributo atributo02_1 = new Atributo(TipoAtributo.DECLARACAO_COMPLETA,
				"SELECT CODENT , CENTALTA , CUENTA , NUMSECHIS , PAN , CODCOM , CHAR(FECHAMOD,ISO) , HORAMOD , TIPACCES , PROCESO , DESPROCESO , DATOMOD , MOTIVO , CANALMOD , CODENTUMO , CODOFIUMO , USUARIOUMO , CODTERMUMO , CONTCUR , TABLA , REGANT , REGACT FROM PCS.MPDT068 WHERE MOTIVO IN ('RO','RM', 'MB' ,'MM','MO') AND TABLA IN ('174') ORDER BY CODENT, CENTALTA, CUENTA, NUMSECHIS WITH UR;",
				null, null);
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
				TipoAmbiente.DESCONHECIDO, "DESCONHECIDO", null, null);
		// ***
		Artefato artefato02 = new Artefato("MPDT068", null, null, TipoArtefato.TABELA, TipoAmbiente.DESCONHECIDO,
				"DESCONHECIDO", null, null);
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

		int contador = listaExtracao.stream().filter(p -> TipoArtefato.TABELA.equals(p.getTipoArtefato()))
				.collect(Collectors.toList()).size();

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
