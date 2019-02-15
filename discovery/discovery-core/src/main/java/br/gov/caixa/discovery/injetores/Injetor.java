package br.gov.caixa.discovery.injetores;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.persistence.EntityManager;

import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.tipos.TipoRelacionamento;
import br.gov.caixa.discovery.core.utils.Configuracao;
import br.gov.caixa.discovery.ejb.dao.ArtefatoDao;
import br.gov.caixa.discovery.ejb.dao.AtributoDao;
import br.gov.caixa.discovery.ejb.dao.RelacionamentoDao;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;

public class Injetor {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static void executar(EntityManager em, ArtefatoPersistence artefato, boolean controlM) throws Exception {

		if (Configuracao.CARGA_INICIAL == true) {
			atualizarTabelaArtefato(em, artefato);
		} else if (controlM == false) {
			atualizarTabelaArtefato(em, artefato);
			atribuirCoArtefato(em, artefato);
			unificarListasRelacionamento(em, artefato);
			verificarTipoListasRelacionamento(em, artefato);
			atualizarTabelaRelacionamento(em, artefato);
		} else {
			atualizarTabelaArtefatoControlM(em, artefato, true);
			atribuirCoArtefato(em, artefato);
			ajustarRelacionamentoParaInclusao(em, artefato);
			atualizarTabelaRelacionamento(em, artefato);
		}
	}

	private static ArtefatoPersistence ajustarRelacionamentoParaInclusao(EntityManager em, ArtefatoPersistence artefato)
			throws Exception {

		for (RelacionamentoPersistence relacionamento : artefato.getTransientListaRelacionamentos()) {

			relacionamento.setArtefato(null);
			relacionamento.setArtefatoPai(null);

			if (relacionamento.getArtefatoPai() != null) {
				ajustarRelacionamentoParaInclusao(em, relacionamento.getArtefato());
			}
		}

		return artefato;
	}

	private static ArtefatoPersistence atualizarTabelaArtefatoControlM(EntityManager em, ArtefatoPersistence artefato,

			boolean desativarPai) throws Exception {

		boolean incluirArtefato = false;

		ArtefatoDao artefatoDao = new ArtefatoDao(em);
		RelacionamentoDao relacionamentoDao = new RelacionamentoDao(em);

		String coNome = artefato.getNoNomeArtefato();
		String coTipoArtefato = artefato.getCoTipoArtefato();

		ArtefatoPersistence artefatoPesquisa = null;
		List<ArtefatoPersistence> resultListaArtefatoPesquisa = null;

		try {
			resultListaArtefatoPesquisa = artefatoDao.getListaArtefato(coNome, coTipoArtefato, null, null, true);
		} catch (EJBException e) {
			LOGGER.log(Level.SEVERE, "Erro ao pesquisar artefatos", e);
		}

		if (resultListaArtefatoPesquisa != null && resultListaArtefatoPesquisa.size() >= 2) {
			LOGGER.log(Level.WARNING, "Verificar pois não deveria retornar mais de um artefato. Nome (" + coNome
					+ ")  Tipo Artefato (" + coTipoArtefato + ")");
		} else if (resultListaArtefatoPesquisa != null && resultListaArtefatoPesquisa.size() > 0) {
			artefato = atribuirDadosArtefato(artefato, resultListaArtefatoPesquisa.get(0));
			artefatoPesquisa = resultListaArtefatoPesquisa.get(0);
		}

		if (artefatoPesquisa == null) {
			incluirArtefato = true;
		} else {
			incluirArtefato = false;
			artefato.setCoArtefato(artefatoPesquisa.getCoArtefato());
		}

		if (incluirArtefato) {
			try {
				artefatoDao.incluir(artefato);
			} catch (EJBException e) {
				LOGGER.log(Level.SEVERE, "Erro a tentar incluir artefato", e);
			}
		} else if (desativarPai) {
			relacionamentoDao.desativarControlM(artefatoPesquisa.getCoArtefato(), Configuracao.TS_ATUAL);
		}

		if (artefato.getTransientListaRelacionamentos() != null
				&& artefato.getTransientListaRelacionamentos().size() > 0) {
			for (RelacionamentoPersistence relacionamento : artefato.getTransientListaRelacionamentos()) {
				if (relacionamento.getArtefatoPai() != null) {
					atualizarTabelaArtefatoControlM(em, relacionamento.getArtefato(), false);
				}
			}
		}

		return artefato;
	}

	@SuppressWarnings("unused")
	private static ArtefatoPersistence verificarTipoListasRelacionamento(EntityManager em, ArtefatoPersistence artefato)
			throws Exception {
		if (artefato.getTransientListaRelacionamentos() != null) {
			for (RelacionamentoPersistence entry : artefato.getTransientListaRelacionamentos()) {
				ArtefatoPersistence artefatoPai = entry.getArtefatoPai();
				ArtefatoPersistence artefatoFilho = entry.getArtefato();

				if (artefatoPai != null && artefatoFilho != null && !"DESCONHECIDO".equals(artefatoPai.getCoSistema())
						&& !"DESCONHECIDO".equals(artefatoFilho.getCoSistema())
						&& !artefatoFilho.getCoSistema().equals(artefatoPai.getCoSistema())) {
					entry.setCoTipoRelacionamento(TipoRelacionamento.INTERFACE.get());
				}

				if (entry.getArtefatoPai() != null) {
					verificarTipoListasRelacionamento(em, entry.getArtefato());
				}

			}
		}

		return artefato;
	}

	private static ArtefatoPersistence unificarListasRelacionamento(EntityManager em, ArtefatoPersistence artefato)
			throws Exception {
		List<RelacionamentoPersistence> tempLista = new ArrayList<>();

		if (artefato.getTransientRelacionamentosDesativados() != null) {
			for (RelacionamentoPersistence entry : artefato.getTransientRelacionamentosDesativados()) {
				RelacionamentoPersistence newEntry = new RelacionamentoPersistence();

				newEntry.setCoArtefato(entry.getCoArtefato());
				newEntry.setCoArtefatoPai(entry.getCoArtefatoPai());
				newEntry.setCoArtefatoAnterior(entry.getCoArtefatoAnterior());
				newEntry.setCoArtefatoPosterior(entry.getCoArtefatoPosterior());
				newEntry.setCoArtefatoPrimeiro(entry.getCoArtefatoPrimeiro());
				newEntry.setCoArtefatoUltimo(entry.getCoArtefatoUltimo());
				newEntry.setCoTipoRelacionamento(entry.getCoTipoRelacionamento());
				newEntry.setIcInclusaoMalha(entry.isIcInclusaoMalha());
				newEntry.setIcInclusaoManual(entry.isIcInclusaoManual());
				newEntry.setTsInicioVigencia(artefato.getTsInicioVigencia());

				if (!relacionamentoDuplicidade(tempLista, newEntry)) {
					tempLista.add(newEntry);
				}
			}
		}

		if (artefato.getTransientListaRelacionamentos() != null) {
			for (RelacionamentoPersistence entry : artefato.getTransientListaRelacionamentos()) {
				if (!relacionamentoDuplicidade(tempLista, entry)) {
					tempLista.add(entry);
				}
			}
		}

		artefato.setTransientListaRelacionamentos(tempLista);

		return artefato;
	}

	private static boolean relacionamentoDuplicidade(List<RelacionamentoPersistence> lista,
			RelacionamentoPersistence relacionamento) throws Exception {
		StringBuilder sbRelacionamento = new StringBuilder();
		sbRelacionamento.append(relacionamento.getCoRelacionamento());
		sbRelacionamento.append(relacionamento.getCoArtefato());
		sbRelacionamento.append(relacionamento.getCoArtefatoPai());

		for (RelacionamentoPersistence entry : lista) {
			StringBuilder sbEntry = new StringBuilder();
			sbEntry.append(entry.getCoRelacionamento());
			sbEntry.append(entry.getCoArtefato());
			sbEntry.append(entry.getCoArtefatoPai());

			if (sbRelacionamento.toString().equals(sbEntry.toString())) {
				return true;
			}

		}

		return false;
	}

	private static ArtefatoPersistence atualizarTabelaRelacionamento(EntityManager em, ArtefatoPersistence artefato)
			throws Exception {

		RelacionamentoDao relacionamentoDao = new RelacionamentoDao(em);
		AtributoDao atributoDao = new AtributoDao(em);

		// System.out.println(artefato);
		for (RelacionamentoPersistence relacionamento : artefato.getTransientListaRelacionamentos()) {

			// relacionamento.setArtefato(null);
			// relacionamento.setArtefatoPai(null);
			// relacionamento.setArtefatoPrimeiro(null);
			// relacionamento.setArtefatoUltimo(null);
			// relacionamento.setArtefatoAnterior(null);
			// relacionamento.setArtefatoPosterior(null);

			if (artefato.isTransientAtualizarRelacionamentos()) {

				relacionamentoDao.incluir(relacionamento);

				for (AtributoPersistence atributo : relacionamento.getTransientListaAtributos()) {
					atributo.setCoExterno(relacionamento.getCoRelacionamento());
					atributoDao.incluir(atributo);
				}
			}

			if (relacionamento.getCoArtefatoPai() != null && relacionamento.getArtefato() != null) {
				atualizarTabelaRelacionamento(em, relacionamento.getArtefato());
			}
		}

		return artefato;
	}

	private static ArtefatoPersistence atualizarTabelaArtefato(EntityManager em, ArtefatoPersistence artefato)
			throws Exception {
		return atualizarTabelaArtefato(em, artefato, false);
	}

	@SuppressWarnings("unused")
	private static ArtefatoPersistence atualizarTabelaArtefato(EntityManager em, ArtefatoPersistence artefato,
			boolean marcaDesativarPai) throws Exception {

		boolean incluirArtefato = false;
		boolean atualizarArtefato = false;
		boolean desativarArtefato = false;

		ArtefatoDao artefatoDao = new ArtefatoDao(em);
		AtributoDao atributoDao = new AtributoDao(em);
		RelacionamentoDao relacionamentoDao = new RelacionamentoDao(em);
		String coNome = artefato.getNoNomeArtefato();
		String coTipoArtefato = artefato.getCoTipoArtefato();

		List<ArtefatoPersistence> resultListaArtefatoPesquisa = null;

		ArtefatoPersistence artefatoPesquisa = null;
		if (!TipoArtefato.DESCONHECIDO.get().equals(coTipoArtefato)) {
			try {
				resultListaArtefatoPesquisa = artefatoDao.getListaArtefato(coNome, coTipoArtefato, null,
						null, true);
			} catch (EJBException e) {
				LOGGER.log(Level.SEVERE, "Erro ao pesquisar artefatos", e);
			}

			if (resultListaArtefatoPesquisa != null && resultListaArtefatoPesquisa.size() >= 2) {
				LOGGER.log(Level.WARNING,
						"Verificar pois não deveria retornar mais de um artefato. Nome (" + coNome + ")");
			} else if (resultListaArtefatoPesquisa != null && resultListaArtefatoPesquisa.size() > 0) {
				artefato = atribuirDadosArtefato(artefato, resultListaArtefatoPesquisa.get(0));
				artefatoPesquisa = resultListaArtefatoPesquisa.get(0);
			}
		}

		if (TipoArtefato.DESCONHECIDO.get().equals(artefato.getCoTipoArtefato()) || artefatoPesquisa == null) {
			//
			// caso o tipo do artefato seja DESCONHECIDO
			//

			if (Configuracao.CARGA_INICIAL == false) {
				try {
					resultListaArtefatoPesquisa = artefatoDao.getListaArtefato(coNome, null, null, null, true);
				} catch (EJBException e) {
					LOGGER.log(Level.SEVERE, "Erro ao pesquisar artefatos", e);
				}
			}

			// boolean isAribuido = false;
			boolean hasCobol = false;
			boolean hasJcl = false;

			hasCobol = resultListaArtefatoPesquisa.stream()
					.anyMatch(p -> TipoArtefato.PROGRAMA_COBOL.get().equals(p.getCoTipoArtefato()));
			hasJcl = resultListaArtefatoPesquisa.stream()
					.anyMatch(p -> TipoArtefato.JCL.get().equals(p.getCoTipoArtefato()));

			for (ArtefatoPersistence entry : resultListaArtefatoPesquisa) {
				if (!TipoArtefato.DESCONHECIDO.get().equals(entry.getCoTipoArtefato())
						&& !TipoArtefato.DESCONHECIDO.get().equals(artefato.getCoTipoArtefato())
						&& !entry.getCoTipoArtefato().equals(artefato.getCoTipoArtefato())) {
					continue;
				}

				if (hasCobol && !TipoArtefato.PROGRAMA_COBOL.get().equals(entry.getCoTipoArtefato())) {
					continue;
				}

				artefato = atribuirDadosArtefato(artefato, entry);
				artefatoPesquisa = entry;
			}
		}

		if (resultListaArtefatoPesquisa != null && resultListaArtefatoPesquisa.size() > 0) {

			if (artefatoPesquisa == null) {
				incluirArtefato = true;
				atualizarArtefato = false;
				desativarArtefato = false;

				artefato.setTransientAtualizarRelacionamentos(true);
			} else if ((artefato.getDeHash() == null || "".equals(artefato.getDeHash().trim()))
					&& (artefatoPesquisa.getDeHash() != null && !"".equals(artefatoPesquisa.getDeHash().trim()))) {
				// ##############################################
				// Quando o artefato tiver hash null
				// ##############################################
				artefato.setCoArtefato(artefatoPesquisa.getCoArtefato());

				incluirArtefato = false;
				atualizarArtefato = false;
				desativarArtefato = false;
				artefato.setTransientAtualizarRelacionamentos(false);

			} else if ((artefato.getDeHash() != null && !"".equals(artefato.getDeHash().trim()))
					&& (artefatoPesquisa.getDeHash() == null || "".equals(artefatoPesquisa.getDeHash().trim()))) {
				// ##############################################
				// Quando o result tiver hash null
				// ##############################################
				artefato.setCoArtefato(artefatoPesquisa.getCoArtefato());

				artefatoPesquisa.setDeHash(artefato.getDeHash());
				artefatoPesquisa.setCoSistema(artefato.getCoSistema());
				artefatoPesquisa.setCoAmbiente(artefato.getCoAmbiente());
				artefatoPesquisa.setCoTipoArtefato(artefato.getCoTipoArtefato());
				artefatoPesquisa.setTsUltimaModificacao(artefato.getTsUltimaModificacao());
				artefatoPesquisa.setDeDescricaoArtefato(artefato.getDeDescricaoArtefato());
				artefatoPesquisa.setIcInclusaoManual(false);
				artefatoPesquisa.setNoNomeInterno(artefato.getNoNomeInterno());

				incluirArtefato = false;
				atualizarArtefato = true;
				desativarArtefato = false;
				artefato.setTransientAtualizarRelacionamentos(true);

			} else if ((artefato.getDeHash() != null && !"".equals(artefato.getDeHash().trim()))
					&& (artefatoPesquisa.getDeHash() != null && !"".equals(artefatoPesquisa.getDeHash().trim()))
					&& (!artefato.getDeHash().equals(artefatoPesquisa.getDeHash()))) {

				artefatoPesquisa.setTsFimVigencia(Configuracao.TS_ATUAL);
				artefato.setDeDescricaoUsuario(artefatoPesquisa.getDeDescricaoUsuario());
				artefato.setIcProcessoCritico(artefatoPesquisa.isIcProcessoCritico());

				incluirArtefato = false;
				atualizarArtefato = true;
				desativarArtefato = true;
				artefato.setTransientAtualizarRelacionamentos(true);

			} else if ((artefato.getDeHash() != null && !"".equals(artefato.getDeHash().trim()))
					&& (artefatoPesquisa.getDeHash() != null && !"".equals(artefatoPesquisa.getDeHash().trim()))
					&& (artefato.getDeHash().equals(artefatoPesquisa.getDeHash()))) {
				artefato.setCoArtefato(artefatoPesquisa.getCoArtefato());

				incluirArtefato = false;
				atualizarArtefato = false;
				desativarArtefato = false;
				marcaDesativarPai = false;
				artefato.setTransientAtualizarRelacionamentos(false);

			} else if ((artefato.getDeHash() == null || "".equals(artefato.getDeHash()))
					&& (artefatoPesquisa.getDeHash() == null || "".equals(artefatoPesquisa.getDeHash().trim()))) {
				artefato.setCoArtefato(artefatoPesquisa.getCoArtefato());
				incluirArtefato = false;
				atualizarArtefato = false;
				desativarArtefato = false;
				marcaDesativarPai = false;

				artefato.setTransientAtualizarRelacionamentos(false);
			}
			// }
		} else {
			incluirArtefato = true;
			atualizarArtefato = false;
			desativarArtefato = false;

			artefato.setTransientAtualizarRelacionamentos(true);
		}

		if (incluirArtefato && artefato.getCoArtefato() == null) {
			try {
				artefatoDao.incluir(artefato);
			} catch (EJBException e) {
				LOGGER.log(Level.SEVERE, "Erro a tentar incluir artefato", e);
			}

			if (artefato.getTransientListaAtributos() != null) {
				for (AtributoPersistence atributo : artefato.getTransientListaAtributos()) {
					atributo.setCoExterno(artefato.getCoArtefato());
					atributoDao.incluir(atributo);
				}
			}

		} else if (atualizarArtefato == true && desativarArtefato == true && Configuracao.CARGA_INICIAL == false) {
			// DESATIVAR ARTEFATO E RELACIONAMENTOS

			List<RelacionamentoPersistence> tempLista = relacionamentoDao.desativar(artefatoPesquisa.getCoArtefato(),
					artefatoPesquisa.getTsFimVigencia());

			try {
				artefatoDao.desativar(artefatoPesquisa);
			} catch (EJBException e) {
				LOGGER.log(Level.SEVERE, "Erro a tentar desativar artefato", e);
			}

			try {
				artefatoDao.incluir(artefato);
			} catch (EJBException e) {
				LOGGER.log(Level.SEVERE, "Erro a tentar incluir artefato", e);
			}

			if (tempLista != null) {
				for (RelacionamentoPersistence entry : tempLista) {
					if (

					(entry.getCoArtefato() != null && entry.getCoArtefato().equals(artefatoPesquisa.getCoArtefato()))
							|| (entry.getCoArtefatoAnterior() != null
									&& entry.getCoArtefatoAnterior().equals(artefatoPesquisa.getCoArtefato()))
							|| (entry.getCoArtefatoPosterior() != null
									&& entry.getCoArtefatoPosterior().equals(artefatoPesquisa.getCoArtefato()))
							|| (entry.getCoArtefatoPrimeiro() != null
									&& entry.getCoArtefatoPrimeiro().equals(artefatoPesquisa.getCoArtefato()))
							|| (entry.getCoArtefatoUltimo() != null
									&& entry.getCoArtefatoUltimo().equals(artefatoPesquisa.getCoArtefato()))
							|| entry.isIcInclusaoMalha() || entry.isIcInclusaoManual()) {

						if (entry.getCoArtefato().equals(artefatoPesquisa.getCoArtefato())) {
							entry.setCoArtefato(artefato.getCoArtefato());
						}

						if (entry.getCoArtefatoAnterior() != null
								&& entry.getCoArtefatoAnterior().equals(artefatoPesquisa.getCoArtefato())) {
							entry.setCoArtefatoAnterior(artefatoPesquisa.getCoArtefato());
						}

						if (entry.getCoArtefatoPosterior() != null
								&& entry.getCoArtefatoPosterior().equals(artefatoPesquisa.getCoArtefato())) {
							entry.setCoArtefatoPosterior(artefatoPesquisa.getCoArtefato());
						}

						if (entry.getCoArtefatoPrimeiro() != null
								&& entry.getCoArtefatoPrimeiro().equals(artefatoPesquisa.getCoArtefato())) {
							entry.setCoArtefatoPrimeiro(artefatoPesquisa.getCoArtefato());
						}

						if (entry.getCoArtefatoUltimo() != null
								&& entry.getCoArtefatoUltimo().equals(artefatoPesquisa.getCoArtefato())) {
							entry.setCoArtefatoUltimo(artefatoPesquisa.getCoArtefato());
						}

						if (entry.getCoArtefatoPai() != null
								&& entry.getCoArtefatoPai().equals(artefatoPesquisa.getCoArtefato())) {
							entry.setCoArtefatoPai(artefatoPesquisa.getCoArtefato());
						}

						artefato.adicionarRelacionamentoDesativadoTransient(entry);

					}
				}
			}

			for (AtributoPersistence atributo : artefato.getTransientListaAtributos()) {
				atributo.setCoExterno(artefato.getCoArtefato());
				atributoDao.incluir(atributo);
			}
		} else if (atualizarArtefato && !desativarArtefato && Configuracao.CARGA_INICIAL == false) {

			try {
				artefatoDao.atualizar(artefatoPesquisa);
			} catch (EJBException e) {
				LOGGER.log(Level.SEVERE, "Erro a tentar atualizar artefato", e);
			}

			for (AtributoPersistence atributo : artefato.getTransientListaAtributos()) {
				atributo.setCoExterno(artefatoPesquisa.getCoArtefato());
				atributoDao.incluir(atributo);
			}
		}

		if (artefato.getTransientListaRelacionamentos() != null
				&& artefato.getTransientListaRelacionamentos().size() > 0) {
			for (RelacionamentoPersistence relacionamento : artefato.getTransientListaRelacionamentos()) {
				if (relacionamento.getArtefatoPai() != null) {
					atualizarTabelaArtefato(em, relacionamento.getArtefato(), marcaDesativarPai);
				}
			}
		}

		return artefato;
	}

	private static ArtefatoPersistence atribuirDadosArtefato(ArtefatoPersistence artefato,
			ArtefatoPersistence artefatoPesquisa) throws Exception {

		/*
		 * Verifica se o Tipo de Artefato do Artefato Entrada é igual a DESCONHECIDO e
		 * se o TIpo de Artefato do result é diferente de DESCONHECIDO e NULL
		 */

		if (TipoArtefato.DESCONHECIDO.get().equals(artefato.getCoTipoArtefato())
				&& !TipoArtefato.DESCONHECIDO.get().equals(artefatoPesquisa.getCoTipoArtefato())
				&& artefatoPesquisa.getCoTipoArtefato() != null) {
			artefato.setCoTipoArtefato(artefatoPesquisa.getCoTipoArtefato());
		}

		/*
		 * Verifica se o Sistema do Artefato Entrada é igual a DESCONHECIDO e se o
		 * Sistema do result é diferente de DESCONHECIDO e NULL
		 */

		if ("DESCONHECIDO".equals(artefato.getCoSistema()) && !"DESCONHECIDO".equals(artefatoPesquisa.getCoSistema())
				&& artefatoPesquisa.getCoSistema() != null) {
			artefato.setCoSistema(artefatoPesquisa.getCoSistema());
		}

		/*
		 * Verifica se o Ambiente do Artefato Entrada é igual a DESCONHECIDO e se o
		 * Ambiente do result é diferente de DESCONHECIDO e NULL
		 */

		if ("DESCONHECIDO".equals(artefato.getCoAmbiente()) && !"DESCONHECIDO".equals(artefatoPesquisa.getCoAmbiente())
				&& artefatoPesquisa.getCoAmbiente() != null) {
			artefato.setCoAmbiente(artefatoPesquisa.getCoAmbiente());
		}

		artefato.setIcProcessoCritico(artefatoPesquisa.isIcProcessoCritico());

		return artefato;
	}

	private static ArtefatoPersistence atribuirCoArtefato(EntityManager em, ArtefatoPersistence artefatoEntrada)
			throws Exception {
		for (RelacionamentoPersistence relacionamento : artefatoEntrada.getTransientListaRelacionamentos()) {
			ArtefatoPersistence artefato = relacionamento.getArtefato();
			ArtefatoPersistence artefatoPai = relacionamento.getArtefatoPai();
			ArtefatoPersistence artefatoAnterior = relacionamento.getArtefatoAnterior();
			ArtefatoPersistence artefatoPosterior = relacionamento.getArtefatoPosterior();
			ArtefatoPersistence artefatoPrimeiro = relacionamento.getArtefatoPrimeiro();
			ArtefatoPersistence artefatoUltimo = relacionamento.getArtefatoUltimo();

			if (artefatoAnterior != null) {
				relacionamento.setCoArtefatoAnterior(artefatoAnterior.getCoArtefato());
			}
			if (artefatoPosterior != null) {
				relacionamento.setCoArtefatoPosterior(artefatoPosterior.getCoArtefato());
			}
			if (artefatoPrimeiro != null) {
				relacionamento.setCoArtefatoPrimeiro(artefatoPrimeiro.getCoArtefato());
			}
			if (artefatoUltimo != null) {
				relacionamento.setCoArtefatoUltimo(artefatoUltimo.getCoArtefato());
			}
			if (artefato != null) {
				relacionamento.setCoArtefato(artefato.getCoArtefato());
			}

			if (artefatoPai != null) {
				relacionamento.setCoArtefatoPai(artefatoPai.getCoArtefato());
				artefato = atribuirCoArtefato(em, artefato);
			}
		}

		return artefatoEntrada;
	}

}
