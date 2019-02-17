package br.gov.caixa.discovery.ws.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.discovery.ws.modelos.RelacionamentoDomain;

@Path("/relacionamento")
public interface RelacionamentoResourceI {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response incluir(RelacionamentoDomain relacionamentoDomain);

	@POST
	@Path("{coRelacionamento}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizar(@PathParam("coRelacionamento") Long coRelacionamento,
			RelacionamentoDomain relacionamentoDomain);

	@GET
	@Path("interface/{sistema}/sankey")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInterfaceDiagramaSankey(@PathParam("sistema") String coSistema);

	@GET
	@Path("interface/{sistema}/tabela")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInterfaceDiagramaTabela(@PathParam("sistema") String coSistema);

}
