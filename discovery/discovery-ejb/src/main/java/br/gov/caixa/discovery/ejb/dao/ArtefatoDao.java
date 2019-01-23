package br.gov.caixa.discovery.ejb.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;

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

//	public static void main(String[] args) {
//		Dao dao = new Dao();
//		dao.abrirConexao();
//		EntityManager em = dao.getEmFactory().createEntityManager();
//		ArtefatoDao artefatoDao = new ArtefatoDao(em);
//
//		ArtefatoPersistence artefato = artefatoDao.pesquisarArtefato(45695L);
//
//		em.close();
//		dao.fecharConexao();
//
//		// System.out.println(artefato.getNoNomeArtefato());
//		// System.out.println(artefato.getListaArtefato().get(1).getArtefato().getNoNomeArtefato());
//	}

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
				artefatoRoot.get("tsFimVigencia")).where(pCoArtefato);
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

		EntityGraph<ArtefatoPersistence> graph = this.em.createEntityGraph(ArtefatoPersistence.class);
		graph.addAttributeNodes("tipoArtefato");
		Subgraph<RelacionamentoPersistence> relacionamentoGraph = graph.addSubgraph("listaArtefato");
		relacionamentoGraph.addAttributeNodes("artefato");
		relacionamentoGraph.addAttributeNodes("artefatoPai");
		relacionamentoGraph.addAttributeNodes("artefatoPrimeiro");
		relacionamentoGraph.addAttributeNodes("artefatoUltimo");
		relacionamentoGraph.addAttributeNodes("artefatoAnterior");
		relacionamentoGraph.addAttributeNodes("artefatoPosterior");

		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("javax.persistence.loadgraph", graph);

		try {
			output = this.em.find(ArtefatoPersistence.class, coArtefato, hints);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar pesquisar artefato. " + "CoArtefato (" + coArtefato + ")", e);
		}

		return output;
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

		cq.select(artefatoRoot).where(listaAnd.toArray(new Predicate[] {}));

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
