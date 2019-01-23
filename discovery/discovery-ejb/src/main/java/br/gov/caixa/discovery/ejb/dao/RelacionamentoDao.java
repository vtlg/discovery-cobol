package br.gov.caixa.discovery.ejb.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;

@Stateless
public class RelacionamentoDao {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@PersistenceContext(name = "discovery-postgresql-jta", unitName = "discovery-postgresql-jta")
	private EntityManager em;

	public RelacionamentoDao() {
	}

	public RelacionamentoDao(EntityManager em) {
		this.em = em;
	}

	public RelacionamentoPersistence incluir(RelacionamentoPersistence relacionamento) {
		try {
			em.persist(relacionamento);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar incluir relacionamento.", e);
		}

		return relacionamento;
	}

}
