package br.gov.caixa.discovery.injetores;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import br.gov.caixa.discovery.core.extratores.Extrator;
import br.gov.caixa.discovery.core.modelos.ArquivoConfiguracao;
import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.utils.Configuracao;
import br.gov.caixa.discovery.core.utils.UtilsHandler;
import br.gov.caixa.discovery.ejb.dao.Dao;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;

public class Discovery {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static EntityManager em = null;

	public static void main(String[] args) {
		LOGGER.log(Level.INFO, "Carregando parâmetros");
		Configuracao.carregar(args);

		try {
			Dao dao = new Dao();
			dao.abrirConexao();
			em = dao.getEmFactory().createEntityManager();

			if (Configuracao.CARGA_INICIAL == true) {
				LOGGER.log(Level.INFO, "Iniciando carga inicial");
				_executarCargaInicial(Configuracao.getConfiguracao(TipoArtefato.JCL), TipoArtefato.JCL, 0, false);
				//_executarCargaInicial(Configuracao.getConfiguracao(TipoArtefato.PROGRAMA_COBOL), TipoArtefato.PROGRAMA_COBOL, 0, false);
				//_executarCargaInicial(Configuracao.getConfiguracao(TipoArtefato.COPYBOOK), TipoArtefato.COPYBOOK, 0, false);
			}

			if (Configuracao.CARGA_INICIAL == false) {
				LOGGER.log(Level.INFO, "Iniciando carga completa");
				_executarCargaNormal(Configuracao.getConfiguracao(TipoArtefato.JCL), TipoArtefato.JCL, 0, false);
				// _executarCargaNormal(Configuracao.getConfiguracao(TipoArtefato.PROGRAMA_COBOL),
				// TipoArtefato.PROGRAMA_COBOL, 0, false);
				// _executarCargaNormal(Configuracao.getConfiguracao(TipoArtefato.COPYBOOK),
				// TipoArtefato.COPYBOOK, 0, false);
				// _executarCargaNormal(Configuracao.getConfiguracao(TipoArtefato.CONTROL_M),
				// TipoArtefato.CONTROL_M, 0, true);
			}
			dao.fecharConexao();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void _executarCargaInicial(ArquivoConfiguracao arquivoConfiguracao, TipoArtefato tipoArtefato,
			int deslocamento, boolean controlM) throws Exception {
		long startTime = Calendar.getInstance().getTimeInMillis();
		LOGGER.log(Level.INFO, "(Carga Inicial) Iniciando carregamentos dos artefatos " + tipoArtefato.get());

		List<Path> listaPaths = _getListaPaths(arquivoConfiguracao, tipoArtefato);

		em.getTransaction().begin();
		for (Path path : listaPaths) {
			Extrator extrator = new Extrator();
			extrator.inicializar(path.toAbsolutePath().toString(), tipoArtefato, deslocamento);
			List<Artefato> listaArtefato = extrator.converter();
			List<ArtefatoPersistence> listaArtefatoPersistence = new ArrayList<>();

			for (Artefato entry : listaArtefato) {
				long startTimeConversao = Calendar.getInstance().getTimeInMillis();
				LOGGER.log(Level.FINE, "Convertendo artefato (" + entry.getNome() + ")");
				listaArtefatoPersistence.addAll(Conversor.executar(entry, controlM));
				LOGGER.log(Level.FINE, "Finalizando conversão do artefato (" + entry.getNome() + "). Tempo de execução "
						+ UtilsHandler.getTempoExecucao(startTimeConversao));
			}

			for (ArtefatoPersistence entry : listaArtefatoPersistence) {
				long startTimeInsert = Calendar.getInstance().getTimeInMillis();
				if (Configuracao.CARGA_INICIAL) {
					entry.setDeHash("");
				}
				LOGGER.log(Level.FINE, "Inserindo artefato (" + entry.getNoNomeArtefato() + ")");
				Injetor.executar(em, entry, controlM);
				LOGGER.log(Level.INFO, "Finalizando inserção do artefato (" + entry.getNoNomeArtefato()
						+ "). Tempo de execução " + UtilsHandler.getTempoExecucao(startTimeInsert));
			}

			listaArtefato.clear();
			listaArtefato = null;
			listaArtefatoPersistence.clear();
			listaArtefatoPersistence = null;
			extrator = null;
			Runtime.getRuntime().gc();
		}
		em.getTransaction().commit();

		LOGGER.log(Level.INFO, "(Carga Inicial) Finalizando carregamentos dos artefatos " + tipoArtefato.get()
				+ " - Tempo execução " + UtilsHandler.getTempoExecucao(startTime));

	}

	private static void _executarCargaNormal(ArquivoConfiguracao arquivoConfiguracao, TipoArtefato tipoArtefato, int deslocamento,
			boolean controlM) throws Exception {
		long startTime = Calendar.getInstance().getTimeInMillis();
		LOGGER.log(Level.INFO, "(Carga Normal) Iniciando carregamentos dos artefatos " + tipoArtefato.get());

		List<Path> listaPaths = _getListaPaths(arquivoConfiguracao, tipoArtefato);

		for (Path path : listaPaths) {
			Extrator extrator = new Extrator();
			extrator.inicializar(path.toAbsolutePath().toString(), tipoArtefato, deslocamento);
			List<Artefato> listaArtefato = extrator.converter();
			List<ArtefatoPersistence> listaArtefatoPersistence = new ArrayList<>();

			for (Artefato entry : listaArtefato) {
				listaArtefatoPersistence.addAll(Conversor.executar(entry, controlM));
			}

			for (ArtefatoPersistence entry : listaArtefatoPersistence) {
				if (Configuracao.CARGA_INICIAL) {
					entry.setDeHash("");
				}
				em.getTransaction().begin();
				Injetor.executar(em, entry, controlM);
				em.getTransaction().commit();
			}

			listaArtefato.clear();
			listaArtefato = null;
			listaArtefatoPersistence.clear();
			listaArtefatoPersistence = null;
			extrator = null;
			Runtime.getRuntime().gc();
		}

		LOGGER.log(Level.INFO, "(Carga Final) Finalizando carregamentos dos artefatos " + tipoArtefato.get()
				+ " - Tempo execução " + UtilsHandler.getTempoExecucao(startTime));
	}

	private static List<Path> _getListaPaths(ArquivoConfiguracao arquivoConfiguracao, TipoArtefato tipoArtefato)
			throws Exception {
		List<Path> output = new ArrayList<>();

		if (arquivoConfiguracao == null) {
			throw new RuntimeException("Arquivo de configuração é nulo.");
		}
		String caminhoPasta = arquivoConfiguracao.getCaminhoPasta();
		output = UtilsHandler.recuperarListaArquivo(caminhoPasta, false);

		return output;
	}
}
