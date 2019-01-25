package br.gov.caixa.discovery.ws.resources;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.discovery.ejb.dao.TipoDao;
import br.gov.caixa.discovery.ejb.modelos.TipoPersistence;
import br.gov.caixa.discovery.ws.modelos.TipoDomain;
import br.gov.caixa.discovery.ws.utils.Conversores;

@Singleton
public class TipoResource implements TipoResourceI {

	@EJB
	TipoDao tipoDao;

	@Override
	public Response getLista(String coTabela) {
		ResponseBuilder response = Response.status(Status.OK);
		List<TipoPersistence> persistence = tipoDao.listar(null);

		List<TipoDomain> domain = Conversores.converterListaTipo(persistence);

		response.entity(domain);

		return response.build();
	}

	@Override
	public Response getTipo(String coTipo) {
		ResponseBuilder response = Response.status(Status.OK);
		TipoPersistence persistence = tipoDao.getTipo(coTipo);

		TipoDomain domain = Conversores.converter(persistence);

		response.entity(domain);

		return response.build();
	}

}
