package br.gov.caixa.discovery.ws.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	public Response getRelacionamentos(@PathParam("coArtefato") Long coArtefato);
	
}
