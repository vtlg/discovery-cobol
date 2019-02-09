package br.gov.caixa.discovery.ejb.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.utils.UtilsHandler;
import br.gov.caixa.discovery.ejb.view.InterfaceSistemaView;

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

	public RelacionamentoPersistence incluir(RelacionamentoPersistence relacionamento) throws EJBException {
		try {
			em.persist(relacionamento);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar incluir relacionamento.", e);
		}

		return relacionamento;
	}

	public RelacionamentoPersistence getRelacionamento(Long coRelacionamento) throws EJBException {
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

	public List<RelacionamentoPersistence> getListaRelacionamento(Long coArtefato) throws EJBException {
		List<RelacionamentoPersistence> output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<RelacionamentoPersistence> cq = cb.createQuery(RelacionamentoPersistence.class);
		Root<RelacionamentoPersistence> relacionamentoRoot = cq.from(RelacionamentoPersistence.class);

		Predicate pCoArtefato = cb.equal(relacionamentoRoot.get("coArtefato"), coArtefato);
		Predicate pCoArtefatoPai = cb.equal(relacionamentoRoot.get("coArtefatoPai"), coArtefato);
		Predicate pCoArtefatoAnterior = cb.equal(relacionamentoRoot.get("coArtefatoAnterior"), coArtefato);
		Predicate pCoArtefatoPosterior = cb.equal(relacionamentoRoot.get("coArtefatoPosterior"), coArtefato);
		Predicate pCoArtefatoPrimeiro = cb.equal(relacionamentoRoot.get("coArtefatoPrimeiro"), coArtefato);
		Predicate pCoArtefatoUltimo = cb.equal(relacionamentoRoot.get("coArtefatoUltimo"), coArtefato);

		Expression<Calendar> exTsFimVigencia = relacionamentoRoot.get("tsFimVigencia");
		Predicate pTsFimVigencia = cb.isNull(exTsFimVigencia);

		cq.multiselect(relacionamentoRoot.get("coRelacionamento"), relacionamentoRoot.get("coArtefato"),
				relacionamentoRoot.get("coArtefatoPai"), relacionamentoRoot.get("coArtefatoAnterior"),
				relacionamentoRoot.get("coArtefatoPosterior"), relacionamentoRoot.get("coArtefatoPrimeiro"),
				relacionamentoRoot.get("coArtefatoUltimo"), relacionamentoRoot.get("coTipoRelacionamento"),
				relacionamentoRoot.get("icInclusaoManual"), relacionamentoRoot.get("icInclusaoMalha"),
				relacionamentoRoot.get("tsInicioVigencia"), relacionamentoRoot.get("tsFimVigencia"))
				.where(cb.and(cb.or(pCoArtefato, pCoArtefatoPai, pCoArtefatoAnterior, pCoArtefatoPosterior,
						pCoArtefatoPrimeiro, pCoArtefatoUltimo), pTsFimVigencia));

		try {
			output = em.createQuery(cq).getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao retornar relacionamento. " + "CoArtefato (" + coArtefato + ")", e);
		}

		return output;
	}

	@SuppressWarnings("unused")
	public List<RelacionamentoPersistence> desativar(Long coArtefato, Calendar tsFimVigencia) throws EJBException {

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
	public int desativarControlM(Long coArtefato, Calendar tsFimVigencia) throws EJBException {
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
	public RelacionamentoPersistence atualizar(RelacionamentoPersistence relacionamento) throws EJBException {

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

	public int atualizarRelacionamentoOnAlterarCoSistema(ArtefatoPersistence artefato) {
		int countAtualizacoes = 0;

		String strQuery = "" + " UPDATE " + "     TBL_RELACIONAMENTO_ARTEFATO  " + " SET "
				+ "     CO_TIPO_RELACIONAMENTO = 'INTERFACE'" + "   , IC_INCLUSAO_MANUAL = TRUE"
				+ " WHERE CO_RELACIONAMENTO IN " + " ( "
				+ "     SELECT CO_RELACIONAMENTO FROM TBL_RELACIONAMENTO_ARTEFATO T1   "
				+ "      INNER JOIN TBL_ARTEFATO T2 " + "         ON  T1.CO_ARTEFATO = T2.CO_ARTEFATO "
				+ "        AND T2.CO_SISTEMA <> :co_sistema " + "        AND T2.CO_SISTEMA <> 'DESCONHECIDO' "
				+ "      WHERE T1.CO_ARTEFATO_PAI = :co_artefato " + "  UNION ALL "
				+ "     SELECT CO_RELACIONAMENTO FROM TBL_RELACIONAMENTO_ARTEFATO T1  "
				+ "      INNER JOIN TBL_ARTEFATO T2 " + "         ON  T1.CO_ARTEFATO_PAI = T2.CO_ARTEFATO "
				+ "        AND T2.CO_SISTEMA <> :co_sistema " + "        AND T2.CO_SISTEMA <> 'DESCONHECIDO'"
				+ "      WHERE T1.CO_ARTEFATO = :co_artefato " + " )";

		Query query = this.em.createNativeQuery(strQuery);

		query.setParameter("co_sistema", artefato.getCoSistema());
		query.setParameter("co_artefato", artefato.getCoArtefato());

		query.executeUpdate();

		return countAtualizacoes;
	}

	public static void main(String[] args) {
		Dao dao = new Dao();
		dao.abrirConexao();
		EntityManager em = dao.getEmFactory().createEntityManager();

		RelacionamentoDao relacionamentoDao = new RelacionamentoDao(em);

		ArtefatoPersistence artefatoPersistence = new ArtefatoPersistence();
		artefatoPersistence.setCoArtefato(196601L);
		artefatoPersistence.setCoSistema("SIPCS");

		// em.getTransaction().begin();
		// relacionamentoDao.atualizarRelacionamentoOnAlterarCoSistema(artefatoPersistence);
		//relacionamentoDao.getInterfaces("SIPCS");
		// em.getTransaction().commit();
		em.close();
		dao.fecharConexao();
	}

}
