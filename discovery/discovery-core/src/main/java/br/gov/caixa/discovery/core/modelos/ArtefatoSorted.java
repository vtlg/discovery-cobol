package br.gov.caixa.discovery.core.modelos;

import java.util.Comparator;

public class ArtefatoSorted implements Comparator<Artefato> {

	@Override
	public int compare(Artefato o1, Artefato o2) {

		return o1.getPosicao() - o2.getPosicao();

	}

}
