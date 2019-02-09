package br.gov.caixa.discovery.ejb.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoArtefatoPersistence;

@Stateless
public class ArtefatoDao {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@PersistenceContext(name = "discovery-postgresql-jta", unitName = "discovery-postgresql-jta")
	private EntityManager em;

	public ArtefatoDao() {
	}

	public ArtefatoDao(EntityManager em) {
		this.em = em;
	}

	public ArtefatoPersistence getArtefato(Long coArtefato) throws EJBException {
		LOGGER.log(Level.FINE, "Pesquisar artefato " + "CoArtefato (" + coArtefato + ")");

		ArtefatoPersistence output = null;

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoPersistence> cq = cb.createQuery(ArtefatoPersistence.class);
		Root<ArtefatoPersistence> artefatoRoot = cq.from(ArtefatoPersistence.class);

		Predicate pCoArtefato = cb.equal(artefatoRoot.get("coArtefato"), coArtefato);
		// cq.select(artefatoRoot).where(pCoArtefato);
		cq.multiselect(artefatoRoot.get("coArtefato"), artefatoRoot.get("noNomeArtefato"),
				artefatoRoot.get("noNomeExibicao"), artefatoRoot.get("noNomeInterno"), artefatoRoot.get("coAmbiente"),
				artefatoRoot.get("coSistema"), artefatoRoot.get("coTipoArtefato"), artefatoRoot.get("deIdentificador"),
				artefatoRoot.get("deHash"), artefatoRoot.get("deDescricaoUsuario"),
				artefatoRoot.get("deDescricaoArtefato"), artefatoRoot.get("icInclusaoManual"),
				artefatoRoot.get("tsInicioVigencia"), artefatoRoot.get("tsUltimaModificacao"),
				artefatoRoot.get("tsFimVigencia"), artefatoRoot.get("icProcessoCritico")).where(pCoArtefato);
		try {
			TypedQuery<ArtefatoPersistence> query = em.createQuery(cq);
			output = query.getSingleResult();
		} catch (NoResultException e) {
			LOGGER.log(Level.SEVERE, "Nenhum resultado para pesquisa.", e);
			throw new EJBException("ME0002");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar pesquisar artefato. " + "CoArtefato (" + coArtefato + ")", e);
			throw new EJBException("ME0001");
		}

		return output;
	}

	public List<ArtefatoPersistence> getListaArtefato(String coNome, String coTipoArtefato, String coAmbiente,
			String coSistema, boolean somenteVigente) throws EJBException {
		LOGGER.log(Level.FINE,
				"Pesquisar artefato " + "Nome (" + coNome + ")" + "Tipo Artefato (" + coTipoArtefato + ")"
						+ "Ambiente (" + coAmbiente + ")" + "Sistema (" + coSistema + ")" + "Somente Vigente ("
						+ somenteVigente + ")");

		List<ArtefatoPersistence> listaOutput = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoPersistence> cq = cb.createQuery(ArtefatoPersistence.class);
		Root<ArtefatoPersistence> artefatoRoot = cq.from(ArtefatoPersistence.class);

		// *
		List<Predicate> listaAnd = new ArrayList<>();

		Predicate pNomeArtefato = cb.equal(artefatoRoot.get("noNomeArtefato"), coNome);
		listaAnd.add(pNomeArtefato);

		if (coTipoArtefato != null) {
			Predicate pTipoArtefato = cb.equal(artefatoRoot.get("coTipoArtefato"), coTipoArtefato);
			listaAnd.add(pTipoArtefato);
		}

		if (coAmbiente != null) {
			Predicate pAmbiente = cb.equal(artefatoRoot.get("coAmbiente"), coAmbiente);
			Predicate pAmbienteDesconhecido = cb.equal(artefatoRoot.get("coAmbiente"), "DESCONHECIDO");
			Predicate orAmbiente = cb.or(pAmbiente, pAmbienteDesconhecido);
			listaAnd.add(orAmbiente);
		}

		if (coSistema != null) {
			Predicate pSistema = cb.equal(artefatoRoot.get("coSistema"), coSistema);
			Predicate pSistemaDesconhecido = cb.equal(artefatoRoot.get("coSistema"), "DESCONHECIDO");
			Predicate orSistema = cb.or(pSistema, pSistemaDesconhecido);
			listaAnd.add(orSistema);
		}

		if (somenteVigente) {
			Expression<Calendar> exTsFimVigencia = artefatoRoot.get("tsFimVigencia");
			Predicate pTsFimVigencia = cb.isNull(exTsFimVigencia);
			listaAnd.add(pTsFimVigencia);
		}

		cq.multiselect(artefatoRoot.get("coArtefato"), artefatoRoot.get("noNomeArtefato"),
				artefatoRoot.get("noNomeExibicao"), artefatoRoot.get("noNomeInterno"), artefatoRoot.get("coAmbiente"),
				artefatoRoot.get("coSistema"), artefatoRoot.get("coTipoArtefato"), artefatoRoot.get("deIdentificador"),
				artefatoRoot.get("deHash"), artefatoRoot.get("deDescricaoUsuario"),
				artefatoRoot.get("deDescricaoArtefato"), artefatoRoot.get("icInclusaoManual"),
				artefatoRoot.get("tsInicioVigencia"), artefatoRoot.get("tsUltimaModificacao"),
				artefatoRoot.get("tsFimVigencia"), artefatoRoot.get("icProcessoCritico"))
				.where(listaAnd.toArray(new Predicate[] {}));

		try {
			TypedQuery<ArtefatoPersistence> query = em.createQuery(cq);
			listaOutput = query.getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao tentar pesquisar artefato. " + "Nome (" + coNome + ")" + "Tipo Artefato (" + coTipoArtefato
							+ ")" + "Ambiente (" + coAmbiente + ")" + "Sistema (" + coSistema + ")"
							+ "Somente Vigente (" + somenteVigente + ")",
					e);
		}
		return listaOutput;
	}

	public ArtefatoPersistence incluir(ArtefatoPersistence artefato) throws EJBException {

		try {
			em.persist(artefato);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao tentar incluir artefato. " + "Nome (" + artefato.getNoNomeArtefato() + ")"
							+ "Tipo Artefato (" + artefato.getCoTipoArtefato() + ")" + "Ambiente ("
							+ artefato.getCoAmbiente() + ")" + "Sistema (" + artefato.getCoSistema() + ")",
					e);
		}

		return artefato;
	}

	public ArtefatoPersistence atualizar(ArtefatoPersistence artefato) throws EJBException {
		try {
			em.merge(artefato);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao tentar atualizar artefato. " + "Nome (" + artefato.getNoNomeArtefato() + ")"
							+ "Tipo Artefato (" + artefato.getCoTipoArtefato() + ")" + "Ambiente ("
							+ artefato.getCoAmbiente() + ")" + "Sistema (" + artefato.getCoSistema() + ")",
					e);
		}
		return artefato;
	}

	public ArtefatoPersistence desativar(ArtefatoPersistence artefato) throws EJBException {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaUpdate<ArtefatoPersistence> cq = cb.createCriteriaUpdate(ArtefatoPersistence.class);
		Root<ArtefatoPersistence> artefatoRoot = cq.from(ArtefatoPersistence.class);

		Predicate pNoNomeArtefato = cb.like(artefatoRoot.get("noNomeArtefato"), artefato.getNoNomeArtefato() + "/%");
		Expression<Calendar> exTsFimVigencia = artefatoRoot.get("tsFimVigencia");
		Predicate pTsFimVigencia = cb.isNull(exTsFimVigencia);

		cq.set(artefatoRoot.get("tsFimVigencia"), artefato.getTsFimVigencia());
		cq.where(pNoNomeArtefato, pTsFimVigencia);

		try {
			em.merge(artefato);
			em.createQuery(cq).executeUpdate();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao tentar desativar artefato. " + "Nome (" + artefato.getNoNomeArtefato() + ")"
							+ "Tipo Artefato (" + artefato.getCoTipoArtefato() + ")" + "Ambiente ("
							+ artefato.getCoAmbiente() + ")" + "Sistema (" + artefato.getCoSistema() + ")",
					e);
		}

		return artefato;
	}

	public List<ArtefatoPersistence> getArtefatoAtributos(Long coArtefato) {
		List<ArtefatoPersistence> output = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoPersistence> cq = cb.createQuery(ArtefatoPersistence.class);
		Root<ArtefatoPersistence> artefatoRoot = cq.from(ArtefatoPersistence.class);
		Join<ArtefatoPersistence, AtributoArtefatoPersistence> atributoJoin = artefatoRoot.join("listaAtributos",
				JoinType.LEFT);

		Predicate joinOnCoArtefato = cb.equal(artefatoRoot.get("coArtefato"), atributoJoin.get("coExterno"));
		atributoJoin.on(joinOnCoArtefato);

		Predicate pCoArtefato = cb.equal(artefatoRoot.get("coArtefato"), coArtefato);

		cq.multiselect(artefatoRoot, atributoJoin).where(pCoArtefato);

		// List<Object> listaResult = this.em.createQuery(cq).getResultList();

		return output;
	}

	public List<ArtefatoPersistence> pesquisar(String noNome) {
		LOGGER.log(Level.FINE, "Pesquisar artefato " + "Nome (" + noNome + ")");

		List<ArtefatoPersistence> listaOutput = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoPersistence> cq = cb.createQuery(ArtefatoPersistence.class);
		Root<ArtefatoPersistence> artefatoRoot = cq.from(ArtefatoPersistence.class);

		Predicate pNomeInterno = cb.like(cb.upper(artefatoRoot.get("noNomeInterno")), "%" + noNome.toUpperCase() + "%");
		Predicate pNomeExibicao = cb.like(cb.upper(artefatoRoot.get("noNomeExibicao")),
				"%" + noNome.toUpperCase() + "%");
		Predicate orNome = cb.or(pNomeInterno, pNomeExibicao);

		Expression<Calendar> exTsFimVigencia = artefatoRoot.get("tsFimVigencia");
		Predicate pTsFimVigencia = cb.isNull(exTsFimVigencia);

		cq.multiselect(artefatoRoot.get("coArtefato"), artefatoRoot.get("noNomeArtefato"),
				artefatoRoot.get("noNomeExibicao"), artefatoRoot.get("noNomeInterno"),
				artefatoRoot.get("coTipoArtefato"), artefatoRoot.get("coAmbiente"), artefatoRoot.get("coSistema"))
				.where(cb.and(orNome, pTsFimVigencia));

		try {
			TypedQuery<ArtefatoPersistence> query = em.createQuery(cq);
			listaOutput = query.getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar pesquisar artefato. " + "Nome (" + noNome + ")", e);
		}

		return listaOutput;
	}

	public List<ArtefatoPersistence> pesquisar(String expNome, String expDescricao, String[] listaTipoArtefato,
			String[] listaSistema, Boolean icProcessoCritico, Boolean icInterface, int offset, int limit)
			throws EJBException {
		List<ArtefatoPersistence> listaOutput = new ArrayList<>();
		List<Predicate> listaPredicadosAnd = new ArrayList<>();

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<ArtefatoPersistence> cq = cb.createQuery(ArtefatoPersistence.class);
		Root<ArtefatoPersistence> artefatoRoot = cq.from(ArtefatoPersistence.class);

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

		Expression<Calendar> exTsFimVigencia = artefatoRoot.get("tsFimVigencia");
		Predicate pTsFimVigencia = cb.isNull(exTsFimVigencia);
		listaPredicadosAnd.add(pTsFimVigencia);

		cq.multiselect(
				artefatoRoot.get("coArtefato"),
				artefatoRoot.get("noNomeArtefato"),
				artefatoRoot.get("noNomeExibicao"),
				artefatoRoot.get("noNomeInterno"),
				artefatoRoot.get("coAmbiente"),
				artefatoRoot.get("coSistema"),
				artefatoRoot.get("coTipoArtefato"),
				artefatoRoot.get("deIdentificador"),
				artefatoRoot.get("deHash"),
				artefatoRoot.get("deDescricaoUsuario"),
				artefatoRoot.get("deDescricaoArtefato"),
				artefatoRoot.get("icInclusaoManual"),
				artefatoRoot.get("icProcessoCritico"),
				artefatoRoot.get("tsInicioVigencia"),
				artefatoRoot.get("tsUltimaModificacao"));
		
		if (listaPredicadosAnd.size() > 0) {
			cq.where(listaPredicadosAnd.toArray(new Predicate[] {}));
		}
		
		
		
		

		try {
			cq.orderBy(cb.asc(artefatoRoot.get("coArtefato")));
			TypedQuery<ArtefatoPersistence> query = em.createQuery(cq).setFirstResult(offset).setMaxResults(limit);
			listaOutput = query.getResultList();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Erro ao tentar pesquisar artefato view. " + " expNome (" + expNome + ")" + " expDescricao ("
							+ expDescricao + ")" + " listaTipoArtefato (" + listaTipoArtefato + ")"
							+ " icProcessoCritico (" + icProcessoCritico + ")" + " icInterface (" + icInterface + ")",
					e);
			throw new EJBException("ME0001");
		}

		return listaOutput;
	}

	public static void main(String[] args) throws EJBException {
		Dao dao = new Dao();
		dao.abrirConexao();
		EntityManager em = dao.getEmFactory().createEntityManager();
//		ArtefatoDao artefatoDao = new ArtefatoDao(em);

		
//		String[] arrSistema = {"SIPCS","SIFDL","DESCONHECIDO"};
//		String[] arrTipoArtefato = {"JCL","JCL-STEP"};
		
		
//		List<ArtefatoPersistence> artefato = artefatoDao.pesquisar(null, null, arrTipoArtefato, arrSistema, null, null, 0, 200);

		em.close();
		dao.fecharConexao();

		// System.out.println(artefato.getNoNomeArtefato());
		// System.out.println(artefato.getListaArtefato().get(0).getListaAtributos().toArray());
		// System.out.println(artefato.getListaArtefato().get(1).getListaAtributos().toArray());
		// System.out.println(artefato.getListaArtefato().get(2).getListaAtributos().toArray());

//		if (artefato.getListaArtefato() != null) {
//			artefato.getListaArtefato().forEach((rel) -> {
//				System.out.println(rel.getCoRelacionamento());
//				System.out.println(rel.getArtefato().getNoNomeArtefato());
//			});
//		}

	}

}
