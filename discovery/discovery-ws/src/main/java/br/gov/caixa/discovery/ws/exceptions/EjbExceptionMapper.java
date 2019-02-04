package br.gov.caixa.discovery.ws.exceptions;

import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EjbExceptionMapper implements ExceptionMapper<EJBException> {

	@Override
	public Response toResponse(EJBException arg0) {
		System.out.println("Teste Teste");
		return null;
	}

}
