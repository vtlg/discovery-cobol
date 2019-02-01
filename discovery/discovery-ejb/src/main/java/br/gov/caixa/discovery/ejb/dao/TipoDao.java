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

import br.gov.caixa.discovery.ejb.modelos.TipoPersistence;

@Stateless
public class TipoDao {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@PersistenceContext(name = "discovery-postgresql-jta", unitName = "discovery-postgresql-jta")
	private EntityManager em;

	public TipoDao() {
	}

	public TipoDao(EntityManager em) {
		this.em = em;
	}

	public TipoPersistence getTipo(String coTipo) {
		LOGGER.log(Level.FINE, "Pesquisar Tipo " + "CoTipo (" + coTipo + ")");

		TipoPersistence output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<TipoPersistence> cq = cb.createQuery(TipoPersistence.class);
		Root<TipoPersistence> tipoRoot = cq.from(TipoPersistence.class);

		Predicate pCoTipoArtefato = cb.equal(tipoRoot.get("coTipo"), coTipo);

		cq.multiselect(tipoRoot.get("coTipo"), tipoRoot.get("coTabela"), tipoRoot.get("deTipo"),
				tipoRoot.get("icPesquisavel"), tipoRoot.get("icExibirAtributo"), tipoRoot.get("icExibirGrafo"),
				tipoRoot.get("coCor"), tipoRoot.get("coCorBorda"), tipoRoot.get("nuLarguraBorda"))
				.where(pCoTipoArtefato);
		try {
			TypedQuery<TipoPersistence> query = em.createQuery(cq);
			output = query.getSingleResult();

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar pesquisar tipo. " + "CoTipo (" + coTipo + ")", e);
		}

		return output;
	}

	public List<TipoPersistence> listar(String coTabela) {
		LOGGER.log(Level.FINE, "Listar tipo");

		List<TipoPersistence> listaOutput = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<TipoPersistence> cq = cb.createQuery(TipoPersistence.class);
		Root<TipoPersistence> tipoRoot = cq.from(TipoPersistence.class);
	
		
		cq.multiselect(tipoRoot.get("coTipo"), tipoRoot.get("coTabela"), tipoRoot.get("deTipo"),
				tipoRoot.get("icPesquisavel"), tipoRoot.get("icExibirAtributo"), tipoRoot.get("icExibirGrafo"),
				tipoRoot.get("coCor"), tipoRoot.get("coCorBorda"), tipoRoot.get("nuLarguraBorda"));

		if (coTabela != null) {
			Predicate pCoTabela = cb.equal(tipoRoot.get("coTabela"), coTabela);
			cq.where(pCoTabela);
		}

		try {
			TypedQuery<TipoPersistence> query = em.createQuery(cq);
			listaOutput = query.getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar listar tipo tipo)", e);
		}
		return listaOutput;
	}
	


}
