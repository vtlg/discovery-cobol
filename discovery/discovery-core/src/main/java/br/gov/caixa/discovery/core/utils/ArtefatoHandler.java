package br.gov.caixa.discovery.core.utils;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.EJBException;

import br.gov.caixa.discovery.core.extratores.Extrator;
import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.tipos.TipoAmbiente;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.ejb.dao.ArtefatoDao;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.injetores.Discovery;

public class ArtefatoHandler {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
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

	public static ArtefatoPersistence buscarArtefatoPersistence(String nomeArtefato, TipoArtefato tipoArtefato,
			String sistemaArtefato, String nomeArtefatoPai, TipoArtefato tipoArtefatoPai, String sistemaArtefatoPai) {
		if (Discovery.em == null) {
			return null;
		}

		String tipoArtefatoPesquisa = null;
		if (!TipoArtefato.DESCONHECIDO.equals(tipoArtefato) && tipoArtefato != null) {
			tipoArtefatoPesquisa = tipoArtefato.get();
		}

		ArtefatoPersistence output = null;
		ArtefatoDao artefatoDao = new ArtefatoDao(Discovery.em);

		List<ArtefatoPersistence> listaResultado = null;
		
		try {
			listaResultado = artefatoDao.getListaArtefato(nomeArtefato, tipoArtefatoPesquisa, null, null, true);
		} catch (EJBException e) {
			LOGGER.log(Level.SEVERE, "Erro ao pesquisar artefatos", e);
		}

		if (TipoArtefato.JCL.equals(tipoArtefatoPai)
				&& (TipoArtefato.DESCONHECIDO.equals(tipoArtefato) || tipoArtefato == null)) {
			// CASO O ARTEFATO PAI ANALISADO FOR UM JCL
			// MAIS PROVÁVEL QUE SEJA A CHAMADA DE UM PROGRAMA COBOL
			// EM SEGUNDO CASO, PROCURA POR UM JCL
			// EM ÚLTIMO CASO, RETORNA O PRIMEIRO REGISTRO

			List<ArtefatoPersistence> entry = null;
			entry = listaResultado.stream()
					.filter((p) -> TipoArtefato.PROGRAMA_COBOL.get().equals(p.getCoTipoArtefato()))
					.collect(Collectors.toList());
			if (entry != null && entry.size() > 0) {
				return entry.get(0);
			}

			entry = listaResultado.stream().filter((p) -> TipoArtefato.UTILITARIO.get().equals(p.getCoTipoArtefato()))
					.collect(Collectors.toList());
			if (entry != null && entry.size() > 0) {
				return entry.get(0);
			}

			if (nomeArtefato.equals(nomeArtefatoPai)) {
				return null;
			}

			entry = listaResultado.stream().filter((p) -> TipoArtefato.JCL.get().equals(p.getCoTipoArtefato()))
					.collect(Collectors.toList());

			if (entry != null && entry.size() > 0) {
				return entry.get(0);
			}

			if (listaResultado != null && listaResultado.size() > 0) {
				return listaResultado.get(0);
			}
		} else if (TipoArtefato.PROGRAMA_COBOL.equals(tipoArtefatoPai)
				&& (TipoArtefato.DESCONHECIDO.equals(tipoArtefato) || tipoArtefato == null)) {
			// Caso o artefato pai analisado for um Programa Cobol
			// o mais provável que seja a chamada de um programa cobol
			// em segundo caso, procura por uma tabela
			// em último caso, retorna o primeiro registro

			List<ArtefatoPersistence> entry = null;

			entry = listaResultado.stream().filter((p) -> TipoArtefato.UTILITARIO.get().equals(p.getCoTipoArtefato()))
					.collect(Collectors.toList());
			if (entry != null && entry.size() > 0) {
				return entry.get(0);
			}

			if (nomeArtefato.equals(nomeArtefatoPai)) {
				return null;
			}

			entry = listaResultado.stream().filter((p) -> TipoArtefato.PROGRAMA_COBOL.get().equals(p.getTipoArtefato()))
					.collect(Collectors.toList());
			if (entry != null && entry.size() > 0) {
				return entry.get(0);
			}

			// entry = listaResultado.stream().filter((p) ->
			// TipoArtefato.TABELA.get().equals(p.getTipoArtefato()))
			// .collect(Collectors.toList());

			// if (entry != null && entry.size() > 0) {
			// return entry.get(0);
			// }

			if (listaResultado != null && listaResultado.size() > 0) {
				return listaResultado.get(0);
			}
		} else if (!TipoArtefato.DESCONHECIDO.equals(tipoArtefato) && tipoArtefato == null) {
			return listaResultado.get(0);
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
			} else if (artefato.getTipoArtefato().equals(tipo) && artefato.getNome().equals(nome)) {
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
