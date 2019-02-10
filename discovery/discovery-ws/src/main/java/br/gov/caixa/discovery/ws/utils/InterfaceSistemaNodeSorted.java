package br.gov.caixa.discovery.ws.utils;

import java.util.Comparator;

import br.gov.caixa.discovery.ws.modelos.InterfaceSistemaDiagramaSankeyNode;

public class InterfaceSistemaNodeSorted implements Comparator<InterfaceSistemaDiagramaSankeyNode> {

	@Override
	public int compare(InterfaceSistemaDiagramaSankeyNode o1, InterfaceSistemaDiagramaSankeyNode o2) {

		return o1.getNode() - o2.getNode();

	}

}
