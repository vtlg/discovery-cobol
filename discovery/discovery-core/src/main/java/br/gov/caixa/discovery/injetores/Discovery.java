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
import br.gov.caixa.discovery.ejb.dao.ControleArquivoDao;
import br.gov.caixa.discovery.ejb.dao.Dao;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.ControleArquivoPersistence;

public class Discovery {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static EntityManager em = null;

	public static void main(String[] args) throws Exception {
		LOGGER.log(Level.INFO, "Carregando parâmetros");
		Configuracao.carregar(args);
		Dao dao = null;
		try {
			dao = new Dao();
			dao.abrirConexao();
			em = dao.getEmFactory().createEntityManager();

			executar(Configuracao.getConfiguracao(TipoArtefato.CATALOGO_DB), TipoArtefato.CATALOGO_DB, 0, false);
			executar(Configuracao.getConfiguracao(TipoArtefato.JCL), TipoArtefato.JCL, 0, false);
			executar(Configuracao.getConfiguracao(TipoArtefato.PROGRAMA_COBOL), TipoArtefato.PROGRAMA_COBOL, 0, false);
			executar(Configuracao.getConfiguracao(TipoArtefato.COPYBOOK), TipoArtefato.COPYBOOK, 0, false);
			executar(Configuracao.getConfiguracao(TipoArtefato.CONTROL_M), TipoArtefato.CONTROL_M, 0, true);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
			dao.fecharConexao();
		}
	}

	private static int executar(ArquivoConfiguracao arquivoConfiguracao, TipoArtefato tipoArtefato, int deslocamento,
			boolean controlM) throws Exception {
		long startTime = Calendar.getInstance().getTimeInMillis();
		if (Configuracao.CARGA_INICIAL && TipoArtefato.CONTROL_M.equals(tipoArtefato)) {
			return 0;
		} else if (Configuracao.CARGA_INICIAL) {
			em.getTransaction().begin();
		}

		StringBuilder sbErro = new StringBuilder();

		LOGGER.log(Level.INFO, "(Carga) Iniciando carga dos artefatos " + tipoArtefato.get());
		List<Path> listaPaths = Configuracao.getListaPaths(arquivoConfiguracao, tipoArtefato);
		ControleArquivoDao controleArquivoDao = new ControleArquivoDao(em);

		int count = 1;
		int sizeListaPaths = listaPaths.size();
		for (Path path : listaPaths) {
			System.out.println("(" + tipoArtefato.get() + ") " + count + "/" + sizeListaPaths);
			count++;
			// CALCULA O HASH DO ARQUIVO PARA VERIFICAR A NECESSIDADE DE PROCESSÁ-LO OU NÃO
			String hashCalculado = UtilsHandler.calcularHash(path, UtilsHandler.SHA256);
			ControleArquivoPersistence controleArquivoPersistence = controleArquivoDao
					.getControleArquivo(path.getFileName().toString());

			// Se o arquivo exisitr na base de dados e o hash for igual, pula para o próximo
			// registro
			if (hashCalculado != null && controleArquivoPersistence != null
					&& hashCalculado.equals(controleArquivoPersistence.getDeHash())) {
				continue;
			}

			if (count % 1000 == 0 && Configuracao.CARGA_INICIAL) {
				em.getTransaction().commit();
				em.getTransaction().begin();
			}

			Extrator extrator = new Extrator();
			extrator.inicializar(path.toAbsolutePath().toString(), tipoArtefato, deslocamento);

			List<Artefato> listaArtefato = null;
			try {
				listaArtefato = extrator.converter();
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Erro ao extrair arquivo (" + path.toAbsolutePath().toString() + ")", e);
				sbErro.append("Erro ao extrair arquivo (" + path.toAbsolutePath().toString() + ") Mensagem ("
						+ e.getMessage() + ")");
			}

			List<ArtefatoPersistence> listaArtefatoPersistence = new ArrayList<>();

			try {
				for (Artefato entry : listaArtefato) {
					listaArtefatoPersistence.addAll(Conversor.executar(entry, controlM));
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Erro ao converter arquivo (" + path.toAbsolutePath().toString() + ")", e);
				sbErro.append("Erro ao converter arquivo (" + path.toAbsolutePath().toString() + ") Mensagem ("
						+ e.getMessage() + ")");
			}

			for (ArtefatoPersistence entry : listaArtefatoPersistence) {
				if (Configuracao.CARGA_INICIAL) {
					entry.setDeHash("");
				}

				if (!Configuracao.CARGA_INICIAL) {
					em.getTransaction().begin();
				}

				try {
					Injetor.executar(em, entry, controlM);
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Erro ao persistir arquivo (" + path.toAbsolutePath().toString() + ")", e);
					sbErro.append("Erro ao persistir arquivo (" + path.toAbsolutePath().toString() + ") Mensagem ("
							+ e.getMessage() + ")");
				}

				if (!Configuracao.CARGA_INICIAL) {
					em.getTransaction().commit();
				}
			}

			listaArtefato.clear();
			listaArtefato = null;
			listaArtefatoPersistence.clear();
			listaArtefatoPersistence = null;
			extrator = null;

			// INCLUIR OU ATUALIZAR O CONTROLE DE ARQUIVO
			if (!Configuracao.CARGA_INICIAL) {
				boolean atualizarControle = true;
				if (controleArquivoPersistence == null) {
					controleArquivoPersistence = new ControleArquivoPersistence();
					controleArquivoPersistence.setNoNomeArquivo(path.getFileName().toString());
					atualizarControle = false;
				}

				controleArquivoPersistence.setDeHash(hashCalculado);
				controleArquivoPersistence.setIcForcarAtualizacao(false);
				controleArquivoPersistence.setTsUltimaLeitura(Configuracao.TS_ATUAL);

				em.getTransaction().begin();
				if (atualizarControle) {
					controleArquivoDao.atualizar(controleArquivoPersistence);
				} else {
					controleArquivoDao.incluir(controleArquivoPersistence);
				}
				em.getTransaction().commit();
			}
		}

		if (Configuracao.CARGA_INICIAL) {
			em.getTransaction().commit();
		}

		LOGGER.log(Level.INFO, "(Carga) Finalizando carregamentos dos artefatos " + tipoArtefato.get()
				+ " - Tempo execução " + UtilsHandler.getTempoExecucao(startTime));
		return 0;
	}

}
