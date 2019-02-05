package br.gov.caixa.discovery.core.extratores;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.utils.ArtefatoHandler;
import br.gov.caixa.discovery.core.utils.Configuracao;
import br.gov.caixa.discovery.core.utils.Patterns;

public class Extrator {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	List<String> listaCaminhoArquivos = new ArrayList<>();
	private TipoArtefato tipoArtefato;
	private int deslocamento;

	// private Artefato artefato;
	// private String nomeArquivo;
	// private byte[] tempArquivo;
	// private List<String> tempCodigoCompleto = new ArrayList<>();

	public void inicializar(String caminhoArquivo, TipoArtefato tipoArtefato) {
		inicializar(caminhoArquivo, tipoArtefato, 0);
	}

	public void inicializar(String caminhoArquivo, TipoArtefato tipoArtefato, int deslocamento) {
		List<String> listaCaminhoArquivos = new ArrayList<>();
		listaCaminhoArquivos.add(caminhoArquivo);
		inicializar(listaCaminhoArquivos, tipoArtefato, deslocamento);
	}

	public void inicializar(List<String> listaCaminhoArquivos, TipoArtefato tipoArtefato, int deslocamento) {
		this.listaCaminhoArquivos = listaCaminhoArquivos;
		this.tipoArtefato = tipoArtefato;
		this.deslocamento = deslocamento;
	}

	public void inicializar(List<Path> listaArquivos, TipoArtefato tipoArtefato2) {
		List<String> tempLista = listaArquivos.stream().map((o) -> {
			return o.toAbsolutePath().toString();
		}).collect(Collectors.toList());

		this.listaCaminhoArquivos = tempLista;
		this.tipoArtefato = tipoArtefato2;
	}

	public List<Artefato> converter() {
		if (this.listaCaminhoArquivos == null && listaCaminhoArquivos.size() == 0) {
			LOGGER.log(Level.WARNING, "Nenhum arquivo para extração!");
			return null;
		}

		List<Artefato> listaArtefato = new ArrayList<>();

		if (TipoArtefato.CONTROL_M.equals(this.tipoArtefato) && Configuracao.CARGA_INICIAL == false) {
			ExtratorControlM extrator = new ExtratorControlM();
			extrator.inicializar(listaCaminhoArquivos);
			listaArtefato = extrator.executa();
		} else {
			for (String caminhoArquivo : this.listaCaminhoArquivos) {
				Path path = Paths.get(caminhoArquivo);
				_validarArquivo(path);
				// _extrairCodigoCompleto(path);
				List<String> codigoCompleto = _extrairCodigoCompleto(path);

				Artefato artefato = new Artefato();
				artefato.setAmbiente(Configuracao.AMBIENTE);
				artefato.setSistema(Configuracao.SISTEMA);
				artefato.setTipoArtefato(this.tipoArtefato);
				artefato.setCaminhoArquivo(path.toAbsolutePath().toString());

				if (Configuracao.CARGA_INICIAL == false) {
					artefato.setCodigoCompleto(codigoCompleto);
				}
				// artefato.setNomeArquivo(ArtefatoHandler.tratarNomeArtefato(); //

				String nomeArtefato = path.getFileName().toString();

				Matcher m_extrator_p_nome_artefato = Patterns.EXTRATOR_P_NOME_ARTEFATO
						.matcher(path.getFileName().toString());
				if (m_extrator_p_nome_artefato.matches()) {
					nomeArtefato = m_extrator_p_nome_artefato.group("parametro");
				}

				artefato.setNome(ArtefatoHandler.tratarNomeArtefato(nomeArtefato));

				if (TipoArtefato.PROGRAMA_COBOL.equals(tipoArtefato) && Configuracao.CARGA_INICIAL == false) { // ExtratorCobol
					ExtratorProgramaCobol extrator = new ExtratorProgramaCobol(artefato, deslocamento);
					artefato = extrator.executa();
					listaArtefato.add(artefato);
				}
				if (TipoArtefato.COPYBOOK.equals(tipoArtefato) && Configuracao.CARGA_INICIAL == false) { // ExtratorCopybook
					ExtratorCopybook extrator = new ExtratorCopybook(artefato, deslocamento);
					artefato = extrator.executa();
					listaArtefato.add(artefato);
				}
				if (TipoArtefato.JCL.equals(tipoArtefato) && Configuracao.CARGA_INICIAL == false) { // ExtratorJcl
					ExtratorJcl extrator = new ExtratorJcl(artefato, deslocamento);
					artefato = extrator.executa();
					listaArtefato.add(artefato);
				}
				if (Configuracao.CARGA_INICIAL) {
					listaArtefato.add(artefato);
				}

				listaArtefato = removerArtefatosNaoIncluir(listaArtefato);

			}
		}

		return listaArtefato;
	}

	private List<Artefato> removerArtefatosNaoIncluir(List<Artefato> listaEntrada) {
		if (listaEntrada == null || listaEntrada.size() == 0) {
			return null;
		}

		List<Artefato> listaSaida = new ArrayList<>();

		for (Artefato entry : listaEntrada) {
			entry.setArtefatosRelacionados(removerArtefatosNaoIncluir(entry.getArtefatosRelacionados()));

			if (TipoArtefato.COPYBOOK_VARIAVEL.equals(entry.getTipoArtefato())
					|| TipoArtefato.JCL_VARIAVEL.equals(entry.getTipoArtefato())
					|| TipoArtefato.PROGRAMA_COBOL_PARAGRAFO.equals(entry.getTipoArtefato())) {
				continue;
			}

			listaSaida.add(entry);

		}

		return listaSaida;
	}

	private boolean _validarArquivo(Path path) {
		if (path.toAbsolutePath().toString() == null) {
			LOGGER.log(Level.SEVERE, "" + path.toAbsolutePath().toString() + " não é um arquivo.");
			return false;
		}

		if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			LOGGER.log(Level.SEVERE, "Arquivo " + path.toAbsolutePath().toString() + " não localizado.");
			return false;
		} else if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
			LOGGER.log(Level.SEVERE, "" + path.toAbsolutePath().toString() + " não é um arquivo.");
			return false;
		}
		return true;
	}

	private List<String> _extrairCodigoCompleto(Path path) {
		List<String> tempCodigoCompleto = new ArrayList<>();

		try {
			tempCodigoCompleto = Files.readAllLines(path, Charset.forName("UTF-8"));
		} catch (MalformedInputException e) {
			try {
				tempCodigoCompleto = Files.readAllLines(path, Charset.forName("Cp1252"));
			} catch (IOException e1) {
				try {
					tempCodigoCompleto = Files.readAllLines(path, Charset.forName("ISO-8859-1"));
				} catch (IOException e2) {
					LOGGER.log(Level.SEVERE,
							"Charset do arquivo " + path.toAbsolutePath().toString() + " não localizado.", e2);
					return null;
				}
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Erro ao abrir o arquivo " + path.toAbsolutePath().toString() + ".");
			return null;
		}
		return tempCodigoCompleto;
	}

}
