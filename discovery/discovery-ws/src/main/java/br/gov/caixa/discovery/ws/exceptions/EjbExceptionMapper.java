package br.gov.caixa.discovery.ws.exceptions;

import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.gov.caixa.discovery.ejb.tipos.MensagemSistema;

@Provider
public class EjbExceptionMapper implements ExceptionMapper<EJBException> {

	@Override
	public Response toResponse(EJBException e) {
		ResponseBuilder response = Response.status(Status.NOT_FOUND);  
		
		if (e.getMessage().equals(MensagemSistema.ME013_ERRO_RELACIONAMENTO_JA_EXISTE.get())) {
			response.status(Status.INTERNAL_SERVER_ERROR);
			response.entity(MensagemSistema.ME013_ERRO_RELACIONAMENTO_JA_EXISTE.get() + " - Relacionamento já existe.");
		}
		else if (e.getMessage().equals(MensagemSistema.MA009_NENHUM_RESULTADO.get())) {
			response.status(Status.NOT_FOUND);
			response.entity(MensagemSistema.MA009_NENHUM_RESULTADO.get() + " - Nenhum resultado para os parâmetros da pesquisa.");
		}
		
		return response.build();
	}

}
