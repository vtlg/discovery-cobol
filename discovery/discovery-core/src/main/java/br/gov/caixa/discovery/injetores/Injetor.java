package br.gov.caixa.discovery.injetores;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.utils.Configuracao;
import br.gov.caixa.discovery.ejb.dao.ArtefatoDao;
import br.gov.caixa.discovery.ejb.dao.AtributoDao;
import br.gov.caixa.discovery.ejb.dao.Dao;
import br.gov.caixa.discovery.ejb.dao.RelacionamentoDao;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;

public class Injetor {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private static EntityManager em = null;

	public static void main(String[] args) {
		LOGGER.log(Level.INFO, "Carregando parâmetros");
		Configuracao.carregar(args);
		List<ArtefatoPersistence> listaArtefatosPersistence = Conversor.executar();

		executar(listaArtefatosPersistence);

	}

	private static void executar(List<ArtefatoPersistence> listaEntrada) {
		if (listaEntrada != null) {
			Dao dao = new Dao();
			dao.abrirConexao();
			em = dao.getEmFactory().createEntityManager();
			// EntityTransaction transaction = em.getTransaction();

			for (ArtefatoPersistence artefato : listaEntrada) {
				em.getTransaction().begin();
				atualizarTabelaArtefato(artefato);
				atribuirCoArtefato(artefato);
				unificarListasRelacionamento(artefato);
				atualizarTabelaRelacionamento(artefato);
				em.getTransaction().commit();
				// atualizarRelacionamento(artefato);
			}
			dao.fecharConexao();
		}
	}

	private static ArtefatoPersistence unificarListasRelacionamento(ArtefatoPersistence artefato) {
		List<RelacionamentoPersistence> tempLista = new ArrayList<>();

		if (artefato.getTransientRelacionamentosDesativados() != null) {
			for (RelacionamentoPersistence entry : artefato.getTransientRelacionamentosDesativados()) {
				RelacionamentoPersistence newEntry = new RelacionamentoPersistence();

				newEntry.setCoArtefato(entry.getCoArtefato());
				newEntry.setCoArtefatoPai(entry.getCoArtefatoPai());
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
			RelacionamentoPersistence relacionamento) {
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

	private static ArtefatoPersistence atualizarTabelaRelacionamento(ArtefatoPersistence artefato) {

		RelacionamentoDao relacionamentoDao = new RelacionamentoDao(em);
		AtributoDao atributoDao = new AtributoDao(em);

		for (RelacionamentoPersistence relacionamento : artefato.getTransientListaRelacionamentos()) {

			if (artefato.isTransientAtualizarRelacionamentos()) {
				relacionamentoDao.incluir(relacionamento);
				for (AtributoPersistence atributo : relacionamento.getTransientListaAtributos()) {
					atributo.setCoExterno(relacionamento.getCoRelacionamento());
					atributoDao.incluir(atributo);
				}
			}

			if (relacionamento.getArtefatoPai() != null) {
				atualizarTabelaRelacionamento(relacionamento.getArtefato());
			}
		}

		return artefato;
	}

	private static ArtefatoPersistence atualizarTabelaArtefato(ArtefatoPersistence artefato) {
		return atualizarTabelaArtefato(artefato, false);
	}

	private static ArtefatoPersistence atualizarTabelaArtefato(ArtefatoPersistence artefato,
			boolean marcaDesativarPai) {

		boolean incluirArtefato = false;
		boolean atualizarArtefato = false;
		boolean desativarArtefato = false;

		ArtefatoDao artefatoDao = new ArtefatoDao(em);
		AtributoDao atributoDao = new AtributoDao(em);
		RelacionamentoDao relacionamentoDao = new RelacionamentoDao(em);
		String coNome = artefato.getNoNomeArtefato();

		List<ArtefatoPersistence> resultListaArtefatoPesquisa = null;

		ArtefatoPersistence artefatoPesquisa = null;
		if (!TipoArtefato.DESCONHECIDO.get().equals(artefato.getCoTipoArtefato())) {
			resultListaArtefatoPesquisa = artefatoDao.pesquisarArtefato(coNome, artefato.getCoTipoArtefato(), null,
					null, true);
			if (resultListaArtefatoPesquisa != null && resultListaArtefatoPesquisa.size() >= 2) {
				System.out.println("Verificar poi não deveria retornar mais de um artefato");
			} else if (resultListaArtefatoPesquisa != null && resultListaArtefatoPesquisa.size() > 0) {
				artefato = atribuirDadosArtefato(artefato, resultListaArtefatoPesquisa.get(0));
				artefatoPesquisa = resultListaArtefatoPesquisa.get(0);
			}
		}

		if (TipoArtefato.DESCONHECIDO.get().equals(artefato.getCoTipoArtefato()) || artefatoPesquisa == null) {
			//
			// caso o tipo do artefato seja DESCONHECIDO
			//
			resultListaArtefatoPesquisa = artefatoDao.pesquisarArtefato(coNome, null, null, null, true);

			boolean isAribuido = false;
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
					&& (artefatoPesquisa.getDeHash() == null || "".equals(artefatoPesquisa.getDeHash()))) {
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
			artefatoDao.incluir(artefato);

			for (AtributoPersistence atributo : artefato.getTransientListaAtributos()) {
				atributo.setCoExterno(artefato.getCoArtefato());
				atributoDao.incluir(atributo);
			}

		} else if (atualizarArtefato && desativarArtefato) {
			// DESATIVAR ARTEFATO E RELACIONAMENTOS

			List<RelacionamentoPersistence> tempLista = relacionamentoDao.desativar(artefatoPesquisa.getCoArtefato(),
					artefatoPesquisa.getTsFimVigencia());

			artefatoDao.desativar(artefatoPesquisa);
			artefatoDao.incluir(artefato);

			if (tempLista != null) {
				for (RelacionamentoPersistence entry : tempLista) {
					if ((entry.getCoArtefato() != null
							&& entry.getCoArtefato().equals(artefatoPesquisa.getCoArtefato()))
							|| entry.isIcInclusaoMalha() || entry.isIcInclusaoManual()) {

						if (entry.getCoArtefato().equals(artefatoPesquisa.getCoArtefato())) {
							entry.setCoArtefato(artefato.getCoArtefato());
						}
						if (entry.getCoArtefatoPai().equals(artefatoPesquisa.getCoArtefato())) {
							entry.setCoArtefatoPai(artefato.getCoArtefato());
						}

						artefato.adicionarRelacionamentoDesativadoTransient(entry);

					}
				}
			}

			for (AtributoPersistence atributo : artefato.getTransientListaAtributos()) {
				atributo.setCoExterno(artefato.getCoArtefato());
				atributoDao.incluir(atributo);
			}
		} else if (atualizarArtefato && !desativarArtefato) {
			artefatoDao.atualizar(artefatoPesquisa);

			for (AtributoPersistence atributo : artefato.getTransientListaAtributos()) {
				atributo.setCoExterno(artefatoPesquisa.getCoArtefato());
				atributoDao.incluir(atributo);
			}
		}

		if (artefato.getTransientListaRelacionamentos() != null
				&& artefato.getTransientListaRelacionamentos().size() > 0) {
			for (RelacionamentoPersistence relacionamento : artefato.getTransientListaRelacionamentos()) {
				if (relacionamento.getArtefatoPai() != null) {
					atualizarTabelaArtefato(relacionamento.getArtefato(), marcaDesativarPai);
				}
			}
		}

		return artefato;
	}

	private static ArtefatoPersistence atribuirDadosArtefato(ArtefatoPersistence artefato,
			ArtefatoPersistence artefatoPesquisa) {

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

	private static ArtefatoPersistence atribuirCoArtefato(ArtefatoPersistence artefatoEntrada) {

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
				artefato = atribuirCoArtefato(artefato);
			}
		}

		return artefatoEntrada;
	}

}
