package br.gov.caixa.discovery.ws.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.discovery.ejb.dao.RelacionamentoDao;
import br.gov.caixa.discovery.ejb.dao.ViewDao;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.view.InterfaceSistemaView;
import br.gov.caixa.discovery.ws.modelos.InterfaceSistemaDomain;
import br.gov.caixa.discovery.ws.modelos.InterfaceSistemaDiagramaSankeyLink;
import br.gov.caixa.discovery.ws.modelos.InterfaceSistemaDiagramaSankeyNode;
import br.gov.caixa.discovery.ws.modelos.RelacionamentoDomain;
import br.gov.caixa.discovery.ws.utils.InterfaceSistemaNodeSorted;

@Stateless
public class RelacionamentoResource implements RelacionamentoResourceI {

	@EJB
	RelacionamentoDao relacionamentoDao;

	@EJB
	ViewDao viewDao;

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

	@Override
	public Response getInterfaceDiagramaSankey(String coSistema) {
		ResponseBuilder response = Response.status(Status.OK);
		HashMap<String, Integer> mapaNodes = new HashMap<>();

		List<InterfaceSistemaView> listaPersistence = viewDao.getInterfacesSistema(coSistema);
		List<InterfaceSistemaDiagramaSankeyNode> listaNodes = new ArrayList<>();
		List<InterfaceSistemaDiagramaSankeyLink> listaLinks = new ArrayList<>();

		mapaNodes.put(coSistema + "ESQUERDA", 0);
		mapaNodes.put(coSistema + "DIREITA", 0);
		int i = 1;
		for (InterfaceSistemaView persistence : listaPersistence) {
			if (!mapaNodes.containsKey(persistence.getNoNomeArtefatoPai())) {
				mapaNodes.put(persistence.getNoNomeArtefatoPai(), i);
				i++;
			}

			if (!mapaNodes.containsKey(persistence.getCoSistemaPai() + "ESQUERDA")) {
				mapaNodes.put(persistence.getCoSistemaPai() + "ESQUERDA", i);
				i++;
			}

			if (!mapaNodes.containsKey(persistence.getCoSistema() + "DIREITA")) {
				mapaNodes.put(persistence.getCoSistema() + "DIREITA", i);
				i++;
			}
		}

		for (InterfaceSistemaView persistence : listaPersistence) {

			//
			// NODES
			//

			InterfaceSistemaDiagramaSankeyNode nodeArtefato = new InterfaceSistemaDiagramaSankeyNode();

			nodeArtefato.setNode(mapaNodes.get(persistence.getNoNomeArtefatoPai()));
			nodeArtefato.setName(persistence.getNoNomeExibicaoPai());

			if (!sankeyExisteNode(listaNodes, nodeArtefato)) {
				listaNodes.add(nodeArtefato);
			}

			InterfaceSistemaDiagramaSankeyNode nodeSistemaPai = new InterfaceSistemaDiagramaSankeyNode();

			nodeSistemaPai.setNode(mapaNodes.get(persistence.getCoSistemaPai() + "ESQUERDA"));
			nodeSistemaPai.setName(persistence.getCoSistemaPai());

			if (!sankeyExisteNode(listaNodes, nodeSistemaPai)) {
				listaNodes.add(nodeSistemaPai);
			}

			InterfaceSistemaDiagramaSankeyNode nodeSistema = new InterfaceSistemaDiagramaSankeyNode();

			nodeSistema.setNode(mapaNodes.get(persistence.getCoSistema() + "DIREITA"));
			nodeSistema.setName(persistence.getCoSistema());

			if (!sankeyExisteNode(listaNodes, nodeSistema)) {
				listaNodes.add(nodeSistema);
			}

			//
			// LINKS
			//

//			InterfaceSistemaLink linkOrigemDestino = new InterfaceSistemaLink();
//			linkOrigemDestino.setSource(mapaNodes.get(persistence.getCoSistemaPai() + "ESQUERDA"));
//			linkOrigemDestino.setTarget(mapaNodes.get(persistence.getCoSistema() + "DIREITA"));
//			linkOrigemDestino.setValue(5);
//
//			if (!existeLink(listaLinks, linkOrigemDestino)) {
//				listaLinks.add(linkOrigemDestino);
//			}			

			InterfaceSistemaDiagramaSankeyLink linkOrigemArtefato = new InterfaceSistemaDiagramaSankeyLink();

			linkOrigemArtefato.setSource(mapaNodes.get(persistence.getCoSistemaPai() + "ESQUERDA"));
			linkOrigemArtefato.setTarget(mapaNodes.get(persistence.getNoNomeArtefatoPai()));
			linkOrigemArtefato.setValue(5);

			if (!sankeyExisteLink(listaLinks, linkOrigemArtefato)) {
				listaLinks.add(linkOrigemArtefato);
			}

			InterfaceSistemaDiagramaSankeyLink linkArtefatoDestino = new InterfaceSistemaDiagramaSankeyLink();

			linkArtefatoDestino.setSource(mapaNodes.get(persistence.getNoNomeArtefatoPai()));
			linkArtefatoDestino.setTarget(mapaNodes.get(persistence.getCoSistema() + "DIREITA"));
			linkArtefatoDestino.setValue(1);

			if (!sankeyExisteLink(listaLinks, linkArtefatoDestino)) {
				listaLinks.add(linkArtefatoDestino);
			}

		}

		Collections.sort(listaNodes, new InterfaceSistemaNodeSorted());

		InterfaceSistemaDomain domain = new InterfaceSistemaDomain();
		domain.setNodes(listaNodes);
		domain.setLinks(listaLinks);

		response.entity(domain);

		return response.build();
	}

	private static boolean sankeyExisteNode(List<InterfaceSistemaDiagramaSankeyNode> lista,
			InterfaceSistemaDiagramaSankeyNode node) {

		for (InterfaceSistemaDiagramaSankeyNode entry : lista) {
			if (node.getNode().equals(entry.getNode())) {
				return true;
			}
		}

		return false;
	}

	private static boolean sankeyExisteLink(List<InterfaceSistemaDiagramaSankeyLink> lista,
			InterfaceSistemaDiagramaSankeyLink link) {

		for (InterfaceSistemaDiagramaSankeyLink entry : lista) {

			if (link.getSource().equals(entry.getSource()) && link.getTarget().equals(entry.getTarget())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Response getInterfaceDiagramaTabela(String coSistema) {
		ResponseBuilder response = Response.status(Status.OK);

		List<InterfaceSistemaView> listaPersistence = viewDao.getInterfacesSistema(coSistema);
		List<InterfaceSistemaDomain> listaDomain = new ArrayList<>();

		for (InterfaceSistemaView persistence : listaPersistence) {
			InterfaceSistemaDomain domain = new InterfaceSistemaDomain();

			domain.setCaminhoCoArtefato(persistence.getCaminhoCoArtefato());
			domain.setCoTipoRelacionamentoInicial(persistence.getCoTipoRelacionamentoInicial());

			domain.setCoSistemaDestino(persistence.getCoSistemaDestino());

			domain.setCoArtefato(persistence.getCoArtefato());
			domain.setCoSistema(persistence.getCoSistema());
			domain.setCoTipoArtefato(persistence.getCoTipoArtefato());
			domain.setNoNomeArtefato(persistence.getNoNomeArtefato());
			domain.setNoNomeExibicao(persistence.getNoNomeExibicao());
			domain.setNoNomeInterno(persistence.getNoNomeInterno());

			domain.setCoArtefatoPai(persistence.getCoArtefatoPai());
			domain.setCoSistemaPai(persistence.getCoSistemaPai());
			domain.setCoTipoArtefatoPai(persistence.getCoTipoArtefatoPai());
			domain.setNoNomeArtefatoPai(persistence.getNoNomeArtefatoPai());
			domain.setNoNomeExibicaoPai(persistence.getNoNomeExibicaoPai());
			domain.setNoNomeInternoPai(persistence.getNoNomeInternoPai());

			if (!tabelaExisteDomain(listaDomain, domain)) {
				listaDomain.add(domain);
			}

		}

		response.entity(listaDomain);

		return response.build();
	}

	private static boolean tabelaExisteDomain(List<InterfaceSistemaDomain> lista, InterfaceSistemaDomain domain) {

		for (InterfaceSistemaDomain entry : lista) {
			if (domain.getCoSistema().equals(entry.getCoSistema())
					&& domain.getCoSistemaPai().equals(entry.getCoSistemaPai())
					&& domain.getCoArtefato().equals(entry.getCoArtefato())
					&& domain.getCoArtefatoPai().equals(entry.getCoArtefatoPai())) {
				return true;
			}
		}

		return false;
	}

}
