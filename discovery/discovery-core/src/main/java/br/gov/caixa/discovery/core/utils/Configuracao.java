package br.gov.caixa.discovery.core.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import br.gov.caixa.discovery.core.modelos.ArquivoConfiguracao;
import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.tipos.TipoAmbiente;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;

public class Configuracao {

	public static boolean CARGA_FULL = false;
	public static boolean MOVER_ARQUIVOS = false;
	public static String SISTEMA;
	public static TipoAmbiente AMBIENTE;
	public static int NUMERO_THREAD = 10;
	public static Calendar TS_ATUAL = Calendar.getInstance();

	public static Collection<Artefato> COLLECTION_ARTEFATO = new HashSet<>();
	public static Collection<ArquivoConfiguracao> COLLECTION_ARQUIVO_CONFIGURACAO = new HashSet<>();

	/**
	 * @param args <br>
	 *             --ambiente : Ambiente PRE, REL, DES, TQS, HMP, PRD <br>
	 *             --sistema : SIPCS, SIFD. <br>
	 *             --data : (OPCIONAL) Data da extração no formato dd/MM/aaaa. <br>
	 *             --mover-arquivos : (OPCIONAL) Caso setado, os arquivos
	 *             convertidos serão movidos para subpasta ./arquivos_processados
	 */
	public static void carregar(String[] args) {
		LoggerImpl.configurarFileLog(Level.INFO);
		_carregarParametro(args);
		_carregarConfiguracaoJson();
		_carregarCollectionArtefatos();
	}

	public static ArquivoConfiguracao getConfiguracao(TipoArtefato tipopArtefato) {
		for (ArquivoConfiguracao entry : COLLECTION_ARQUIVO_CONFIGURACAO) {
			if (AMBIENTE.get().equals(entry.getAmbiente()) && SISTEMA.equals(entry.getSistema())
					&& tipopArtefato.get().equals(entry.getTipo())) {
				return entry;
			}
		}
		return null;
	}

	private static void _carregarParametro(String[] args) {
		String parametros = UtilsHandler.joinArray(args, ' ') + " ";
		Pattern P_AMBIENTE = Pattern.compile("^.*--ambiente[\\s]{1,}(?<parametro>[\\S]{1,}).*$");
		Pattern P_SISTEMA = Pattern.compile("^.*--sistema[\\s]{1,}(?<parametro>[\\S]{1,}).*$");
		Pattern P_DATA_ATUALIZACAO = Pattern.compile("^.*--data[\\s]{1,}(?<parametro>[\\S]{1,}).*$");
		Pattern P_MOVER_ARQUIVOS = Pattern.compile("^.*--mover-arquivos.*$");
		Pattern P_CARGA_FULL = Pattern.compile("^.*--carga-full.*$");

		Matcher m_carga_full = P_CARGA_FULL.matcher(parametros);
		Matcher m_mover_arquivos = P_MOVER_ARQUIVOS.matcher(parametros);
		Matcher m_ambiente = P_AMBIENTE.matcher(parametros);
		Matcher m_sistema = P_SISTEMA.matcher(parametros);
		Matcher m_data = P_DATA_ATUALIZACAO.matcher(parametros);

		if (m_carga_full.matches()) {
			CARGA_FULL = true;
		} else {
			CARGA_FULL = false;
		}

		if (m_mover_arquivos.matches()) {
			MOVER_ARQUIVOS = true;
		} else {
			MOVER_ARQUIVOS = false;
		}

		if (m_ambiente.matches()) {
			AMBIENTE = TipoAmbiente.valueOf(m_ambiente.group("parametro"));
		} else {
			throw new RuntimeException("O parâmetro '--ambiente <ambiente>' é obrigatório");
		}
		if (m_sistema.matches()) {
			SISTEMA = m_sistema.group("parametro");
		} else {
			throw new RuntimeException("O parâmetro '--sistema <sistema>' é obrigatório");
		}
		if (m_data.matches()) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			String stringData = m_data.group("parametro");
			try {
				stringData = stringData + " " + "00:00:01";
				Date dataAtualizacao = sdf.parse(stringData);
				TS_ATUAL = Calendar.getInstance();
				TS_ATUAL.setTime(dataAtualizacao);
			} catch (Exception e) {
				throw new RuntimeException("Formato da data inválido. Informar data no formato dd/MM/aaaa");
			}
		} else {
			TS_ATUAL = Calendar.getInstance();
		}
	}

	private static void _carregarConfiguracaoJson() {
		Gson gson = new Gson();

		JsonReader jReader = null;
		try {
			jReader = new JsonReader(new FileReader("config.json"));
			ArquivoConfiguracao[] arrConfiguracao = gson.fromJson(jReader, ArquivoConfiguracao[].class);
			COLLECTION_ARQUIVO_CONFIGURACAO.addAll(Arrays.asList(arrConfiguracao));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} finally {
			try {
				jReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void _carregarCollectionArtefatos() {
		if (COLLECTION_ARQUIVO_CONFIGURACAO != null && COLLECTION_ARQUIVO_CONFIGURACAO.size() >= 0) {
			COLLECTION_ARQUIVO_CONFIGURACAO.stream().forEach((configuracao) -> {

			});
		}
	}
}
