package br.gov.caixa.discovery.ws.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.discovery.ws.modelos.RelacionamentoDomain;

@Path("/relacionamento")
public interface RelacionamentoResourceI {

	@POST
	@Path("{coRelacionamento}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizar(@PathParam("coRelacionamento") String coRelacionamento,
			RelacionamentoDomain relacionamentoDomain);

}
