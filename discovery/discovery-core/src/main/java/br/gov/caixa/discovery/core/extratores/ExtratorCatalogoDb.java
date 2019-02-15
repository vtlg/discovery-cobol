package br.gov.caixa.discovery.core.extratores;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.modelos.Atributo;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.tipos.TipoAtributo;
import br.gov.caixa.discovery.core.utils.Configuracao;

public class ExtratorCatalogoDb {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private Path caminhoArquivo = null;
	private List<Artefato> listaArtefato = null;
	private String texto = null;

	public ExtratorCatalogoDb() {
		super();
	}

	public static void main(String[] args) throws Exception {
		ExtratorCatalogoDb extrator = new ExtratorCatalogoDb();
		extrator.inicializar(
				"D:\\CAIXA\\backup_notebook_2019_01_10\\codigo\\SIFDL\\CEFPRD\\catalog\\'DES.FDL.MZ.BHX0.DISCOVER.CATALOG.FDLDB001'");
		extrator.executa();
	}

	public void inicializar(String caminhoArquivo) {
		inicializar(Paths.get(caminhoArquivo));
	}

	public void inicializar(Path caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
		this.listaArtefato = new ArrayList<>();
	}

	public List<Artefato> executa() {

		try {
			extrairTexto();
			extrairTabelas();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.listaArtefato;
	}

	private void extrairTexto() throws Exception {
		List<String> tempCodigoCompleto = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		try {
			tempCodigoCompleto = Files.readAllLines(caminhoArquivo, Charset.forName("UTF-8"));
		} catch (MalformedInputException e) {
			try {
				tempCodigoCompleto = Files.readAllLines(caminhoArquivo, Charset.forName("Cp1252"));
			} catch (IOException e1) {
				try {
					tempCodigoCompleto = Files.readAllLines(caminhoArquivo, Charset.forName("ISO-8859-1"));
				} catch (IOException e2) {
					LOGGER.log(Level.SEVERE,
							"Charset do arquivo " + caminhoArquivo.toAbsolutePath().toString() + " não localizado.",
							e2);
				}
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Erro ao abrir o arquivo " + caminhoArquivo.toAbsolutePath().toString() + ".");
		}

		for (String texto : tempCodigoCompleto) {

			if (!texto.trim().startsWith("--")) {
				sb.append(" " + texto);
				// listaTexto.add(texto);
			}
		}

		texto = sb.toString().replaceAll("[\\s]{2,}", " ");
	}

	private void extrairTabelas() {

		// public static Pattern COBOL_P_JOIN =
		// Pattern.compile("[\\s]{1,}JOIN[\\s]{1,}(?<tabela>[\\S]{1,})[\\s]{0,}");

		Pattern CATALOGO_DB_CREATE_TABLE = Pattern.compile("[\\s]{1,1}(?<createTableStmt>CREATE TABLE .*?);");

		Pattern CATALOGO_DB_TABLE_NOME_CAMPOS = Pattern
				.compile("^CREATE TABLE (?<nomeTabela>[\\S]+?)[\\s]+?\\((?<camposTabela>.*), CONSTRAINT.*");

		Pattern CATALOGO_DB_TABLE_CAMPO = Pattern.compile("^(?<nomeCampo>[\\S]+?)[\\s]+?(?<atributos>.*)$");

		Matcher m_create_table_stmt = CATALOGO_DB_CREATE_TABLE.matcher(this.texto);
		Matcher m_table_nome_campos = null;
		Matcher m_table_campo = null;

		while (m_create_table_stmt.find()) {
			String createTableStmt = m_create_table_stmt.group("createTableStmt");
			// System.out.println(createTableStmt);

			m_table_nome_campos = CATALOGO_DB_TABLE_NOME_CAMPOS.matcher(createTableStmt);
			// m_table_campos = CATALOGO_DB_TABLE_CAMPOS.matcher(createTableStmt);

			if (m_table_nome_campos.matches()) {
				String nomeTabelaMatch = m_table_nome_campos.group("nomeTabela");
				String camposTabelaMatch = m_table_nome_campos.group("camposTabela");

				String nomeTabela = null;

				if (nomeTabelaMatch.contains(".")) {
					nomeTabela = nomeTabelaMatch.split("\\.")[1];
				} else {
					nomeTabela = nomeTabelaMatch;
				}

				Artefato artefatoTabela = new Artefato();
				artefatoTabela.setNome(nomeTabela);
				artefatoTabela.setNomeInterno(nomeTabela);
				artefatoTabela.setTipoArtefato(TipoArtefato.TABELA);
				artefatoTabela.setSistema(Configuracao.SISTEMA);
				artefatoTabela.setAmbiente(Configuracao.AMBIENTE);

				Integer posicao = 0;
				for (String campo : camposTabelaMatch.split(",(?![^\\(]*\\))")) {
					m_table_campo = CATALOGO_DB_TABLE_CAMPO.matcher(campo.trim());
					// System.out.println(campo.trim());
					if (m_table_campo.matches()) {

						Artefato artefatoCampo = new Artefato();
						artefatoCampo.setNome(m_table_campo.group("nomeCampo"));
						artefatoCampo.setNomeInterno(m_table_campo.group("nomeCampo"));
						artefatoCampo.setTipoArtefato(TipoArtefato.TABELA_CAMPO);
						artefatoCampo.setSistema(Configuracao.SISTEMA);
						artefatoCampo.setAmbiente(Configuracao.AMBIENTE);
						artefatoCampo.setPosicao(posicao);
						
						Atributo atributo = new Atributo();
						atributo.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
						atributo.setValor(m_table_campo.group("atributos"));
						
						Atributo atributoPosicao = new Atributo();
						atributoPosicao.setTipoAtributo(TipoAtributo.POSICAO);
						atributoPosicao.setValor(String.valueOf(posicao));

						artefatoCampo.adicionarAtributo(atributo);
						artefatoCampo.adicionarAtributo(atributoPosicao);
						artefatoTabela.adicionarArtefatosRelacionados(artefatoCampo);
						posicao++;
					}
				}

				listaArtefato.add(artefatoTabela);

			}
		}
	}
}
