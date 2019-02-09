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
import br.gov.caixa.discovery.ws.modelos.InterfaceSistemaLink;
import br.gov.caixa.discovery.ws.modelos.InterfaceSistemaNode;
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
	public Response getInterface(String coSistema) {
		ResponseBuilder response = Response.status(Status.OK);
		HashMap<String, Integer> mapaNodes = new HashMap<>();

		List<InterfaceSistemaView> listaPersistence = viewDao.getInterfacesSistema(coSistema);
		List<InterfaceSistemaNode> listaNodes = new ArrayList<>();
		List<InterfaceSistemaLink> listaLinks = new ArrayList<>();

		mapaNodes.put(coSistema + "ESQUERDA", 0);
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

			InterfaceSistemaNode nodeArtefato = new InterfaceSistemaNode();

			nodeArtefato.setNode(mapaNodes.get(persistence.getNoNomeArtefatoPai()));
			nodeArtefato.setName(persistence.getNoNomeExibicaoPai());

			if (!existeNode(listaNodes, nodeArtefato)) {
				listaNodes.add(nodeArtefato);
			}

			InterfaceSistemaNode nodeSistemaPai = new InterfaceSistemaNode();

			nodeSistemaPai.setNode(mapaNodes.get(persistence.getCoSistemaPai() + "ESQUERDA"));
			nodeSistemaPai.setName(persistence.getCoSistemaPai());

			if (!existeNode(listaNodes, nodeSistemaPai)) {
				listaNodes.add(nodeSistemaPai);
			}

			InterfaceSistemaNode nodeSistema = new InterfaceSistemaNode();

			nodeSistema.setNode(mapaNodes.get(persistence.getCoSistema() + "DIREITA"));
			nodeSistema.setName(persistence.getCoSistema());

			if (!existeNode(listaNodes, nodeSistema)) {
				listaNodes.add(nodeSistema);
			}

			//
			// LINKS
			//

			InterfaceSistemaLink linkOrigemArtefato = new InterfaceSistemaLink();

			linkOrigemArtefato.setSource(mapaNodes.get(persistence.getCoSistemaPai() + "ESQUERDA"));
			linkOrigemArtefato.setTarget(mapaNodes.get(persistence.getNoNomeArtefatoPai()));
			linkOrigemArtefato.setValue(1);

			if (!existeLink(listaLinks, linkOrigemArtefato)) {
				listaLinks.add(linkOrigemArtefato);
			}

			InterfaceSistemaLink linkArtefatoDestino = new InterfaceSistemaLink();

			linkArtefatoDestino.setSource(mapaNodes.get(persistence.getNoNomeArtefatoPai()));
			linkArtefatoDestino.setTarget(mapaNodes.get(persistence.getCoSistema() + "DIREITA"));
			linkArtefatoDestino.setValue(1);

			if (!existeLink(listaLinks, linkArtefatoDestino)) {
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

	private static boolean existeNode(List<InterfaceSistemaNode> lista, InterfaceSistemaNode node) {

		for (InterfaceSistemaNode entry : lista) {
			if (node.getNode().equals(entry.getNode())) {
				return true;
			}
		}

		return false;
	}

	private static boolean existeLink(List<InterfaceSistemaLink> lista, InterfaceSistemaLink link) {

		for (InterfaceSistemaLink entry : lista) {

			if (link.getSource().equals(entry.getSource()) && link.getTarget().equals(entry.getTarget())) {
				return true;
			}

		}

		return false;
	}

}
