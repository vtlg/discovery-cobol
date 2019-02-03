package br.gov.caixa.discovery.injetores;

import java.nio.file.Path;
import java.util.ArrayList;
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

			_executar(Configuracao.getConfiguracao(TipoArtefato.JCL), TipoArtefato.JCL, 0);

			dao.fecharConexao();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void _executar(ArquivoConfiguracao arquivoConfiguracao, TipoArtefato tipoArtefato,
			int deslocamento) throws Exception {

		List<Path> listaPaths = _getListaPaths(arquivoConfiguracao, tipoArtefato);

		for (Path path : listaPaths) {
			Extrator extrator = new Extrator();
			extrator.inicializar(path.toAbsolutePath().toString(), tipoArtefato, deslocamento);
			List<Artefato> listaArtefato = extrator.converter();
			List<ArtefatoPersistence> listaArtefatoPersistence = new ArrayList<>();

			for (Artefato entry : listaArtefato) {
				if (TipoArtefato.CONTROL_M.equals(tipoArtefato)) {
					listaArtefatoPersistence.addAll(Conversor.executar(entry, true));
				} else {
					listaArtefatoPersistence.addAll(Conversor.executar(entry, false));
				}
			}

			for (ArtefatoPersistence entry : listaArtefatoPersistence) {
				if (TipoArtefato.CONTROL_M.equals(tipoArtefato)) {
					Injetor.executar(em, entry, true);
				} else {
					Injetor.executar(em, entry, false);
				}
			}
		}
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
