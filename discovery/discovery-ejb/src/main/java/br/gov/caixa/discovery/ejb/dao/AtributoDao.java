package br.gov.caixa.discovery.ejb.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.caixa.discovery.ejb.modelos.AtributoPersistence;

@Stateless
public class AtributoDao {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@PersistenceContext(name = "discovery-postgresql-jta", unitName = "discovery-postgresql-jta")
	private EntityManager em;

	public AtributoDao() {
	}

	public AtributoDao(EntityManager em) {
		this.em = em;
	}

	public AtributoPersistence incluir(AtributoPersistence atributo) throws EJBException {
		try {
			em.persist(atributo);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar incluir atributo.", e);
		}

		return atributo;
	}

}
