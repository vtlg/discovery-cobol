package br.gov.caixa.discovery.core.utils;

import java.util.List;

import br.gov.caixa.discovery.core.extratores.Extrator;
import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.tipos.TipoAmbiente;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;

public class ArtefatoHandler {
	public static String tratarNomeArtefato(String nome) {
		String output = nome;

		try {
			if (nome.contains(".")) {
				output = nome.split("\\.")[0];
			} else if (nome.contains(",")) {
				output = nome.split(",")[0];
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return output;
	}

	public static Artefato buscarArtefato(String nomeArtefato, TipoArtefato tipoArtefato) {
		if (nomeArtefato == null || nomeArtefato.trim().equals("")) {
			return null;
		}
		if (tipoArtefato == null) {
			return null;
		}

		for (Artefato artefato : Configuracao.COLLECTION_ARTEFATO) {
			if (TipoArtefato.DESCONHECIDO.equals(tipoArtefato)
					&& TipoArtefato.DESCONHECIDO.equals(artefato.getTipoArtefato())
					&& nomeArtefato.equals(artefato.getNome())) {
				return artefato;
			} else if (nomeArtefato.equals(artefato.getNome()) && tipoArtefato.equals(artefato.getTipoArtefato())) {
				return artefato;
			}
		}

		return null;
	}

	public static TipoArtefato identificarTipoArtefato(String nomeArtefato) {
		if (nomeArtefato == null || nomeArtefato.trim().equals("")) {
			return null;
		}

		for (Artefato artefato : Configuracao.COLLECTION_ARTEFATO) {
			if (nomeArtefato.equals(artefato.getNome())) {
				return artefato.getTipoArtefato();
			}
		}

		return TipoArtefato.DESCONHECIDO;
	}

	public static TipoAmbiente identificarAmbiente(String nomeArtefato, TipoArtefato tipoArtefato) {
		if (nomeArtefato == null || nomeArtefato.trim().equals("")) {
			return null;
		}
		if (tipoArtefato == null) {
			return null;
		}

		for (Artefato artefato : Configuracao.COLLECTION_ARTEFATO) {
			if (nomeArtefato.equals(artefato.getNome()) && tipoArtefato.equals(artefato.getTipoArtefato())) {
				return artefato.getAmbiente();
			}
		}

		return TipoAmbiente.DESCONHECIDO;
	}

	public static String identificarSistema(String nomeArtefato, TipoArtefato tipoArtefato) {
		if (nomeArtefato == null || nomeArtefato.trim().equals("")) {
			return null;
		}
		if (tipoArtefato == null) {
			return null;
		}

		for (Artefato artefato : Configuracao.COLLECTION_ARTEFATO) {
			if (nomeArtefato.equals(artefato.getNome()) && tipoArtefato.equals(artefato.getTipoArtefato())) {
				return artefato.getSistema();
			}
		}

		return "DESCONHECIDO";
	}

	public static Artefato recuperarArtefato(List<Artefato> lista, TipoArtefato tipo, String nome) {
		for (Artefato artefato : lista) {

			if (tipo == null && artefato.getNome().equals(nome)) {
				return artefato;
			} else if (artefato.getTipoArtefato() != null && artefato.getTipoArtefato().equals(tipo)
					&& artefato.getNome().equals(nome)) {
				return artefato;
			}
		}
		return null;
	}

	public static boolean existeArtefato(List<Artefato> lista, String nome) {
		for (Artefato artefato : lista) {
			if (artefato.getNome().equals(nome)) {
				return true;
			}
		}
		return false;
	}

	public static boolean existeArtefato(List<Artefato> lista, TipoArtefato tipo, String nome) {

		for (Artefato artefato : lista) {
			if (artefato.getTipoArtefato() == null) {
				boolean valor = existeArtefato(lista, nome);
				if (valor) {
					return true;
				}
			} else  if (artefato.getTipoArtefato().equals(tipo) && artefato.getNome().equals(nome)) {
				return true;
			}
		}
		return false;
	}

	public static Artefato extrairArtefato(String nome, TipoArtefato tipoArtefato, int deslocamento) {

		if (!TipoArtefato.COPYBOOK.equals(tipoArtefato) || nome == null || nome.trim().equals("")) {
			return null;
		}

		Artefato artefatoOutput = null;
		Artefato artefatoPesquisa = null;

		for (Artefato copybook : Configuracao.COLLECTION_ARTEFATO) {
			if (copybook.getNome().equals(nome)) {
				artefatoPesquisa = copybook;
			}
		}

		if (artefatoPesquisa != null) {
			Extrator extrator = new Extrator();
			extrator.inicializar(artefatoPesquisa.getCaminhoArquivo(), TipoArtefato.COPYBOOK);
			List<Artefato> lista = extrator.converter();
			if (lista != null && lista.size() > 0) {
				artefatoOutput = lista.get(0);
			}
		}
		return artefatoOutput;
	}

}
