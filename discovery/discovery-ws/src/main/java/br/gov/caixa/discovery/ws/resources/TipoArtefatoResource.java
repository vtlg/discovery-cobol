package br.gov.caixa.discovery.ws.resources;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.discovery.ejb.dao.TipoArtefatoDao;
import br.gov.caixa.discovery.ejb.modelos.TipoArtefatoPersistence;
import br.gov.caixa.discovery.ws.modelos.TipoArtefatoDomain;
import br.gov.caixa.discovery.ws.utils.Conversores;

public class TipoArtefatoResource implements TipoArtefatoResourceI {

	@EJB
	TipoArtefatoDao tipoArtefatoDao;

	@Override
	public Response getLista() {
		ResponseBuilder response = Response.status(Status.OK);
		List<TipoArtefatoPersistence> persistence = tipoArtefatoDao.listar();

		List<TipoArtefatoDomain> domain = Conversores.converterListaTipoArtefato(persistence);

		response.entity(domain);

		return response.build();
	}

	@Override
	public Response getTipoArtefato(String coTipoArtefato) {
		ResponseBuilder response = Response.status(Status.OK);
		TipoArtefatoPersistence persistence = tipoArtefatoDao.pesquisaTipoArtefato(coTipoArtefato);

		TipoArtefatoDomain domain = Conversores.converter(persistence);

		response.entity(domain);

		return response.build();
	}

}
