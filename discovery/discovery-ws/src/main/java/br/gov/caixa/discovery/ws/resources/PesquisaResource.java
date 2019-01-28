package br.gov.caixa.discovery.ws.resources;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.discovery.ejb.dao.ArtefatoDao;
import br.gov.caixa.discovery.ejb.view.ArtefatoView;
import br.gov.caixa.discovery.ws.modelos.ArtefatoViewDomain;
import br.gov.caixa.discovery.ws.modelos.PesquisaDomain;
import br.gov.caixa.discovery.ws.utils.Conversores;

@Stateless
public class PesquisaResource implements PesquisaResourceI {

	@EJB
	ArtefatoDao artefatoDao;

	@Override
	public Response pesquisaAvancada(int offset, int limit, PesquisaDomain pesquisaDomain) {
		ResponseBuilder response = Response.status(Status.OK);
		Collection<ArtefatoView> listaView = artefatoDao.pesquisaAvancada(pesquisaDomain.getExpNome(),
				pesquisaDomain.getExpDescricao(), pesquisaDomain.getListaTipoArtefato(),
				pesquisaDomain.getIcProcessoCritico(), pesquisaDomain.getIcInterface(),
				offset, limit);

		List<ArtefatoViewDomain> listaDomain = Conversores.converterListaArtefatoView(listaView);

		response.entity(listaDomain);

		return response.build();
	}

	@Override
	public Response pesquisaRapida(String termo) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public Response getArtefato(Long coArtefato) {
//		ResponseBuilder response = Response.status(Status.OK);
//		ArtefatoPersistence persistence = artefatoDao.pesquisarArtefato(coArtefato);
//
//		ArtefatoDomain domain = Conversores.converter(persistence, true, false);
//
//		if (domain == null) {
//			response.status(Status.NOT_FOUND);
//		} else {
//			response.entity(domain);
//		}
//
//		return response.build();
//	}
//
//	@Override
//	public Response getRelacionamentos(Long coArtefato) {
//		ResponseBuilder response = Response.status(Status.OK);
//		ArtefatoPersistence persistence = artefatoDao.pesquisarArtefatoRelacionamento(coArtefato);
//
//		ArtefatoDomain domain = Conversores.converter(persistence, true, false);
//
//		response.entity(domain);
//
//		return response.build();
//	}
//
//	@Override
//	public Response atualizarArtefato(Long coArtefato, ArtefatoDomain domain) {
//		ResponseBuilder response = Response.status(Status.OK);
//		ArtefatoPersistence persistence = artefatoDao.pesquisarArtefato(coArtefato);
//
//		if (persistence == null) {
//			// ERRO : ARTEFATO NÃO ENCONTRADO
//		}
//
//		persistence.setCoSistema(domain.getCoSistema());
//		persistence.setCoAmbiente(domain.getCoAmbiente());
//		persistence.setDeDescricaoUsuario(domain.getDeDescricaoUsuario());
//		persistence.setNoNomeExibicao(domain.getNoNomeExibicao());
//		persistence.setTsUltimaModificacao(Calendar.getInstance());
//
//		artefatoDao.atualizar(persistence);
//
//		domain = Conversores.converter(persistence, false, false);
//
//		response.entity(domain);
//
//		return response.build();
//	}
//
//	@Override
//	public Response pesquisar(String termo) {
//		ResponseBuilder response = Response.status(Status.OK);
//
//		List<ArtefatoPersistence> listaPersistence = artefatoDao.pesquisarRapida(termo);
//
//		if (listaPersistence == null || listaPersistence.size() == 0) {
//			response.status(Status.NOT_FOUND);
//		} else {
//			response.entity(Conversores.converterListaArtefato(listaPersistence));
//		}
//
//		return response.build();
//	}

}
