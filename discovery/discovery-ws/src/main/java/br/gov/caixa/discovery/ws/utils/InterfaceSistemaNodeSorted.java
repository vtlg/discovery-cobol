package br.gov.caixa.discovery.ws.utils;

import java.util.Comparator;

import br.gov.caixa.discovery.ws.modelos.InterfaceSistemaNode;

public class InterfaceSistemaNodeSorted implements Comparator<InterfaceSistemaNode> {

	@Override
	public int compare(InterfaceSistemaNode o1, InterfaceSistemaNode o2) {

		return o1.getNode() - o2.getNode();

	}

}
