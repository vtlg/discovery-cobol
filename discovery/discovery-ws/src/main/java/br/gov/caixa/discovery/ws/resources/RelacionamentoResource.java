package br.gov.caixa.discovery.ws.resources;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.discovery.ejb.dao.RelacionamentoDao;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;
import br.gov.caixa.discovery.ws.modelos.RelacionamentoDomain;

@Stateless
public class RelacionamentoResource implements RelacionamentoResourceI {

	@EJB
	RelacionamentoDao relacionamentoDao;

	@Override
	public Response atualizar(String coRelacionamento, RelacionamentoDomain domain) {
		ResponseBuilder response = Response.status(Status.OK);

		RelacionamentoPersistence persistence = new RelacionamentoPersistence();
		persistence.setCoRelacionamento(domain.getCoRelacionamento());
		persistence.setCoTipoRelacionamento(domain.getTipoRelacionamento().getCoTipo());

		relacionamentoDao.atualizar(persistence);

		response.entity(domain);
		return response.build();
	}

}
