package br.gov.caixa.discovery.ws.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.discovery.ws.modelos.ArtefatoDomain;

@Path("/artefato")
public interface ArtefatoResourceI {

	@GET
	@Path("{coArtefato}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArtefato(@PathParam("coArtefato") Long coArtefato);

	@GET
	@Path("{coArtefato}/relacionamento")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRelacionamentos(@PathParam("coArtefato") Long coArtefato) throws Exception;
	
//	@GET
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response pesquisar(@QueryParam("termo") String termo);

	@POST
	@Path("{coArtefato}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizarArtefato(@PathParam("coArtefato") Long coArtefato, ArtefatoDomain artefato);

}
