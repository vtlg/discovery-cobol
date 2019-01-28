package br.gov.caixa.discovery.ejb.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

	public RelacionamentoPersistence getRelacionamento(Long coRelacionamento) {
		RelacionamentoPersistence output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<RelacionamentoPersistence> cq = cb.createQuery(RelacionamentoPersistence.class);
		Root<RelacionamentoPersistence> relacionamentoRoot = cq.from(RelacionamentoPersistence.class);

		Predicate pCoRelacionamento = cb.equal(relacionamentoRoot.get("coRelacionamento"), coRelacionamento);

		cq.multiselect(relacionamentoRoot.get("coRelacionamento"), relacionamentoRoot.get("coArtefato"),
				relacionamentoRoot.get("coArtefatoPai"), relacionamentoRoot.get("coArtefatoAnterior"),
				relacionamentoRoot.get("coArtefatoPosterior"), relacionamentoRoot.get("coArtefatoPrimeiro"),
				relacionamentoRoot.get("coArtefatoUltimo"), relacionamentoRoot.get("coTipoRelacionamento"),
				relacionamentoRoot.get("icInclusaoManual"), relacionamentoRoot.get("icInclusaoMalha"),
				relacionamentoRoot.get("tsInicioVigencia"), relacionamentoRoot.get("tsFimVigencia"))
				.where(cb.and(pCoRelacionamento));

		try {
			output = em.createQuery(cq).getSingleResult();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao retornar relacionamento. " + "CoRelacionamento (" + coRelacionamento + ")", e);
		}

		return output;
	}

	public List<RelacionamentoPersistence> getListaRelacionamento(Long coArtefato) {
		List<RelacionamentoPersistence> output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<RelacionamentoPersistence> cq = cb.createQuery(RelacionamentoPersistence.class);
		Root<RelacionamentoPersistence> relacionamentoRoot = cq.from(RelacionamentoPersistence.class);

		Predicate pCoArtefato = cb.equal(relacionamentoRoot.get("coArtefato"), coArtefato);
		Predicate pCoArtefatoPai = cb.equal(relacionamentoRoot.get("coArtefatoPai"), coArtefato);

		Expression<Calendar> exTsFimVigencia = relacionamentoRoot.get("tsFimVigencia");
		Predicate pTsFimVigencia = cb.isNull(exTsFimVigencia);

		cq.multiselect(relacionamentoRoot.get("coRelacionamento"), relacionamentoRoot.get("coArtefato"),
				relacionamentoRoot.get("coArtefatoPai"), relacionamentoRoot.get("coArtefatoAnterior"),
				relacionamentoRoot.get("coArtefatoPosterior"), relacionamentoRoot.get("coArtefatoPrimeiro"),
				relacionamentoRoot.get("coArtefatoUltimo"), relacionamentoRoot.get("coTipoRelacionamento"),
				relacionamentoRoot.get("icInclusaoManual"), relacionamentoRoot.get("icInclusaoMalha"),
				relacionamentoRoot.get("tsInicioVigencia"), relacionamentoRoot.get("tsFimVigencia"))
				.where(cb.and(cb.or(pCoArtefato, pCoArtefatoPai), pTsFimVigencia));

		try {
			output = em.createQuery(cq).getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao retornar relacionamento. " + "CoArtefato (" + coArtefato + ")", e);
		}

		return output;
	}

	@SuppressWarnings("unused")
	public List<RelacionamentoPersistence> desativar(Long coArtefato, Calendar tsFimVigencia) {

		List<RelacionamentoPersistence> output = getListaRelacionamento(coArtefato);

		if (output != null) {
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<RelacionamentoPersistence> cq = cb.createCriteriaUpdate(RelacionamentoPersistence.class);
			Root<RelacionamentoPersistence> relacionamentoRoot = cq.from(RelacionamentoPersistence.class);

			Set<Long> listaCoRelacionamento = new HashSet<>();
			for (RelacionamentoPersistence persistence : output) {
				listaCoRelacionamento.add(persistence.getCoRelacionamento());
				persistence.setTsFimVigencia(tsFimVigencia);
			}

			Predicate pcoRelacionamento = relacionamentoRoot.get("coRelacionamento").in(listaCoRelacionamento);
			cq.set(relacionamentoRoot.get("tsFimVigencia"), tsFimVigencia);

			cq.where(pcoRelacionamento);

			try {
				int i = this.em.createQuery(cq).executeUpdate();
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE,
						"Erro ao tentar desativar relacionamento. " + "CoArtefato (" + coArtefato + ")", e);
			}
		}

		return output;
	}

	@SuppressWarnings("unused")
	public int desativarControlM(Long coArtefato, Calendar tsFimVigencia) {
		int output = 0;
		List<RelacionamentoPersistence> resultRelacionamento = getListaRelacionamento(coArtefato);
		List<RelacionamentoPersistence> listaDesativar = new ArrayList<>();

		if (resultRelacionamento != null && resultRelacionamento.size() > 0) {
			for (RelacionamentoPersistence entry : resultRelacionamento) {
				if (entry.isIcInclusaoMalha() && !entry.getCoArtefato().equals(coArtefato)) {
					listaDesativar.add(entry);
				}
			}

			if (listaDesativar != null && listaDesativar.size() > 0) {
				CriteriaBuilder cb = this.em.getCriteriaBuilder();
				CriteriaUpdate<RelacionamentoPersistence> cq = cb.createCriteriaUpdate(RelacionamentoPersistence.class);
				Root<RelacionamentoPersistence> relacionamentoRoot = cq.from(RelacionamentoPersistence.class);

				Set<Long> listaCoRelacionamento = new HashSet<>();
				for (RelacionamentoPersistence persistence : listaDesativar) {
					listaCoRelacionamento.add(persistence.getCoRelacionamento());
					persistence.setTsFimVigencia(tsFimVigencia);
				}

				Predicate pcoRelacionamento = relacionamentoRoot.get("coRelacionamento").in(listaCoRelacionamento);
				cq.set(relacionamentoRoot.get("tsFimVigencia"), tsFimVigencia);

				cq.where(pcoRelacionamento);

				try {
					int i = this.em.createQuery(cq).executeUpdate();
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE,
							"Erro ao tentar desativar relacionamento control-m. " + "CoArtefato (" + coArtefato + ")",
							e);
				}
			}

		}

		return output;
	}

	@SuppressWarnings("unused")
	public RelacionamentoPersistence atualizar(RelacionamentoPersistence relacionamento) {

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaUpdate<RelacionamentoPersistence> cq = cb.createCriteriaUpdate(RelacionamentoPersistence.class);
		Root<RelacionamentoPersistence> relacionamentoRoot = cq.from(RelacionamentoPersistence.class);

		Predicate pcoRelacionamento = cb.equal(relacionamentoRoot.get("coRelacionamento"),
				relacionamento.getCoRelacionamento());

		cq.set(relacionamentoRoot.get("coTipoRelacionamento"), relacionamento.getCoTipoRelacionamento());

		cq.where(pcoRelacionamento);

		try {
			int i = this.em.createQuery(cq).executeUpdate();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar atualizar relacionamento. " + "CoArtefato ("
					+ relacionamento.getCoRelacionamento() + ")", e);
		}

		return relacionamento;
	}

}
