package br.gov.caixa.discovery.ws.resources;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.discovery.ejb.dao.ArtefatoViewDao;
import br.gov.caixa.discovery.ejb.view.ArtefatoView;
import br.gov.caixa.discovery.ws.modelos.ArtefatoViewDomain;
import br.gov.caixa.discovery.ws.modelos.PesquisaDomain;
import br.gov.caixa.discovery.ws.utils.Conversores;

@Stateless
public class PesquisaResource implements PesquisaResourceI {

	@EJB
	ArtefatoViewDao artefatoViewDao;

	@Override
	public Response pesquisaRapida(String termo) {
		ResponseBuilder response = Response.status(Status.OK);

		List<ArtefatoView> listaPersistence = artefatoViewDao.getArtefato(termo);

		if (listaPersistence == null || listaPersistence.size() == 0) {
			response.status(Status.NOT_FOUND);
		} else {
			response.entity(Conversores.converterListaArtefatoView(listaPersistence));
		}

		return response.build();
	}

	@Override
	public Response pesquisaAvancada(int offset, int limit, PesquisaDomain pesquisaDomain) throws Exception {
		ResponseBuilder response = Response.status(Status.OK);
		Collection<ArtefatoView> listaView = artefatoViewDao.getArtefato(pesquisaDomain.getExpNome(),
				pesquisaDomain.getExpDescricao(), pesquisaDomain.getListaTipoArtefato(),
				pesquisaDomain.getListaSistema(), pesquisaDomain.getIcProcessoCritico(),
				pesquisaDomain.getIcInterface(), offset, limit);
		
		if (listaView != null) {
			List<ArtefatoViewDomain> listaDomain = Conversores.converterListaArtefatoView(listaView);
			response.entity(listaDomain);
		} else {
			response.status(Status.NOT_FOUND);
		}

		return response.build();
	}
}