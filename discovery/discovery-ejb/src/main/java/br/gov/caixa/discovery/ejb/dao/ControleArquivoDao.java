package br.gov.caixa.discovery.ejb.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.caixa.discovery.ejb.modelos.ControleArquivoPersistence;

@Stateless
public class ControleArquivoDao {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@PersistenceContext(name = "discovery-postgresql-jta", unitName = "discovery-postgresql-jta")
	private EntityManager em;

	public ControleArquivoDao() {
	}

	public ControleArquivoDao(EntityManager em) {
		this.em = em;
	}

	public ControleArquivoPersistence incluir(ControleArquivoPersistence controleArquivo) throws EJBException {
		try {
			em.persist(controleArquivo);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar incluir controle arquivo.", e);
		}

		return controleArquivo;
	}

	public ControleArquivoPersistence getControleArquivo(String noNomeArquivo) throws EJBException {
		ControleArquivoPersistence output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ControleArquivoPersistence> cq = cb.createQuery(ControleArquivoPersistence.class);
		Root<ControleArquivoPersistence> controleArquivoRoot = cq.from(ControleArquivoPersistence.class);

		Predicate pNomeArquivo = cb.equal(controleArquivoRoot.get("noNomeArquivo"), noNomeArquivo);

		cq.select(controleArquivoRoot).where(cb.and(pNomeArquivo));

		try {
			output = em.createQuery(cq).getSingleResult();
		} catch (NoResultException e) {
			output = null;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao retornar controle arquivo. " + "NoNomeArquivo (" + noNomeArquivo + ")",
					e);
		}

		return output;
	}
	
	public ControleArquivoPersistence atualizar(ControleArquivoPersistence controleArquivo) throws EJBException {
		try {
			em.merge(controleArquivo);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar incluir controle arquivo.", e);
		}

		return controleArquivo;
	}

}
