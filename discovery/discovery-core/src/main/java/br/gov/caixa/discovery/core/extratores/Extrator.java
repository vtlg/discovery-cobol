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

import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.utils.ArtefatoHandler;
import br.gov.caixa.discovery.core.utils.Configuracao;

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

	public List<Artefato> converter() {
		if (this.listaCaminhoArquivos == null && listaCaminhoArquivos.size() == 0) {
			LOGGER.log(Level.WARNING, "Nenhum arquivo para extração!");
			return null;
		}

		List<Artefato> listaArtefato = new ArrayList<>();

		for (String caminhoArquivo : this.listaCaminhoArquivos) {
			Path path = Paths.get(caminhoArquivo);
			_validarArquivo(path);
			_extrairodigoCompleto(path);
			List<String> codigoCompleto = _extrairodigoCompleto(path);

			Artefato artefato = new Artefato();
			artefato.setAmbiente(Configuracao.AMBIENTE);
			artefato.setSistema(Configuracao.SISTEMA);
			artefato.setTipoArtefato(this.tipoArtefato);
			artefato.setCodigoCompleto(codigoCompleto);
			artefato.setCaminhoArquivo(path.toAbsolutePath().toString());
			artefato.setNomeArquivo(ArtefatoHandler.tratarNomeArtefato(path.getFileName().toString())); //
			artefato.setNome(ArtefatoHandler.tratarNomeArtefato(artefato.getNomeArquivo()));

			if (TipoArtefato.PROGRAMA_COBOL.equals(tipoArtefato)) { // ExtratorCobol
				ExtratorProgramaCobol extrator = new ExtratorProgramaCobol(artefato, deslocamento);
				artefato = extrator.executa();
				listaArtefato.add(artefato);
			}
			if (TipoArtefato.COPYBOOK.equals(tipoArtefato)) { // ExtratorCopybook
				ExtratorCopybook extrator = new ExtratorCopybook(artefato, deslocamento);
				artefato = extrator.executa();
				listaArtefato.add(artefato);
			}
			if (TipoArtefato.JCL.equals(tipoArtefato)) { // ExtratorJcl
				ExtratorJcl extrator = new ExtratorJcl(artefato, deslocamento);
				artefato = extrator.executa();
				listaArtefato.add(artefato);
			}
		}

		return listaArtefato;
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

	private List<String> _extrairodigoCompleto(Path path) {
		List<String> tempCodigoCompleto = new ArrayList<>();

		try {
			tempCodigoCompleto = Files.readAllLines(path, Charset.forName("UTF-8"));
		} catch (MalformedInputException e) {
			try {
				tempCodigoCompleto = Files.readAllLines(path, Charset.forName("Cp1252"));
			} catch (IOException e1) {
				LOGGER.log(Level.SEVERE, "Charset do arquivo " + path.toAbsolutePath().toString() + " não localizado.");
				return null;
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Erro ao abrir o arquivo " + path.toAbsolutePath().toString() + ".");
			return null;
		}
		return tempCodigoCompleto;
	}

}
