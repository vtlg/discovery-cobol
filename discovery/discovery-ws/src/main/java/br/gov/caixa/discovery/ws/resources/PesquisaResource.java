package br.gov.caixa.discovery.ws.resources;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.discovery.ejb.dao.ArtefatoDao;
import br.gov.caixa.discovery.ejb.dao.ViewDao;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.tipos.MensagemSistema;
import br.gov.caixa.discovery.ws.modelos.ArtefatoDomain;
import br.gov.caixa.discovery.ws.modelos.PesquisaDomain;
import br.gov.caixa.discovery.ws.utils.Conversores;

@Stateless
public class PesquisaResource implements PesquisaResourceI {

	@EJB
	ArtefatoDao artefatoDao;

	@EJB
	ViewDao viewDao;

	@Override
	public Response pesquisaRapida(String termo, int limit) {
		ResponseBuilder response = Response.status(Status.OK);

		List<ArtefatoPersistence> listaPersistence = artefatoDao.pesquisar(termo, limit);

		if (listaPersistence == null || listaPersistence.size() == 0) {
			throw new EJBException(MensagemSistema.MA009_NENHUM_RESULTADO.get());
		} else {
			response.entity(Conversores.converterListaArtefato(listaPersistence));
		}

		return response.build();
	}

	@Override
	public Response pesquisaAvancada(int offset, int limit, PesquisaDomain pesquisaDomain) throws Exception {
		ResponseBuilder response = Response.status(Status.OK);
		List<ArtefatoPersistence> listaPersistence = artefatoDao.pesquisar(pesquisaDomain.getExpNome(),
				pesquisaDomain.getExpDescricao(), pesquisaDomain.getListaTipoArtefato(),
				pesquisaDomain.getListaSistema(), pesquisaDomain.getIcProcessoCritico(),
				pesquisaDomain.getIcInterface(), offset, limit);

		listaPersistence = viewDao.getArtefatoCounts(listaPersistence);

		if (listaPersistence != null && listaPersistence.size() > 0) {
			List<ArtefatoDomain> listaDomain = Conversores.converterListaArtefato(listaPersistence);
			response.entity(listaDomain);
		} else {
			throw new EJBException(MensagemSistema.MA009_NENHUM_RESULTADO.get());
		}

		return response.build();
	}
}