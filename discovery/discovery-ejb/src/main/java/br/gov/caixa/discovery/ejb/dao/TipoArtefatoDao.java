package br.gov.caixa.discovery.ejb.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.caixa.discovery.ejb.modelos.TipoArtefatoPersistence;

@Stateless
public class TipoArtefatoDao {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@PersistenceContext(name = "discovery-postgresql-jta", unitName = "discovery-postgresql-jta")
	private EntityManager em;

	public TipoArtefatoDao() {
	}

	public TipoArtefatoDao(EntityManager em) {
		this.em = em;
	}

	public TipoArtefatoPersistence pesquisaTipoArtefato(String coTipoArtefato) {
		LOGGER.log(Level.FINE, "Pesquisar Tipo Artefato " + "CoTipoArtefato (" + coTipoArtefato + ")");

		TipoArtefatoPersistence output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<TipoArtefatoPersistence> cq = cb.createQuery(TipoArtefatoPersistence.class);
		Root<TipoArtefatoPersistence> tipoArtefatoRoot = cq.from(TipoArtefatoPersistence.class);

		Predicate pCoTipoArtefato = cb.equal(tipoArtefatoRoot.get("coTipoArtefato"), coTipoArtefato);
		// cq.select(artefatoRoot).where(pCoArtefato);
		cq.multiselect(tipoArtefatoRoot.get("coTipoArtefato"), tipoArtefatoRoot.get("deTipoArtefato"),
				tipoArtefatoRoot.get("icPesquisavel"), tipoArtefatoRoot.get("icAtributo"),
				tipoArtefatoRoot.get("icGrafo"), tipoArtefatoRoot.get("coCor"), tipoArtefatoRoot.get("coCorBorda"))
				.where(pCoTipoArtefato);
		try {
			TypedQuery<TipoArtefatoPersistence> query = em.createQuery(cq);
			output = query.getSingleResult();

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar pesquisar artefato. " + "CoTipoArtefato (" + coTipoArtefato + ")",
					e);
		}

		return output;
	}

	public List<TipoArtefatoPersistence> listar() {
		LOGGER.log(Level.FINE, "Listar tipo artefato");

		List<TipoArtefatoPersistence> listaOutput = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<TipoArtefatoPersistence> cq = cb.createQuery(TipoArtefatoPersistence.class);
		Root<TipoArtefatoPersistence> tipoArtefatoRoot = cq.from(TipoArtefatoPersistence.class);

		cq.multiselect(tipoArtefatoRoot.get("coTipoArtefato"), tipoArtefatoRoot.get("deTipoArtefato"),
				tipoArtefatoRoot.get("icPesquisavel"), tipoArtefatoRoot.get("icAtributo"),
				tipoArtefatoRoot.get("icGrafo"), tipoArtefatoRoot.get("coCor"));

		try {
			TypedQuery<TipoArtefatoPersistence> query = em.createQuery(cq);
			listaOutput = query.getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar listar tipo artefato)", e);
		}
		return listaOutput;
	}

}
