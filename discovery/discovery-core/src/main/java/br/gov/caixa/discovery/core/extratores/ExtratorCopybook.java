package br.gov.caixa.discovery.core.extratores;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.modelos.Atributo;
import br.gov.caixa.discovery.core.tipos.TipoAmbiente;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.tipos.TipoAtributo;
import br.gov.caixa.discovery.core.utils.ArtefatoHandler;
import br.gov.caixa.discovery.core.utils.Patterns;

public class ExtratorCopybook {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private Boolean foiProcessado = false;
	private List<Artefato> listaVariavel = null;
	private Boolean bInicioLinha = false;

	private Artefato artefato = null;
	private int deslocamento = 0;
	private int posicao = 0;
	private TipoAmbiente ambiente = null;
	private String sistema = null;

	private Matcher m_Var_Linha_Inicio_1 = null;
	private Matcher m_Var_Linha_Inicio_2 = null;
	private Matcher m_Var_Linha_Meio = null;
	private Matcher m_Var_Linha_Fim = null;
	private Matcher m_Var_Sintetica_1 = null;
	private Matcher m_Var_Sintetica_2 = null;
	private Matcher m_Var_Analitico_1 = null;
	private Matcher m_Var_Analitico_2 = null;
	private Matcher m_Var_Analitico_3 = null;
	private Matcher m_Var_Analitico_4 = null;
	private Matcher m_Var_Analitico_5 = null;
	private Matcher m_Var_Analitico_6 = null;
	private Matcher m_Var_Analitico_7 = null;
	private Matcher m_Var_Analitico_8 = null;
	private Matcher m_Var_Analitico_9 = null;
	private Matcher m_Var_Analitico_10 = null;
	private Matcher m_Var_Analitico_11 = null;
	private Matcher m_Var_Analitico_12 = null;

	public ExtratorCopybook() {
		super();
	}

	public ExtratorCopybook(Artefato artefato, int deslocamento) {
		super();
		this.artefato = artefato;
		this.deslocamento = deslocamento;
	}

	public Artefato executa() {
		try {
			if (this.ambiente == null) {
				this.ambiente = this.artefato.getAmbiente();
			}
			if (this.sistema == null) {
				this.sistema = this.artefato.getSistema();
			}

			// ATRIBUTI NOME DO ARTEFATO
			// String nomeArquivo =
			// Paths.get(this.artefato.getCaminhoArquivo()).getFileName().toString();
			String nomeArtefato = Paths.get(this.artefato.getCaminhoArquivo()).getFileName().toString();

			Matcher m_extrator_p_nome_artefato = Patterns.EXTRATOR_P_NOME_ARTEFATO
					.matcher(Paths.get(this.artefato.getCaminhoArquivo()).getFileName().toString());
			if (m_extrator_p_nome_artefato.matches()) {
				nomeArtefato = m_extrator_p_nome_artefato.group("parametro");
			}
			if (nomeArtefato.contains(".")) {
				nomeArtefato = nomeArtefato.split("\\.")[0].trim().toUpperCase();
			}

			artefato.setNome(ArtefatoHandler.tratarNomeArtefato(nomeArtefato));

			this.artefato = processarCodigoCompleto(this.artefato);
			this.artefato = relacionarVariavel(this.artefato, this.deslocamento, this.sistema, this.ambiente);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar converter " + this.artefato.getCaminhoArquivo(), e);
		}

		return artefato;
	}

	private Artefato processarCodigoCompleto(Artefato artefato) throws Exception {
		for (String textoCompleto : artefato.getCodigoCompleto()) {
			if ("".equals(textoCompleto.trim())) {
				continue;
			}
			String texto = textoCompleto;
			if (texto.length() >= 73) {
				texto = texto.substring(this.deslocamento, 72);
				this.artefato.adicionarRepresentacaoTextual(texto.substring(this.deslocamento, 72));
			} else {
				texto = texto.substring(this.deslocamento, texto.length());
				this.artefato.adicionarRepresentacaoTextual(texto.substring(this.deslocamento, texto.length()));
			}

			if (texto != null && texto.length() > (this.deslocamento + 6)
					&& "*".equals(texto.substring(this.deslocamento + 6, this.deslocamento + 7))) {
				artefato.adicionarComentario(texto.substring(this.deslocamento));
			} else {
				artefato.adicionarCodigoFonte(texto.substring(this.deslocamento));
			}
		}
		return artefato;
	}

	protected Artefato relacionarVariavel(Artefato artefato, int deslocamento, String sistema, TipoAmbiente ambiente)
			throws Exception {

		String textoUmaLinha = "";
		List<String> variaveisTexto = new ArrayList<String>();
		listaVariavel = new ArrayList<Artefato>();

		if (this.ambiente == null) {
			this.ambiente = ambiente;
		}
		if (this.sistema == null) {
			this.sistema = sistema;
		}

		for (String textoCompleto : artefato.getCodigoFonte()) {
			if (textoCompleto.length() < 6) {
				continue;
			}
			foiProcessado = false;

			String texto = textoCompleto.substring(6);
			m_Var_Linha_Inicio_1 = Patterns.COPYBOOK_P_VAR_LINHA_INICIO_1.matcher(texto);
			m_Var_Linha_Inicio_2 = Patterns.COPYBOOK_P_VAR_LINHA_INICIO_2.matcher(texto);
			m_Var_Linha_Meio = Patterns.COPYBOOK_P_VAR_LINHA_MEIO.matcher(texto);
			m_Var_Linha_Fim = Patterns.COPYBOOK_P_VAR_LINHA_FIM.matcher(texto);

			if (m_Var_Linha_Inicio_1.matches() && !foiProcessado) {
				textoUmaLinha = texto;

				variaveisTexto.add(textoUmaLinha); // adiciona o texto na lista

				textoUmaLinha = "";

				foiProcessado = true;
				bInicioLinha = false;
			}

			if (m_Var_Linha_Inicio_2.matches() && !foiProcessado) {
				textoUmaLinha = texto;
				foiProcessado = true;
				bInicioLinha = true;
			}

			if (m_Var_Linha_Meio.matches() && bInicioLinha && !foiProcessado) {
				textoUmaLinha = textoUmaLinha + " " + texto;
				// System.out.println("Meio");
				// System.out.println(textoUmaLinha);
				foiProcessado = true;
			}

			if (m_Var_Linha_Fim.matches() && !foiProcessado) {
				textoUmaLinha = textoUmaLinha + " " + texto;
				// System.out.println("Fim");
				// System.out.println(textoUmaLinha);

				variaveisTexto.add(textoUmaLinha); // adiciona o texto na lista

				textoUmaLinha = "";
				foiProcessado = true;
				bInicioLinha = false;
			}
		} // for

		for (String texto : variaveisTexto) {
			foiProcessado = false;

			texto = texto.replaceAll("[\\s]{2,}", " ");

			m_Var_Sintetica_1 = Patterns.COPYBOOK_P_VAR_SINTETICA_1.matcher(texto);
			m_Var_Sintetica_2 = Patterns.COPYBOOK_P_VAR_SINTETICA_2.matcher(texto);
			m_Var_Analitico_1 = Patterns.COPYBOOK_P_VAR_ANALITICA_1.matcher(texto);
			m_Var_Analitico_2 = Patterns.COPYBOOK_P_VAR_ANALITICA_2.matcher(texto);
			m_Var_Analitico_3 = Patterns.COPYBOOK_P_VAR_ANALITICA_3.matcher(texto);
			m_Var_Analitico_4 = Patterns.COPYBOOK_P_VAR_ANALITICA_4.matcher(texto);
			m_Var_Analitico_5 = Patterns.COPYBOOK_P_VAR_ANALITICA_5.matcher(texto);
			m_Var_Analitico_6 = Patterns.COPYBOOK_P_VAR_ANALITICA_6.matcher(texto);
			m_Var_Analitico_7 = Patterns.COPYBOOK_P_VAR_ANALITICA_7.matcher(texto);
			m_Var_Analitico_8 = Patterns.COPYBOOK_P_VAR_ANALITICA_8.matcher(texto);
			m_Var_Analitico_9 = Patterns.COPYBOOK_P_VAR_ANALITICA_9.matcher(texto);
			m_Var_Analitico_10 = Patterns.COPYBOOK_P_VAR_ANALITICA_10.matcher(texto);
			m_Var_Analitico_11 = Patterns.COPYBOOK_P_VAR_ANALITICA_11.matcher(texto);
			m_Var_Analitico_12 = Patterns.COPYBOOK_P_VAR_ANALITICA_12.matcher(texto);

			if (!foiProcessado)
				sintetico_1(texto);
			if (!foiProcessado)
				sintetico_2(texto); //
			if (!foiProcessado)
				analitico_7(texto);
			if (!foiProcessado)
				analitico_1(texto);
			if (!foiProcessado)
				analitico_2(texto);
			if (!foiProcessado)
				analitico_3(texto); // somente 88
			if (!foiProcessado)
				analitico_5(texto); // redefines com pic
			if (!foiProcessado)
				analitico_4(texto); // redefines sem pic
			if (!foiProcessado)
				analitico_6(texto); // var with value
			if (!foiProcessado)
				analitico_8(texto); //
			if (!foiProcessado)
				analitico_9(texto); //
			if (!foiProcessado)
				analitico_10(texto); //
			if (!foiProcessado)
				analitico_11(texto); //
			if (!foiProcessado)
				analitico_12(texto); //

		}

		// cobol.setVariaveis(listaVariavel);
		// List<Artefato> variaveis = criarHierarquia(listaVariavel);

		// for(CobolVariavel var : variaveis)
		// System.out.println(var.getNome());

		// artefato.setArtefatosRelacionados(variaveis);

		hierarquia(this.listaVariavel);

		artefato.adicionarArtefatosRelacionados(this.listaVariavel);

		return artefato;
	}

	/*
	 * #################################################### Criar lista variavels
	 * Hierarquia Alterar lógica para que as variaveis analiticas sejam incluidas na
	 * variavel hierarquia da lista variavels Hierarquia
	 */

	private void hierarquia(List<Artefato> listaEntrada) throws Exception {
		// List<Artefato> listaSaida = new ArrayList<>();

		for (int i = 0; i <= listaEntrada.size() - 1; i++) {

			Artefato artefatoI = listaEntrada.get(i);

			Atributo atributoI = artefatoI.buscaAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			int hierarquiaI = Integer.parseInt(atributoI.getValor());
			int guardarHierarquia = hierarquiaI;
			boolean temSubvariavel = false;
			for (int k = i + 1; k <= listaEntrada.size() - 1; k++) {
				Artefato artefatoK = listaEntrada.get(k);
				Atributo atributoK = artefatoK.buscaAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
				int hierarquiaK = Integer.parseInt(atributoK.getValor());

				if (hierarquiaK > hierarquiaI && !temSubvariavel) {
					temSubvariavel = true;
					guardarHierarquia = hierarquiaK;
					artefatoI.adicionarArtefatosRelacionados(artefatoK);
				} else if (guardarHierarquia == hierarquiaK && temSubvariavel) {
					artefatoI.adicionarArtefatosRelacionados(artefatoK);
				} else if (!temSubvariavel || hierarquiaK <= hierarquiaI) {
					break;
				}
			}
		}
	}

	private void sintetico_1(String texto) throws Exception {
		if (m_Var_Sintetica_1.matches() && !foiProcessado) {
			posicao++;
			// System.out.println("m_Var_Sintetico_1");

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Sintetica_1.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Sintetica_1.group("hierarquia");
			// variavel.setTemSubvariavel(true);

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo2.setValor(texto.trim());

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);

			listaVariavel.add(variavel);
			foiProcessado = true;
		}
	}

	private void sintetico_2(String texto) throws Exception {
		if (m_Var_Sintetica_2.matches() && !foiProcessado) {
			posicao++;
			// System.out.println("m_Var_Sintetico_1");

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Sintetica_2.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Sintetica_2.group("hierarquia");
			// variavel.setTemSubvariavel(true);

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo2.setValor(texto.trim());

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);

			listaVariavel.add(variavel);
			foiProcessado = true;
		}
	}

	private void analitico_9(String texto) throws Exception {
		if (m_Var_Analitico_9.matches() && !foiProcessado) {
			posicao++;

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_9.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Analitico_9.group("hierarquia");
			String tipo = m_Var_Analitico_9.group("tipo");

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.TIPO);
			atributo2.setValor(tipo);

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo3.setValor(texto.trim());

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);
			variavel.adicionarAtributo(atributo3);

			// variavel.setDeclaracaoCompleta(texto);
			listaVariavel.add(variavel);
			foiProcessado = true;
		}
	}

	private void analitico_7(String texto) throws Exception {
		if (m_Var_Analitico_7.matches() && !foiProcessado) {
			posicao++;

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_7.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Analitico_7.group("hierarquia");
			String tipo = m_Var_Analitico_7.group("tipo");

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.TIPO);
			atributo2.setValor(tipo);

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo3.setValor(texto.trim());

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);
			variavel.adicionarAtributo(atributo3);

			// variavel.setDeclaracaoCompleta(texto);
			listaVariavel.add(variavel);
			foiProcessado = true;
		}
	}

	private void analitico_6(String texto) throws Exception {
		if (m_Var_Analitico_6.matches() && !foiProcessado) {
			posicao++;

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_6.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Analitico_6.group("hierarquia");
			String tipo = m_Var_Analitico_6.group("tipo");
			String valorPadrao = m_Var_Analitico_6.group("valorPadrao");

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.TIPO);
			atributo2.setValor(tipo);

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo3.setValor(texto.trim());

			Atributo atributo4 = new Atributo();
			atributo4.setTipoAtributo(TipoAtributo.VALOR_PADRAO);
			atributo4.setValor(valorPadrao);

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);
			variavel.adicionarAtributo(atributo3);
			variavel.adicionarAtributo(atributo4);

			listaVariavel.add(variavel);
			foiProcessado = true;
		}
	}

	private void analitico_11(String texto) throws Exception {
		if (m_Var_Analitico_11.matches() && !foiProcessado) {
			posicao++;

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_11.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Analitico_11.group("hierarquia");
			String tipo = m_Var_Analitico_11.group("tipo");

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.TIPO);
			atributo2.setValor(tipo);

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo3.setValor(texto.trim());

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);
			variavel.adicionarAtributo(atributo3);

			listaVariavel.add(variavel);
			foiProcessado = true;
		}
	}

	private void analitico_1(String texto) throws Exception {
		if (m_Var_Analitico_1.matches() && !foiProcessado) {
			posicao++;

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_1.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Analitico_1.group("hierarquia");
			String tipo = m_Var_Analitico_1.group("tipo");

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.TIPO);
			atributo2.setValor(tipo);

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo3.setValor(texto.trim());

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);
			variavel.adicionarAtributo(atributo3);

			listaVariavel.add(variavel);
			foiProcessado = true;
		}
	}

	private void analitico_2(String texto) throws Exception {
		if (m_Var_Analitico_2.matches() && !foiProcessado) {
			posicao++;

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_2.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Analitico_2.group("hierarquia");
			String valorPadrao = m_Var_Analitico_2.group("valorPadrao");

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.VALOR_PADRAO);
			atributo2.setValor(valorPadrao);

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo3.setValor(texto.trim());

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);
			variavel.adicionarAtributo(atributo3);

			// variavel.setHierarquia(m_Var_Analitico_2.group("hierarquia"));
			// variavel.setTipoVariavel("OCCURS");
			// variavel.setValorPadrao(m_Var_Analitico_2.group("valorPadrao"));
			// variavel.setDeclaracaoCompleta(texto);

			listaVariavel.add(variavel);

			foiProcessado = true;
		}
	}

	private void analitico_10(String texto) throws Exception {
		if (m_Var_Analitico_10.matches() && !foiProcessado) {
			posicao++;

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_10.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Analitico_10.group("hierarquia");
			// String valorPadrao = m_Var_Analitico_10.group("valorPadrao");

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo3.setValor(texto.trim());

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo3);

			listaVariavel.add(variavel);

			foiProcessado = true;
		}
	}

	private void analitico_12(String texto) throws Exception {
		if (m_Var_Analitico_12.matches() && !foiProcessado) {
			posicao++;

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_12.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Analitico_12.group("hierarquia");
			// String valorPadrao = m_Var_Analitico_12.group("valorPadrao");

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo3.setValor(texto.trim());

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo3);

			listaVariavel.add(variavel);

			foiProcessado = true;
		}
	}

	private void analitico_3(String texto) throws Exception {

		if (m_Var_Analitico_3.matches() && !foiProcessado) {
			posicao++;

			String valorPadrao = "";

			// System.out.println("m_Var_Analitico_3");

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_3.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Analitico_3.group("hierarquia");

			valorPadrao = " " + m_Var_Analitico_3.group("valorPadrao");

			// m_valor_padrao_thru = P_VALOR_PADRAO_THRU.matcher(valorPadrao);

			String line = valorPadrao;
			line = line.replace("\'", "\"");
			String[] tokens = line.split(",|\\s(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			List<String> splitValorPadrao = new ArrayList<String>();
			List<String> listaValorPadrao = new ArrayList<String>();

			for (String entry : tokens) {
				if (!entry.trim().equals("")) {
					splitValorPadrao.add(entry);
				}
			}

			for (Integer i = 0; i < splitValorPadrao.size(); i++) {
				if (((i + 1) < (splitValorPadrao.size() - 1))
						&& splitValorPadrao.get(i + 1).trim().toUpperCase().equals("THRU")) {
					String strValorInicial = splitValorPadrao.get(i);
					String strValorFinal = splitValorPadrao.get(i + 2);

					strValorInicial = strValorInicial.replaceAll("[,\\s'\"]{1,}", "");
					strValorFinal = strValorFinal.replaceAll("[,\\s'\"]{1,}", "");

					try {
						Long valorInicial = Long.parseLong(strValorInicial);
						Long valorFinal = Long.parseLong(strValorFinal);

						for (Long index = valorInicial; index <= valorFinal; index++) {
							listaValorPadrao.add("" + index);
						}
					} catch (NumberFormatException e) {
						listaValorPadrao.add("" + strValorInicial);
						listaValorPadrao.add("" + strValorFinal);
					}
					i += 2;
				} else {
					listaValorPadrao.add(splitValorPadrao.get(i));
				}
			}

			valorPadrao = null;
			for (String entry : listaValorPadrao) {

				if (valorPadrao != null) {
					valorPadrao = valorPadrao + "," + entry;
				} else {
					valorPadrao = entry;
				}

			}
			// variavel.setValorPadrao(valorPadrao);

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.VALOR_PADRAO);
			atributo2.setValor(valorPadrao);

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo3.setValor(texto.trim());

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);
			variavel.adicionarAtributo(atributo3);

			listaVariavel.add(variavel);

			foiProcessado = true;
		}
	}

	private void analitico_4(String texto) throws Exception {
		if (m_Var_Analitico_4.matches() && !foiProcessado) {
			posicao++;

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_4.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Analitico_4.group("hierarquia");
			String redefines = m_Var_Analitico_4.group("nomeVariavelRedefinida");

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.COBOL_REDEFINE);
			atributo2.setValor(redefines);

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo3.setValor(texto.trim());

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);
			variavel.adicionarAtributo(atributo3);

			// variavel.setHierarquia(m_Var_Analitico_4.group("hierarquia"));
			// variavel.setRedefines(true);
			// variavel.setNomeRedefinido(m_Var_Analitico_4.group("nomeVariavelRedefinida"));
			// variavel.setDeclaracaoCompleta(texto);

			listaVariavel.add(variavel);
			foiProcessado = true;
		}
	}

	private void analitico_5(String texto) throws Exception {
		if (m_Var_Analitico_5.matches() && !foiProcessado) {
			posicao++;

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_5.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			String hierarquia = m_Var_Analitico_5.group("hierarquia");
			String redefines = m_Var_Analitico_5.group("nomeVariavelRedefinida");
			String tipo = m_Var_Analitico_5.group("tipo");

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.COBOL_REDEFINE);
			atributo2.setValor(redefines);

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo3.setValor(texto.trim());

			Atributo atributo4 = new Atributo();
			atributo4.setTipoAtributo(TipoAtributo.TIPO);
			atributo4.setValor(tipo);

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);
			variavel.adicionarAtributo(atributo3);
			variavel.adicionarAtributo(atributo4);

			// variavel.setHierarquia(m_Var_Analitico_5.group("hierarquia"));
			// variavel.setRedefines(true);
			// variavel.setTipoVariavel(m_Var_Analitico_5.group("tipo"));
			// variavel.setNomeRedefinido(m_Var_Analitico_5.group("nomeVariavelRedefinida"));
			// variavel.setDeclaracaoCompleta(texto);

			listaVariavel.add(variavel);

			foiProcessado = true;
		}
	}

	private void analitico_8(String texto) throws Exception {

		if (m_Var_Analitico_8.matches() && !foiProcessado) {
			posicao++;
			String valorPadrao = "";

			// System.out.println("m_Var_Analitico_3");

			Artefato variavel = new Artefato();
			variavel.setNome(m_Var_Analitico_8.group("nomeVariavel"));
			variavel.setTipoArtefato(TipoArtefato.COPYBOOK_VARIAVEL);
			variavel.setAmbiente(this.ambiente);
			variavel.setSistema(this.sistema);
			variavel.setPosicao(posicao);

			// variavel.setHierarquia(m_Var_Analitico_8.group("hierarquia"));
			// variavel.setDeclaracaoCompleta(texto);

			valorPadrao = " " + m_Var_Analitico_8.group("valorPadrao");

			// m_valor_padrao_thru = P_VALOR_PADRAO_THRU.matcher(valorPadrao);

			String line = valorPadrao;
			line = line.replace("\"", "\'");
			String[] tokens = line.split(",|\\s(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			List<String> splitValorPadrao = new ArrayList<String>();
			List<String> listaValorPadrao = new ArrayList<String>();

			for (String entry : tokens) {
				if (!entry.trim().equals("")) {
					splitValorPadrao.add(entry);
				}
			}

			for (Integer i = 0; i < splitValorPadrao.size(); i++) {
				if (((i + 1) < (splitValorPadrao.size() - 1))
						&& splitValorPadrao.get(i + 1).trim().toUpperCase().equals("THRU")) {
					String strValorInicial = splitValorPadrao.get(i);
					String strValorFinal = splitValorPadrao.get(i + 2);

					strValorInicial = strValorInicial.replaceAll("[,\\s'\"]{1,}", "");
					strValorFinal = strValorFinal.replaceAll("[,\\s'\"]{1,}", "");

					Long valorInicial = Long.parseLong(strValorInicial);
					Long valorFinal = Long.parseLong(strValorFinal);

					for (Long index = valorInicial; index <= valorFinal; index++) {
						listaValorPadrao.add("" + index);
					}
					i += 2;
				} else {
					listaValorPadrao.add(splitValorPadrao.get(i));
				}
			}

			valorPadrao = null;
			for (String entry : listaValorPadrao) {

				if (valorPadrao != null) {
					valorPadrao = valorPadrao + "," + entry;
				} else {
					valorPadrao = entry;
				}

			}
			// variavel.setValorPadrao(valorPadrao);

			String hierarquia = m_Var_Analitico_8.group("hierarquia");

			Atributo atributo1 = new Atributo();
			atributo1.setTipoAtributo(TipoAtributo.COBOL_HIERARQUIA_VARIAVEL);
			atributo1.setValor(hierarquia);

			Atributo atributo2 = new Atributo();
			atributo2.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			atributo2.setValor(texto.trim());

			Atributo atributo3 = new Atributo();
			atributo3.setTipoAtributo(TipoAtributo.VALOR_PADRAO);
			atributo3.setValor(valorPadrao);

			variavel.adicionarAtributo(atributo1);
			variavel.adicionarAtributo(atributo2);
			variavel.adicionarAtributo(atributo3);

			listaVariavel.add(variavel);

			foiProcessado = true;
		}
	}
}
