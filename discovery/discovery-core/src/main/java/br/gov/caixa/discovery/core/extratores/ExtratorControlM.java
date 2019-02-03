package br.gov.caixa.discovery.core.extratores;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.modelos.Atributo;
import br.gov.caixa.discovery.core.tipos.TipoAmbiente;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.tipos.TipoAtributo;
import br.gov.caixa.discovery.core.tipos.TipoRelacionamento;
import br.gov.caixa.discovery.ejb.tipos.Tabelas;

public class ExtratorControlM {

	private HashMap<String, List<String>> mapLista = new HashMap<>();
	private List<Artefato> listaArtefatos = new ArrayList<>();
	private HashMap<String, String> dicionario = new HashMap<>();

	public static void main(String[] args) throws IOException {
		Stream<Path> listaPaths = Files
				.list(Paths.get("D:\\CAIXA\\backup_notebook_2019_01_10\\codigo\\sipcs\\CEFPRD\\MALHA\\"));

		ExtratorControlM extrator = new ExtratorControlM();
		extrator.inicializar(listaPaths);
		extrator.executa();
	}

	public ExtratorControlM() {
	}

	public void inicializar(List<String> listaArquivo) {
		inicializar(listaArquivo.stream().map((o) -> {
			return Paths.get(o);
		}));
	}

	public void inicializar(Stream<Path> listaArquivo) {
		listaArquivo.forEach((path) -> {
			try {
				if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)
						&& Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
					try {
						mapLista.put(path.getFileName().toString(), Files.readAllLines(path, Charset.forName("UTF-8")));
					} catch (MalformedInputException e) {
						mapLista.put(path.getFileName().toString(),
								Files.readAllLines(path, Charset.forName("Cp1252")));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public List<Artefato> executa() {

		extrairRelacionamento(mapLista);
		criarDicionario();
		traduzir();
		removerDuplicidade();
		ajustarArtefatos();
		ajustarRelacionamentos();

		/*
		 * for (Artefato artefato : listaArtefatos) { exibir(artefato, ""); }
		 */

		// criarMapaRelacionamento();

		return listaArtefatos;
	}

	@SuppressWarnings("unused")
	private static void exibir(Artefato artefato, String pad) {
		//

		// if (!artefato.isMalhaControlm()) {
		System.out.println(pad + " " + artefato.getNome() + "       " + artefato.getCaminhoArquivo());
		// }

		if (artefato.getArtefatosRelacionados() != null && artefato.getArtefatosRelacionados().size() > 0) {
			for (Artefato entry : artefato.getArtefatosRelacionados()) {
				exibir(entry, pad.concat("--"));
			}
		}
	}

	private void ajustarRelacionamentos() {
		for (Artefato artefato : this.listaArtefatos) {
			ajustarRelacionamentos(artefato);
		}
	}

	private void ajustarRelacionamentos(Artefato artefato) {
		if (artefato.getArtefatosRelacionados() != null && artefato.getArtefatosRelacionados().size() > 0) {
			for (Artefato entry : artefato.getArtefatosRelacionados()) {
				entry.setTipoRelacionamento(TipoRelacionamento.CONTROL_M);
			}
		}
	}

	private void ajustarArtefatos() {
		List<Artefato> tempLista = new ArrayList<>();
		boolean isIncluir = true;
		for (Artefato artefato : listaArtefatos) {
			Artefato artefatoFilho = artefato.getArtefatosRelacionados().get(0);
			isIncluir = true;
			if (artefato.getNome().equals(artefatoFilho.getNome())) {
				if (!artefatoFilho.getNome().equals(artefatoFilho.getNomeInterno())) {
					artefatoFilho.setNome(artefatoFilho.getNomeInterno());
				} else if (!artefato.getNome().equals(artefato.getNomeInterno())) {
					artefato.setNome(artefato.getNomeInterno());
				} else {
					isIncluir = false;
					// throw new RuntimeException("Erro conversão control-m. Auto-relacionamento");
				}
			}

			if (isIncluir) {
				tempLista.add(artefato);
			}
		}

		listaArtefatos = tempLista;
	}

	private void extrairRelacionamento(HashMap<String, List<String>> mapListaArquivo) {

		Pattern P_OWNER_1 = Pattern.compile("^D(?<owner>[\\S]{1,})[\\s]{1,}.*$");
		Pattern P_MEMNAME_GROUP_1 = Pattern.compile("^M(?<memName>[\\S]{8,8})(?<group>[\\S]{1,})[\\s]{1,}.*$");
		Pattern P_MEMNAME_2 = Pattern.compile("^M(?<memName>[\\S]{1,})[\\s]{1,}.*$");
		Pattern P_DOCMEM_DOCLIB_1 = Pattern.compile("^Z(?<docMem>[\\S]{8,8})(?<docLib>[\\S]{1,})[\\s]{1,}.*$");
		Pattern P_DESCRICAO_1 = Pattern.compile("^H(?<descricao>.{1,})$");
		Pattern P_OVERLIB_1 = Pattern.compile("^V(?<overlib>[\\S]{1,})[\\s]{1,}.*$");

		Pattern P_INPUT = Pattern.compile("^I(?<inputs>.*)$");
		Pattern P_OUTPUT = Pattern.compile("^O(?<outputs>.*)$");

		Pattern P_INPUT_ITEM = Pattern
				.compile("(?<elementoEsquerda>[\\S]{8,8})-(?<elementoDireita>[\\S]{8,8})[-\\S\\s]{3,3}ODAT");
		// Pattern P_OUTPUT_ITEM = Pattern
		// .compile("(?<elementoEntrada>[\\S]{8,8})-(?<elementoSaida>[\\S]{8,8})-REODAT(?<sinal>[-|+]{1,1})");

		Pattern P_OUTPUT_ITEM = Pattern.compile(
				"(?<elementoEsquerda>[\\S]{8,8})-(?<elementoDireita>[\\S]{8,8})[-\\S\\s]{3,3}ODAT(?<sinal>[-|+]{1,1})");

		mapListaArquivo.forEach((arquivo, listaTexto) -> {

			Matcher m_owner_1 = null;
			Matcher m_memName_group_1 = null;
			Matcher m_memName_2 = null;
			Matcher m_docMem_docLib_group_1 = null;
			Matcher m_descricao = null;
			Matcher m_overlib = null;
			Matcher m_input = null;
			Matcher m_output = null;

			Matcher m_input_item = null;
			Matcher m_output_item = null;

			String memName = null;
			String groupName = null;
			String owner = null;
			String descricao = null;
			String docMem = null;
			String docLib = null;
			String overLib = null;

			Integer posicao = 1;
			for (String texto : listaTexto) {
				m_owner_1 = P_OWNER_1.matcher(texto);
				m_memName_group_1 = P_MEMNAME_GROUP_1.matcher(texto);
				m_docMem_docLib_group_1 = P_DOCMEM_DOCLIB_1.matcher(texto);
				m_descricao = P_DESCRICAO_1.matcher(texto);
				m_overlib = P_OVERLIB_1.matcher(texto);
				m_input = P_INPUT.matcher(texto);
				m_output = P_OUTPUT.matcher(texto);
				m_memName_2 = P_MEMNAME_2.matcher(texto);

				m_input_item = null;
				m_output_item = null;

				if (m_memName_group_1.matches() || m_memName_2.matches()) {
					memName = null;
					groupName = null;
					owner = null;
					descricao = null;
					docMem = null;
					docLib = null;
					overLib = null;

					if (m_memName_group_1.matches()) {
						memName = m_memName_group_1.group("memName");
						groupName = m_memName_group_1.group("group");
					} else if (m_memName_2.matches()) {
						memName = m_memName_2.group("memName");
					}
				}
				if (m_owner_1.matches()) {
					owner = m_owner_1.group("owner").trim();
				}
				if (m_descricao.matches()) {
					descricao = m_descricao.group("descricao").trim();
				}

				if (m_docMem_docLib_group_1.matches()) {
					docMem = m_docMem_docLib_group_1.group("docMem").trim();
					docLib = m_docMem_docLib_group_1.group("docLib").trim();
				}
				if (m_overlib.matches()) {
					overLib = m_overlib.group("overlib").trim();
				}

				Atributo atributoMemName = new Atributo(TipoAtributo.CONTROLM_MEMNAME, memName, null,
						Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				Atributo atributoGroupName = new Atributo(TipoAtributo.CONTROLM_GROUP_NAME, groupName, null,
						Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				Atributo atributoOwner = new Atributo(TipoAtributo.CONTROLM_OWNER, owner, null,
						Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				Atributo atributoDescricao = new Atributo(TipoAtributo.DESCRICAO, descricao, null,
						Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				Atributo atributoDocMem = new Atributo(TipoAtributo.CONTROLM_DOC_MEM, docMem, null,
						Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				Atributo atributoDocLib = new Atributo(TipoAtributo.CONTROLM_DOCLIB, docLib, null,
						Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				Atributo atributoOverlib = new Atributo(TipoAtributo.CONTROLM_OVERLIB, overLib, null,
						Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());

				if (m_input.matches()) {
					String inputs = m_input.group("inputs");
					m_input_item = P_INPUT_ITEM.matcher(inputs);

					while (m_input_item.find()) {
						String elementoEsquerda = m_input_item.group("elementoEsquerda").trim();
						String elementoDireita = m_input_item.group("elementoDireita").trim();

						Artefato artefatoEsquerda = new Artefato();
						artefatoEsquerda.setNome(elementoEsquerda);
						artefatoEsquerda.setTipoArtefato(TipoArtefato.JCL);
						artefatoEsquerda.setNomeInterno(elementoEsquerda);
						artefatoEsquerda.setPosicao(posicao);
						artefatoEsquerda.setMalhaControlm(true);
						artefatoEsquerda.setAmbiente(TipoAmbiente.DESCONHECIDO);
						artefatoEsquerda.setSistema("DESCONHECIDO");

						Atributo atributoInput = new Atributo();
						atributoInput.setTipoAtributo(TipoAtributo.CONTROLM_MALHA_INPUT);
						atributoInput.setValor("true");
						atributoInput.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());

						artefatoEsquerda.adicionarAtributo(atributoInput);

						if (memName != null) {
							artefatoEsquerda.adicionarAtributo(atributoMemName);
						}
						if (groupName != null) {
							artefatoEsquerda.adicionarAtributo(atributoGroupName);
						}
						if (owner != null) {
							artefatoEsquerda.adicionarAtributo(atributoOwner);
						}
						if (descricao != null) {
							artefatoEsquerda.adicionarAtributo(atributoDescricao);
						}
						if (docMem != null) {
							artefatoEsquerda.adicionarAtributo(atributoDocMem);
						}
						if (docLib != null) {
							artefatoEsquerda.adicionarAtributo(atributoDocLib);
						}
						if (overLib != null) {
							artefatoEsquerda.adicionarAtributo(atributoOverlib);
						}

						Artefato artefatoDireita = new Artefato();
						artefatoDireita.setNome(elementoDireita);
						artefatoDireita.setTipoArtefato(TipoArtefato.JCL);
						artefatoDireita.setNomeInterno(elementoDireita);
						artefatoDireita.setMalhaControlm(true);
						artefatoDireita.setAmbiente(TipoAmbiente.DESCONHECIDO);
						artefatoDireita.setSistema("DESCONHECIDO");
						// artefatoDireita.setAmbiente(TipoAmbiente.DESCONHECIDO);

						artefatoEsquerda.setCaminhoArquivo(arquivo);
						artefatoEsquerda.setMalhaControlm(true);
						artefatoEsquerda.adicionarArtefatosRelacionados(artefatoDireita);

						posicao++;

						this.listaArtefatos.add(artefatoEsquerda);
					}
				}

				if (m_output.matches()) {
					String outputs = m_output.group("outputs");
					m_output_item = P_OUTPUT_ITEM.matcher(outputs);

					while (m_output_item.find()) {
						String elementoEsquerda = m_output_item.group("elementoEsquerda").trim();
						String elementoDireita = m_output_item.group("elementoDireita").trim();
						String sinal = m_output_item.group("sinal");

						Artefato artefatoEsquerda = new Artefato();
						artefatoEsquerda.setNome(elementoEsquerda);
						artefatoEsquerda.setTipoArtefato(TipoArtefato.JCL);
						artefatoEsquerda.setNomeInterno(elementoEsquerda);
						artefatoEsquerda.setPosicao(posicao);
						artefatoEsquerda.setMalhaControlm(true);
						artefatoEsquerda.setAmbiente(TipoAmbiente.DESCONHECIDO);
						artefatoEsquerda.setSistema("DESCONHECIDO");

						Atributo atributoOutput = new Atributo();
						atributoOutput.setTipoAtributo(TipoAtributo.CONTROLM_MALHA_OUTPUT);
						atributoOutput.setValor("true");
						atributoOutput.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());

						artefatoEsquerda.adicionarAtributo(atributoOutput);

						Atributo atributoOutputSinal = new Atributo();
						atributoOutputSinal.setTipoAtributo(TipoAtributo.CONTROLM_MALHA_OUTPUT_SINAL);
						atributoOutputSinal.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());

						if ("-".equals(sinal)) {
							atributoOutputSinal.setValor("NEGATIVO");
						} else if ("+".equals(sinal)) {
							atributoOutputSinal.setValor("POSITIVO");
						} else {
							System.out.println("Não pode ocorrer controlM output sem sinal");
						}

						artefatoEsquerda.adicionarAtributo(atributoOutputSinal);

						if (memName != null) {
							artefatoEsquerda.adicionarAtributo(atributoMemName);
						}
						if (groupName != null) {
							artefatoEsquerda.adicionarAtributo(atributoGroupName);
						}
						if (owner != null) {
							artefatoEsquerda.adicionarAtributo(atributoOwner);
						}
						if (descricao != null) {
							artefatoEsquerda.adicionarAtributo(atributoDescricao);
						}
						if (docMem != null) {
							artefatoEsquerda.adicionarAtributo(atributoDocMem);
						}
						if (docLib != null) {
							artefatoEsquerda.adicionarAtributo(atributoDocLib);
						}
						if (overLib != null) {
							artefatoEsquerda.adicionarAtributo(atributoOverlib);
						}

						Artefato artefatoDireita = new Artefato();
						artefatoDireita.setNome(elementoDireita);
						artefatoDireita.setTipoArtefato(TipoArtefato.JCL);
						artefatoDireita.setNomeInterno(elementoDireita);
						artefatoDireita.setMalhaControlm(true);
						artefatoDireita.setAmbiente(TipoAmbiente.DESCONHECIDO);
						artefatoDireita.setSistema("DESCONHECIDO");

						artefatoEsquerda.setCaminhoArquivo(arquivo);
						artefatoEsquerda.setMalhaControlm(true);
						artefatoEsquerda.adicionarArtefatosRelacionados(artefatoDireita);

						posicao++;
						this.listaArtefatos.add(artefatoEsquerda);
					}
				}

			}
		});
	}

	private void criarDicionario() {
		this.listaArtefatos.forEach((artefato) -> {

			Atributo atributoInput = artefato.buscaAtributo(TipoAtributo.CONTROLM_MALHA_INPUT);
			Atributo atributoOutput = artefato.buscaAtributo(TipoAtributo.CONTROLM_MALHA_OUTPUT);
			Atributo atributoOutputSinal = artefato.buscaAtributo(TipoAtributo.CONTROLM_MALHA_OUTPUT_SINAL);
			String memName = artefato.buscaAtributo(TipoAtributo.CONTROLM_MEMNAME).getValor();
			String nomeArtefatoFilho = artefato.getArtefatosRelacionados().get(0).getNome();
			String nomeArtefatoPai = artefato.getNome();

			if (atributoInput != null) {
				/// Artefato artefatoEntry = Manipular.retornarArtefato(nomeArtefatoFilho,
				/// TipoArtefato.JCL, 0);

				// if (artefatoEntry == null) {
				this.dicionario.put(nomeArtefatoFilho, memName);
				// } else {
				// this.dicionario.put(nomeArtefatoFilho, nomeArtefatoFilho);
				// }
			}

			if (atributoOutput != null && memName.equals("--------")) {
				this.dicionario.put(nomeArtefatoPai, nomeArtefatoPai);
			} else if (atributoOutput != null && atributoOutputSinal.getValor().equals("POSITIVO")) {
				/// Artefato artefatoEntry = Manipular.retornarArtefato(nomeArtefatoPai,
				/// TipoArtefato.JCL, 0);

				// if (artefatoEntry == null) {
				this.dicionario.put(nomeArtefatoPai, memName);
				// } else {
				// this.dicionario.put(nomeArtefatoPai, nomeArtefatoPai);
				// }

				// this.dicionario.put(relacionamento.getAscendente().getNome(),
				// relacionamento.getMemName());
			} else if (atributoOutput != null && atributoOutputSinal.getValor().equals("NEGATIVO")) {
				// Artefato artefatoEntry = Manipular.retornarArtefato(nomeArtefatoFilho,
				// TipoArtefato.JCL, 0);

				// if (artefatoEntry == null) {
				this.dicionario.put(nomeArtefatoFilho, memName);
				// } else {
				// this.dicionario.put(nomeArtefatoFilho, nomeArtefatoFilho);
				// }
			}

			if (!this.dicionario.containsKey(nomeArtefatoFilho)) {
				this.dicionario.put(nomeArtefatoFilho, nomeArtefatoFilho);
			}
			if (!this.dicionario.containsKey(nomeArtefatoPai)) {
				this.dicionario.put(nomeArtefatoPai, nomeArtefatoPai);
			}

		});
	}

	private void traduzir() {

		for (Artefato artefato : this.listaArtefatos) {
			String nomePai = artefato.getNome();
			String nomeFilho = artefato.getArtefatosRelacionados().get(0).getNome();

			artefato.setNome(dicionario.get(nomePai));
			artefato.getArtefatosRelacionados().get(0).setNome(dicionario.get(nomeFilho));

		}
	}

	private void removerDuplicidade() {
		List<Artefato> listaTemporaria = new ArrayList<>();

		for (Artefato artefato : this.listaArtefatos) {
			boolean jaIncluido = false;
			String nomePai = artefato.getNome();
			String nomeFilho = artefato.getArtefatosRelacionados().get(0).getNome();

			for (Artefato temp : listaTemporaria) {

				String tempNomePai = temp.getNome();
				String tempNomeFilho = temp.getArtefatosRelacionados().get(0).getNome();

				if (nomePai.equals(tempNomePai) && nomeFilho.equals(tempNomeFilho)) {
					jaIncluido = true;
				}

			}

			if (!jaIncluido) {
				listaTemporaria.add(artefato);
			}
		}
		this.listaArtefatos = listaTemporaria;
	}

}
