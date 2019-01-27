package br.gov.caixa.discovery.ejb.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.view.ArtefatoView;
import br.gov.caixa.discovery.ejb.view.RelacionamentoView;

@Stateless
public class ArtefatoDao {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@PersistenceContext(name = "discovery-postgresql-jta", unitName = "discovery-postgresql-jta")
	private EntityManager em;

	public ArtefatoDao() {
	}

	public ArtefatoDao(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Dao dao = new Dao();
		dao.abrirConexao();
		EntityManager em = dao.getEmFactory().createEntityManager();
		ArtefatoDao artefatoDao = new ArtefatoDao(em);

		ArtefatoPersistence artefato = artefatoDao.pesquisarArtefatoRelacionamento(926L);

		em.close();
		dao.fecharConexao();

		// System.out.println(artefato.getNoNomeArtefato());
		// System.out.println(artefato.getListaArtefato().get(0).getListaAtributos().toArray());
		// System.out.println(artefato.getListaArtefato().get(1).getListaAtributos().toArray());
		// System.out.println(artefato.getListaArtefato().get(2).getListaAtributos().toArray());

//		if (artefato.getListaArtefato() != null) {
//			artefato.getListaArtefato().forEach((rel) -> {
//				System.out.println(rel.getCoRelacionamento());
//				System.out.println(rel.getArtefato().getNoNomeArtefato());
//			});
//		}

	}

	public List<ArtefatoView> pesquisaAvancada(String expNome, String expDescricao, String[] listaTipoArtefato,
			Boolean icProcessoCritico, Boolean icInterface) {

		LOGGER.log(Level.FINE,
				"Pesquisa Avançada." + " expNome (" + expNome + ")" + " expDescricao (" + expDescricao + ")"
						+ " listaTipoArtefato (" + listaTipoArtefato + ")" + " icProcessoCritico (" + icProcessoCritico
						+ ")" + " icInterface (" + icInterface + ")");

		List<ArtefatoView> output = null;
		List<Predicate> listaPredicadosAnd = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoView> cq = cb.createQuery(ArtefatoView.class);
		Root<ArtefatoView> artefatoRoot = cq.from(ArtefatoView.class);

		if (expNome != null && !"".equals(expNome.trim())) {
			Predicate pNoNomeExibicao = cb.like(artefatoRoot.get("noNomeExibicao"), "%" + expNome + "%");
			Predicate pNoNomeInterno = cb.like(artefatoRoot.get("noNomeInterno"), "%" + expNome + "%");

			listaPredicadosAnd.add(cb.or(pNoNomeExibicao, pNoNomeInterno));
		}
		if (expDescricao != null && !"".equals(expDescricao.trim())) {
			Predicate pDeDescricaoArtefato = cb.like(artefatoRoot.get("deDescricaoArtefato"), "%" + expDescricao + "%");
			Predicate pDeDescricaoUsuario = cb.like(artefatoRoot.get("deDescricaoUsuario"), "%" + expDescricao + "%");
			listaPredicadosAnd.add(cb.or(pDeDescricaoArtefato, pDeDescricaoUsuario));
		}
		
		if (icInterface != null && icInterface == true) {
			Predicate pCoTipoRelacionamento = cb.equal(artefatoRoot.get("coTipoRelacionamento"), "INTERFACE");
			listaPredicadosAnd.add(pCoTipoRelacionamento);
		}

		if (listaTipoArtefato != null && listaTipoArtefato.length > 0) {
			Predicate pCoTipArtefato = artefatoRoot.get("coTipoArtefato").in(Arrays.asList(listaTipoArtefato));
			listaPredicadosAnd.add(pCoTipArtefato);
		}

		if (icInterface != null && icInterface == true) {
			Predicate pCoTipoRelacionamento = cb.equal(artefatoRoot.get("coTipoRelacionamento"), "INTERFACE");
			listaPredicadosAnd.add(pCoTipoRelacionamento);
		}

		if (listaPredicadosAnd.size() > 0) {
			cq.select(artefatoRoot).where(listaPredicadosAnd.toArray(new Predicate[] {}));
		} else {
			cq.select(artefatoRoot);
		}

		try {
			TypedQuery<ArtefatoView> query = em.createQuery(cq);
			output = query.getResultList();

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao tentar pesquisar artefato viw. " + " expNome (" + expNome + ")" + " expDescricao ("
							+ expDescricao + ")" + " listaTipoArtefato (" + listaTipoArtefato + ")"
							+ " icProcessoCritico (" + icProcessoCritico + ")" + " icInterface (" + icInterface + ")",
					e);
		}

		return output;

	}

	public ArtefatoPersistence pesquisarArtefato(Long coArtefato) {
		LOGGER.log(Level.FINE, "Pesquisar artefato " + "CoArtefato (" + coArtefato + ")");

		ArtefatoPersistence output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoPersistence> cq = cb.createQuery(ArtefatoPersistence.class);
		Root<ArtefatoPersistence> artefatoRoot = cq.from(ArtefatoPersistence.class);

		Predicate pCoArtefato = cb.equal(artefatoRoot.get("coArtefato"), coArtefato);
		// cq.select(artefatoRoot).where(pCoArtefato);
		cq.multiselect(artefatoRoot.get("coArtefato"), artefatoRoot.get("noNomeArtefato"),
				artefatoRoot.get("noNomeExibicao"), artefatoRoot.get("noNomeInterno"), artefatoRoot.get("coAmbiente"),
				artefatoRoot.get("coSistema"), artefatoRoot.get("coTipoArtefato"), artefatoRoot.get("deIdentificador"),
				artefatoRoot.get("deHash"), artefatoRoot.get("deDescricaoUsuario"),
				artefatoRoot.get("deDescricaoArtefato"), artefatoRoot.get("icInclusaoManual"),
				artefatoRoot.get("tsInicioVigencia"), artefatoRoot.get("tsUltimaModificacao"),
				artefatoRoot.get("tsFimVigencia"), artefatoRoot.get("icProcessoCritico")).where(pCoArtefato);
		try {
			TypedQuery<ArtefatoPersistence> query = em.createQuery(cq);
			output = query.getSingleResult();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar pesquisar artefato. " + "CoArtefato (" + coArtefato + ")", e);
		}

		return output;
	}

	public ArtefatoPersistence pesquisarArtefatoRelacionamento(Long coArtefato) {
		LOGGER.log(Level.FINE, "Pesquisar artefato-relacionamento " + "CoArtefato (" + coArtefato + ")");

		ArtefatoPersistence output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<RelacionamentoView> cq = cb.createQuery(RelacionamentoView.class);
		Root<RelacionamentoView> relacionamentoRoot = cq.from(RelacionamentoView.class);

		Predicate pCoArtefatoDesc = cb.equal(relacionamentoRoot.get("coArtefatoDesc"), coArtefato);
		Predicate pCoArtefatoAsc = cb.equal(relacionamentoRoot.get("coArtefatoAsc"), coArtefato);

		cq.select(relacionamentoRoot).where(cb.or(pCoArtefatoDesc, pCoArtefatoAsc));

		try {
			TypedQuery<RelacionamentoView> query = em.createQuery(cq);
			List<RelacionamentoView> lista = query.getResultList();

			for (RelacionamentoView entry : lista) {
				RelacionamentoPersistence relacionamento = entry.getTransientRelacionamento();

				if (output == null) {
					if (coArtefato.equals(entry.getCoArtefatoAsc())) {
						output = entry.getTransientArtefatoAsc();
					} else if (coArtefato.equals(entry.getCoArtefatoDesc())) {
						output = entry.getTransientArtefatoDesc();
					}
				}

				if (coArtefato.equals(entry.getCoArtefatoAsc())) {
					output.adicionarRelacionamento(relacionamento);
				} else if (coArtefato.equals(entry.getCoArtefatoDesc())) {
					output.adicionarRelacionamentoPai(relacionamento);

				}

			}

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar pesquisar artefato. " + "CoArtefato (" + coArtefato + ")", e);
		}

		return output;

	}

	public List<ArtefatoPersistence> pesquisarRapida(String coNome) {
		LOGGER.log(Level.FINE, "Pesquisar artefato " + "Nome (" + coNome + ")");

		List<ArtefatoPersistence> listaOutput = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoPersistence> cq = cb.createQuery(ArtefatoPersistence.class);
		Root<ArtefatoPersistence> artefatoRoot = cq.from(ArtefatoPersistence.class);

		Predicate pNomeInterno = cb.like(cb.upper(artefatoRoot.get("noNomeInterno")), "%" + coNome.toUpperCase() + "%");
		Predicate pNomeExibicao = cb.like(cb.upper(artefatoRoot.get("noNomeExibicao")),
				"%" + coNome.toUpperCase() + "%");

		Predicate orNome = cb.or(pNomeInterno, pNomeExibicao);

		Expression<Calendar> exTsFimVigencia = artefatoRoot.get("tsFimVigencia");
		Predicate pTsFimVigencia = cb.isNull(exTsFimVigencia);

		cq.multiselect(artefatoRoot.get("coArtefato"), artefatoRoot.get("noNomeArtefato"),
				artefatoRoot.get("noNomeExibicao"), artefatoRoot.get("noNomeInterno"),
				artefatoRoot.get("coTipoArtefato"), artefatoRoot.get("coAmbiente"), artefatoRoot.get("coSistema"))
				.where(cb.and(orNome, pTsFimVigencia));

		try {
			TypedQuery<ArtefatoPersistence> query = em.createQuery(cq);
			listaOutput = query.getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar pesquisar artefato. " + "Nome (" + coNome + ")", e);
		}

		return listaOutput;
	}

	public List<ArtefatoPersistence> pesquisarArtefato(String coNome, String coTipoArtefato, String coAmbiente,
			String coSistema, boolean somenteVigente) {
		LOGGER.log(Level.FINE,
				"Pesquisar artefato " + "Nome (" + coNome + ")" + "Tipo Artefato (" + coTipoArtefato + ")"
						+ "Ambiente (" + coAmbiente + ")" + "Sistema (" + coSistema + ")" + "Somente Vigente ("
						+ somenteVigente + ")");

		List<ArtefatoPersistence> listaOutput = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoPersistence> cq = cb.createQuery(ArtefatoPersistence.class);
		Root<ArtefatoPersistence> artefatoRoot = cq.from(ArtefatoPersistence.class);

		// *
		List<Predicate> listaAnd = new ArrayList<>();

		Predicate pNomeArtefato = cb.equal(artefatoRoot.get("noNomeArtefato"), coNome);
		listaAnd.add(pNomeArtefato);

		if (coTipoArtefato != null) {
			Predicate pTipoArtefato = cb.equal(artefatoRoot.get("coTipoArtefato"), coTipoArtefato);
			Predicate pTipoArtefatoDesconhecido = cb.equal(artefatoRoot.get("coTipoArtefato"), "DESCONHECIDO");
			Predicate orTipoArtefato = cb.or(pTipoArtefato, pTipoArtefatoDesconhecido);
			listaAnd.add(orTipoArtefato);
		}

		if (coAmbiente != null) {
			Predicate pAmbiente = cb.equal(artefatoRoot.get("coAmbiente"), coAmbiente);
			Predicate pAmbienteDesconhecido = cb.equal(artefatoRoot.get("coAmbiente"), "DESCONHECIDO");
			Predicate orAmbiente = cb.or(pAmbiente, pAmbienteDesconhecido);
			listaAnd.add(orAmbiente);
		}

		if (coSistema != null) {
			Predicate pSistema = cb.equal(artefatoRoot.get("coSistema"), coSistema);
			Predicate pSistemaDesconhecido = cb.equal(artefatoRoot.get("coSistema"), "DESCONHECIDO");
			Predicate orSistema = cb.or(pSistema, pSistemaDesconhecido);
			listaAnd.add(orSistema);
		}

		if (somenteVigente) {
			Expression<Calendar> exTsFimVigencia = artefatoRoot.get("tsFimVigencia");
			Predicate pTsFimVigencia = cb.isNull(exTsFimVigencia);
			listaAnd.add(pTsFimVigencia);
		}

		cq.multiselect(artefatoRoot.get("coArtefato"), artefatoRoot.get("noNomeArtefato"),
				artefatoRoot.get("noNomeExibicao"), artefatoRoot.get("noNomeInterno"), artefatoRoot.get("coAmbiente"),
				artefatoRoot.get("coSistema"), artefatoRoot.get("coTipoArtefato"), artefatoRoot.get("deIdentificador"),
				artefatoRoot.get("deHash"), artefatoRoot.get("deDescricaoUsuario"),
				artefatoRoot.get("deDescricaoArtefato"), artefatoRoot.get("icInclusaoManual"),
				artefatoRoot.get("tsInicioVigencia"), artefatoRoot.get("tsUltimaModificacao"),
				artefatoRoot.get("tsFimVigencia"), artefatoRoot.get("icProcessoCritico"))
				.where(listaAnd.toArray(new Predicate[] {}));

		try {
			TypedQuery<ArtefatoPersistence> query = em.createQuery(cq);
			listaOutput = query.getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao tentar pesquisar artefato. " + "Nome (" + coNome + ")" + "Tipo Artefato (" + coTipoArtefato
							+ ")" + "Ambiente (" + coAmbiente + ")" + "Sistema (" + coSistema + ")"
							+ "Somente Vigente (" + somenteVigente + ")",
					e);
		}
		return listaOutput;
	}

	public ArtefatoPersistence incluir(ArtefatoPersistence artefato) {

		try {
			em.persist(artefato);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao tentar incluir artefato. " + "Nome (" + artefato.getNoNomeArtefato() + ")"
							+ "Tipo Artefato (" + artefato.getCoTipoArtefato() + ")" + "Ambiente ("
							+ artefato.getCoAmbiente() + ")" + "Sistema (" + artefato.getCoSistema() + ")",
					e);
		}

		return artefato;
	}

	public ArtefatoPersistence atualizar(ArtefatoPersistence artefato) {

		try {
			em.merge(artefato);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao tentar atualizar artefato. " + "Nome (" + artefato.getNoNomeArtefato() + ")"
							+ "Tipo Artefato (" + artefato.getCoTipoArtefato() + ")" + "Ambiente ("
							+ artefato.getCoAmbiente() + ")" + "Sistema (" + artefato.getCoSistema() + ")",
					e);
		}

		return artefato;
	}

	public ArtefatoPersistence desativar(ArtefatoPersistence artefato) {

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaUpdate<ArtefatoPersistence> cq = cb.createCriteriaUpdate(ArtefatoPersistence.class);
		Root<ArtefatoPersistence> artefatoRoot = cq.from(ArtefatoPersistence.class);

		Predicate pNoNomeArtefato = cb.like(artefatoRoot.get("noNomeArtefato"), artefato.getNoNomeArtefato() + "/%");
		Expression<Calendar> exTsFimVigencia = artefatoRoot.get("tsFimVigencia");
		Predicate pTsFimVigencia = cb.isNull(exTsFimVigencia);

		cq.set(artefatoRoot.get("tsFimVigencia"), artefato.getTsFimVigencia());
		cq.where(pNoNomeArtefato, pTsFimVigencia);

		try {
			em.merge(artefato);
			em.createQuery(cq).executeUpdate();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao tentar desativar artefato. " + "Nome (" + artefato.getNoNomeArtefato() + ")"
							+ "Tipo Artefato (" + artefato.getCoTipoArtefato() + ")" + "Ambiente ("
							+ artefato.getCoAmbiente() + ")" + "Sistema (" + artefato.getCoSistema() + ")",
					e);
		}

		return artefato;
	}

}
