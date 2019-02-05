package br.gov.caixa.discovery.ejb.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.caixa.discovery.ejb.modelos.SistemaPersistence;
import br.gov.caixa.discovery.ejb.tipos.MensagemSistema;

@Stateless
public class SistemaDao {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@PersistenceContext(name = "discovery-postgresql-jta", unitName = "discovery-postgresql-jta")
	private EntityManager em;

	public SistemaDao() {
	}

	public SistemaDao(EntityManager em) {
		this.em = em;
	}

	public SistemaPersistence getSistema(String coSistema) throws EJBException {
		LOGGER.log(Level.FINE, "Pesquisar Sistema " + "CoSistema (" + coSistema + ")");

		SistemaPersistence output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<SistemaPersistence> cq = cb.createQuery(SistemaPersistence.class);
		Root<SistemaPersistence> sistemaRoot = cq.from(SistemaPersistence.class);

		Predicate pCoSistemaArtefato = cb.equal(sistemaRoot.get("coSistema"), coSistema);

		cq.multiselect(sistemaRoot.get("coSistema"), sistemaRoot.get("deSistema")).where(pCoSistemaArtefato);
		try {
			TypedQuery<SistemaPersistence> query = em.createQuery(cq);
			output = query.getSingleResult();
		} catch (NoResultException e) {
			throw new EJBException(MensagemSistema.MA009_NENHUM_RESULTADO.get());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar pesquisar sistema. " + "CoSistema (" + coSistema + ")", e);
		}

		return output;
	}

	public List<SistemaPersistence> listar() {
		LOGGER.log(Level.FINE, "Listar sistemas");

		List<SistemaPersistence> listaOutput = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<SistemaPersistence> cq = cb.createQuery(SistemaPersistence.class);
		Root<SistemaPersistence> sistemaRoot = cq.from(SistemaPersistence.class);

		cq.multiselect(sistemaRoot.get("coSistema"), sistemaRoot.get("coSistema"));

		try {
			TypedQuery<SistemaPersistence> query = em.createQuery(cq);
			listaOutput = query.getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar listar sistemas)", e);
		}
		return listaOutput;
	}

}
