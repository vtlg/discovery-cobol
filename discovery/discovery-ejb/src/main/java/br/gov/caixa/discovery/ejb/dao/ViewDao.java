package br.gov.caixa.discovery.ejb.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.view.ArtefatoCountsView;
import br.gov.caixa.discovery.ejb.view.InterfaceSistemaView;
import br.gov.caixa.discovery.ejb.view.RelacionamentoView;

@Stateless

public class ViewDao {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@PersistenceContext(name = "discovery-postgresql-jta", unitName = "discovery-postgresql-jta")
	private EntityManager em;

	public ViewDao() {
	}

	public ViewDao(EntityManager em) {
		this.em = em;
	}

	public ArtefatoPersistence getArtefatoRelacionamento(Long coArtefato) throws EJBException {
		LOGGER.log(Level.FINE, "Pesquisar artefato-relacionamento " + "CoArtefato (" + coArtefato + ")");

		ArtefatoPersistence output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<RelacionamentoView> cq = cb.createQuery(RelacionamentoView.class);
		Root<RelacionamentoView> relacionamentoRoot = cq.from(RelacionamentoView.class);

		Predicate pCoArtefatoDesc = cb.equal(relacionamentoRoot.get("coArtefatoDesc"), coArtefato);
		Predicate pCoArtefatoAsc = cb.equal(relacionamentoRoot.get("coArtefatoAsc"), coArtefato);
		cq.select(relacionamentoRoot).where(cb.or(pCoArtefatoDesc, pCoArtefatoAsc)).distinct(true);

		try {
			TypedQuery<RelacionamentoView> query = em.createQuery(cq);
			Collection<RelacionamentoView> lista = query.getResultList();

			HashMap<Long, RelacionamentoView> map = new HashMap<>();
			for (RelacionamentoView entry : lista) {

				if (map.containsKey(entry.getCoRelacionamentoRel())) {
					if (entry.getCoNuSequencialAtr() != null) {
						map.get(entry.getCoRelacionamentoRel()).adicionarAtributo(entry.getTransientAtributo());
					}
				} else {
					map.put(entry.getCoRelacionamentoRel(), entry);
					if (entry.getCoNuSequencialAtr() != null) {
						map.get(entry.getCoRelacionamentoRel()).adicionarAtributo(entry.getTransientAtributo());
					}
				}

			}
			lista = map.values();
			for (RelacionamentoView entry : lista) {
				RelacionamentoPersistence relacionamento = entry.getTransientRelacionamento();

				relacionamento.setListaAtributos(entry.getTransientListaAtributos());
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
		} catch (NoResultException e) {
			throw new EJBException("MA0002");
		} catch (Exception e) {
			throw new EJBException("MA0001");
		}
		return output;
	}

	public List<ArtefatoPersistence> getArtefatoCounts(List<ArtefatoPersistence> listaArtefato) throws EJBException {
		if (listaArtefato == null || listaArtefato.size() == 0) {
			return listaArtefato;
		}

		List<Long> listaCoArtefato = listaArtefato.stream().map((o) -> {
			return o.getCoArtefato();
		}).collect(Collectors.toList());

		List<ArtefatoCountsView> listaCount = getArtefatoCounts(listaCoArtefato, 0);

		for (ArtefatoPersistence artefato : listaArtefato) {
			for (ArtefatoCountsView count : listaCount) {
				if (artefato.getCoArtefato().equals(count.getCoArtefato())) {

					artefato.setTransientCountRelacionamentos(count.getCountRelacionamento());
					artefato.setTransientCountRelacionamentosControlM(count.getCountRelacionamentoControlM());
					artefato.setTransientCountRelacionamentosInterface(count.getCountRelacionamentoInterface());
					artefato.setTransientCountRelacionamentosNormal(count.getCountRelacionamentoNormal());
					break;
				}
			}
		}

		return listaArtefato;
	}

	public List<ArtefatoCountsView> getArtefatoCounts(List<Long> listaCoArtefato, int diferenciar) throws EJBException {
		LOGGER.log(Level.FINE, "Entrando em getArtefatoCounts()");

		List<ArtefatoCountsView> output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoCountsView> cq = cb.createQuery(ArtefatoCountsView.class);
		Root<ArtefatoCountsView> artefatoCountsViewRoot = cq.from(ArtefatoCountsView.class);

		Predicate pInArtefato = artefatoCountsViewRoot.get("coArtefato").in(listaCoArtefato); // .equal(artefatoCountsViewRoot.get("coArtefato"),
																								// listaCoArtefato);

		cq.select(artefatoCountsViewRoot).where(pInArtefato).distinct(true);

		try {
			TypedQuery<ArtefatoCountsView> query = em.createQuery(cq);
			output = query.getResultList();
		} catch (Exception e) {
			throw new EJBException("ME001", e);
		}

		return output;
	}

	public List<InterfaceSistemaView> getInterfacesSistema(String coSistema) {
		List<InterfaceSistemaView> output = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<InterfaceSistemaView> cq = cb.createQuery(InterfaceSistemaView.class);
		Root<InterfaceSistemaView> interfaceRoot = cq.from(InterfaceSistemaView.class);

		Predicate pCoSistema = cb.equal(interfaceRoot.get("coSistema"), coSistema);
		Predicate pCoSistemaPai = cb.equal(interfaceRoot.get("coSistema"), coSistema);

		cq.where(cb.or(pCoSistema, pCoSistemaPai));

		try {
			TypedQuery<InterfaceSistemaView> query = this.em.createQuery(cq);
			output = query.getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar recuperar interfaces. " + "CoSistema (" + coSistema + ")", e);
		}

		return output;
	}

}
