package br.gov.caixa.discovery.ws.resources;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.discovery.ejb.dao.SistemaDao;
import br.gov.caixa.discovery.ejb.modelos.SistemaPersistence;
import br.gov.caixa.discovery.ws.modelos.SistemaDomain;
import br.gov.caixa.discovery.ws.utils.Conversores;

@Singleton
public class SistemaResource implements SistemaResourceI {

	@EJB
	SistemaDao sistemaDao;

	@Override
	public Response getLista() {
		ResponseBuilder response = Response.status(Status.OK);
		List<SistemaPersistence> persistence = sistemaDao.listar();

		List<SistemaDomain> domain = Conversores.converterListaSistema(persistence);

		response.entity(domain);

		return response.build();
	}

	@Override
	public Response getSistema(String coSistema) {
		ResponseBuilder response = Response.status(Status.OK);
		SistemaPersistence persistence = sistemaDao.getSistema(coSistema);

		SistemaDomain domain = Conversores.converter(persistence);

		response.entity(domain);

		return response.build();
	}

}
