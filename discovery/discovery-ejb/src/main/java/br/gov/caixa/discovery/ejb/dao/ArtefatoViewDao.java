package br.gov.caixa.discovery.ejb.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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

import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.view.ArtefatoView;
import br.gov.caixa.discovery.ejb.view.RelacionamentoView;

@Stateless
public class ArtefatoViewDao {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@PersistenceContext(name = "discovery-postgresql-jta", unitName = "discovery-postgresql-jta")
	private EntityManager em;

	public static void main(String[] args) throws EJBException {
		Dao dao = new Dao();
		dao.abrirConexao();
		EntityManager em = dao.getEmFactory().createEntityManager();
		ArtefatoViewDao artefatoDao = new ArtefatoViewDao(em);

		ArtefatoPersistence artefato = artefatoDao.getArtefatoRelacionamento(379L);

		em.close();
		dao.fecharConexao();

		System.out.println(artefato.getNoNomeArtefato());
		// System.out.println(artefato.getListaArtefato().get(0).getListaAtributos().toArray());
		// System.out.println(artefato.getListaArtefato().get(1).getListaAtributos().toArray());
		// System.out.println(artefato.getListaArtefato().get(2).getListaAtributos().toArray());

		if (artefato.getListaArtefato() != null) {
			artefato.getListaArtefato().forEach((rel) -> {
				System.out.println(rel.getCoRelacionamento());
				System.out.println(rel.getArtefato().getNoNomeArtefato());
			});
		}

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

	public ArtefatoViewDao() {
	}

	public ArtefatoViewDao(EntityManager em) {
		this.em = em;
	}

	public List<ArtefatoView> getArtefato(String coNome) {
		LOGGER.log(Level.FINE, "Pesquisar artefato " + "Nome (" + coNome + ")");

		List<ArtefatoView> listaOutput = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoView> cq = cb.createQuery(ArtefatoView.class);
		Root<ArtefatoView> artefatoRoot = cq.from(ArtefatoView.class);

		Predicate pNomeInterno = cb.like(cb.upper(artefatoRoot.get("noNomeInterno")), "%" + coNome.toUpperCase() + "%");
		Predicate pNomeExibicao = cb.like(cb.upper(artefatoRoot.get("noNomeExibicao")), "%" + coNome.toUpperCase() + "%");
		Predicate orNome = cb.or(pNomeInterno, pNomeExibicao);

//		Expression<Calendar> exTsFimVigencia = artefatoRoot.get("tsFimVigencia");
//		Predicate pTsFimVigencia = cb.isNull(exTsFimVigencia);

		cq.multiselect(artefatoRoot.get("coArtefato"), artefatoRoot.get("noNomeArtefato"),
				artefatoRoot.get("noNomeExibicao"), artefatoRoot.get("noNomeInterno"),
				artefatoRoot.get("coTipoArtefato"), artefatoRoot.get("coAmbiente"), artefatoRoot.get("coSistema"))
				// .where(cb.and(orNome, pTsFimVigencia));
				.where(cb.and(orNome));

		try {
			TypedQuery<ArtefatoView> query = em.createQuery(cq);
			listaOutput = query.getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar pesquisar artefato. " + "Nome (" + coNome + ")", e);
		}

		return listaOutput;
	}

	public Collection<ArtefatoView> getArtefato(String expNome, String expDescricao, String[] listaTipoArtefato,
			String[] listaSistema, Boolean icProcessoCritico, Boolean icInterface, int offset, int limit)
			throws Exception {

		LOGGER.log(Level.FINE,
				"Pesquisa Avançada." + " expNome (" + expNome + ")" + " expDescricao (" + expDescricao + ")"
						+ " listaTipoArtefato (" + listaTipoArtefato + ")" + " listaSistema (" + listaSistema + ")"
						+ " icProcessoCritico (" + icProcessoCritico + ")" + " icInterface (" + icInterface + ")");

		Collection<ArtefatoView> output = null;
		List<Predicate> listaPredicadosAnd = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoView> cq = cb.createQuery(ArtefatoView.class);
		Root<ArtefatoView> artefatoRoot = cq.from(ArtefatoView.class);

		if (expNome != null && !"".equals(expNome.trim())) {
			Predicate pNoNomeExibicao = cb.like(cb.upper(artefatoRoot.get("noNomeExibicao")),
					"%" + expNome.toUpperCase() + "%");
			Predicate pNoNomeInterno = cb.like(cb.upper(artefatoRoot.get("noNomeInterno")),
					"%" + expNome.toUpperCase() + "%");

			listaPredicadosAnd.add(cb.or(pNoNomeExibicao, pNoNomeInterno));
		}
		if (expDescricao != null && !"".equals(expDescricao.trim())) {
			Predicate pDeDescricaoArtefato = cb.like(cb.upper(artefatoRoot.get("deDescricaoArtefato")),
					"%" + expDescricao.toUpperCase() + "%");
			Predicate pDeDescricaoUsuario = cb.like(cb.upper(artefatoRoot.get("deDescricaoUsuario")),
					"%" + expDescricao.toUpperCase() + "%");
			listaPredicadosAnd.add(cb.or(pDeDescricaoArtefato, pDeDescricaoUsuario));
		}

		if (icProcessoCritico != null && icProcessoCritico == true) {
			Predicate pIcProcessoCritico = cb.equal(artefatoRoot.get("icProcessoCritico"), true);
			listaPredicadosAnd.add(pIcProcessoCritico);
		}

		if (icInterface != null && icInterface == true) {
			Predicate pCoTipoRelacionamento = cb.greaterThan(artefatoRoot.get("countRelacionamentoInterface"), 0);
			listaPredicadosAnd.add(pCoTipoRelacionamento);
		}

		if (listaTipoArtefato != null && listaTipoArtefato.length > 0) {
			Predicate pCoTipoArtefato = artefatoRoot.get("coTipoArtefato").in(Arrays.asList(listaTipoArtefato));
			listaPredicadosAnd.add(pCoTipoArtefato);
		} else {
			return null;
		}

		if (listaSistema != null && listaSistema.length > 0) {
			Predicate pCoSistema = artefatoRoot.get("coSistema").in(Arrays.asList(listaSistema));
			listaPredicadosAnd.add(pCoSistema);
		} else {
			return null;
		}

		if (listaPredicadosAnd.size() > 0) {
			cq.select(artefatoRoot).where(listaPredicadosAnd.toArray(new Predicate[] {}));
		} else {
			cq.select(artefatoRoot);
		}

		try {
			cq.orderBy(cb.asc(artefatoRoot.get("coArtefato")));
			TypedQuery<ArtefatoView> query = em.createQuery(cq).setFirstResult(offset).setMaxResults(limit);
			output = query.getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao tentar pesquisar artefato view. " + " expNome (" + expNome + ")" + " expDescricao ("
							+ expDescricao + ")" + " listaTipoArtefato (" + listaTipoArtefato + ")"
							+ " icProcessoCritico (" + icProcessoCritico + ")" + " icInterface (" + icInterface + ")",
					e);
			throw new Exception("ME0001");
		}

		return output;

	}

}
