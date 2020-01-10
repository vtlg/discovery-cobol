package br.gov.caixa.discovery.core.extratores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.modelos.Atributo;
import br.gov.caixa.discovery.core.tipos.TipoAmbiente;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.tipos.TipoAtributo;
import br.gov.caixa.discovery.core.tipos.TipoRelacionamento;
import br.gov.caixa.discovery.core.utils.ArtefatoHandler;
import br.gov.caixa.discovery.core.utils.Configuracao;
import br.gov.caixa.discovery.core.utils.Patterns;
import br.gov.caixa.discovery.core.utils.PatternsSIAOI;
import br.gov.caixa.discovery.core.utils.UtilsHandler;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.SistemaPersistence;
import br.gov.caixa.discovery.ejb.tipos.Tabelas;

public class ExtratorProgramaCobol {

	private final static Logger LOGGER = Logger.getLogger(ExtratorProgramaCobol.class.getName());

	private Artefato artefato = null;
	int deslocamento = 0;
	boolean marcardor_objetivo = false;
	boolean marcardor_descricao = false;
	boolean foiProcessado = false;

	private List<Artefato> listaVariaveisPesquisa = new ArrayList<>();

	public ExtratorProgramaCobol() {
		super();
	}

	public ExtratorProgramaCobol(Artefato artefato, int deslocamento) {
		this.artefato = artefato;
		this.deslocamento = deslocamento;
	}

	public Artefato executa() {
		try {
			this.artefato = processarCodigoCompleto(this.artefato);
			this.artefato = separarCodigoEmParagrafos(this.artefato);
			this.artefato = roteiroVariavel(this.artefato);
			this.artefato = includeRoteiroVariavel(this.artefato);
			this.artefato = operacoesArquivo(this.artefato);
			this.artefato = declaracaoSql(this.artefato);
			this.artefato = tabelas(this.artefato);
 			//this.artefato = variaveis(this.artefato);
			this.artefato = variaveisPesquisa(this.artefato);
			this.artefato = programas(this.artefato);
			this.artefato = executeCics(this.artefato);
			this.artefato = substituirReferencia(this.artefato);
			this.artefato = excluirArtefato(this.artefato);
			this.artefato = tratarNomeArtefatos(this.artefato);
			this.artefato = atribuirSistemaTipo(this.artefato);
			this.artefato = atribuirTipoArtefato(this.artefato);
			this.artefato = atribuirSistema(this.artefato);
			this.artefato = atribuirAmbiente(this.artefato);
			this.artefato = objetivo(this.artefato);
			this.artefato = descricaoMaiframe(this.artefato);
			this.artefato = classificarRelacionamento(this.artefato);
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
				artefato.adicionarComentario(texto.substring(this.deslocamento).toUpperCase());
			} else {
				artefato.adicionarCodigoFonte(texto.substring(this.deslocamento).toUpperCase());
			}
		}
		return artefato;
	}

	private Artefato separarCodigoEmParagrafos(Artefato artefato) throws Exception {

		Matcher m_program_id_1 = null;
		Matcher m_program_id_2 = null;
		Matcher m_program_id_3 = null;
		Matcher m_identification_division = null;
		Matcher m_environment_division = null;
		Matcher m_data_division = null;
		Matcher m_procedure_division = null;
		Matcher m_paragrafo_exit = null;
		Matcher m_paragrafo = null;

		Artefato paragrafo = null;
		String textoCodigo = null;
		boolean isProcedureDivision = false;

		for (String texto : artefato.getCodigoFonte()) {
			if (texto.length() < 8) {
				texto = UtilsHandler.padRight(texto, 8);
			}

			textoCodigo = texto.substring(6);

			m_program_id_1 = Patterns.COBOL_P_PROGRAM_ID_1.matcher(textoCodigo);
			m_program_id_2 = Patterns.COBOL_P_PROGRAM_ID_2.matcher(textoCodigo);
			m_program_id_3 = Patterns.COBOL_P_PROGRAM_ID_3.matcher(textoCodigo);

			m_identification_division = Patterns.COBOL_P_IDENTIFICATION_DIVISION.matcher(textoCodigo);
			m_environment_division = Patterns.COBOL_P_ENVIRONMENT_DIVISION.matcher(textoCodigo);
			m_data_division = Patterns.COBOL_P_DATA_DIVISION.matcher(textoCodigo);
			m_procedure_division = Patterns.COBOL_P_PROCEDURE_DIVISION.matcher(textoCodigo);

			m_paragrafo_exit = Patterns.COBOL_P_PARAGRAFO_EXIT.matcher(textoCodigo);
			m_paragrafo = Patterns.COBOL_P_PARAGRAFO.matcher(textoCodigo);

			if (m_program_id_1.matches()) {
				artefato.setNomeInterno(m_program_id_1.group("nomePrograma").toUpperCase());
			} else if (m_program_id_2.matches()) {
				artefato.setNomeInterno(m_program_id_2.group("nomePrograma").toUpperCase());
			} else if (m_program_id_3.matches()) {
				artefato.setNomeInterno(m_program_id_3.group("nomePrograma").toUpperCase());
			}

			if (m_identification_division.matches()) {

				if (paragrafo != null) {
					artefato.adicionarArtefatosRelacionados(paragrafo);
				}

				paragrafo = new Artefato();
				paragrafo.setNome("IDENTIFICATION DIVISION");
				paragrafo.setSistema(artefato.getSistema());
				paragrafo.setAmbiente(artefato.getAmbiente());
				paragrafo.setTipoArtefato(TipoArtefato.PROGRAMA_COBOL_PARAGRAFO);

			}

			if (m_environment_division.matches()) {
				if (paragrafo != null) {
					artefato.adicionarArtefatosRelacionados(paragrafo);
				}

				paragrafo = new Artefato();
				paragrafo.setNome("ENVIRONMENT DIVISION");
				paragrafo.setSistema(artefato.getSistema());
				paragrafo.setAmbiente(artefato.getAmbiente());
				paragrafo.setTipoArtefato(TipoArtefato.PROGRAMA_COBOL_PARAGRAFO);
			}

			if (m_data_division.matches()) {
				if (paragrafo != null) {
					artefato.adicionarArtefatosRelacionados(paragrafo);
				}

				paragrafo = new Artefato();
				paragrafo.setNome("DATA DIVISION");
				paragrafo.setSistema(artefato.getSistema());
				paragrafo.setAmbiente(artefato.getAmbiente());
				paragrafo.setTipoArtefato(TipoArtefato.PROGRAMA_COBOL_PARAGRAFO);
			}

			if (isProcedureDivision && m_paragrafo_exit.matches()) {
				if (paragrafo != null) {
					artefato.adicionarArtefatosRelacionados(paragrafo);
				}

				String nomeParagrafo = m_paragrafo_exit.group("nomeParagrafo");

				paragrafo = new Artefato();
				paragrafo.setNome(UtilsHandler.removerPontoFinal(nomeParagrafo.trim()));
				paragrafo.setSistema(artefato.getSistema());
				paragrafo.setAmbiente(artefato.getAmbiente());
				paragrafo.setTipoArtefato(TipoArtefato.PROGRAMA_COBOL_PARAGRAFO);

			} else if (isProcedureDivision && m_paragrafo.matches()) {
				if (paragrafo != null) {
					artefato.adicionarArtefatosRelacionados(paragrafo);
				}

				paragrafo = new Artefato();
				paragrafo.setNome(UtilsHandler.removerPontoFinal(textoCodigo.trim()));
				paragrafo.setSistema(artefato.getSistema());
				paragrafo.setAmbiente(artefato.getAmbiente());
				paragrafo.setTipoArtefato(TipoArtefato.PROGRAMA_COBOL_PARAGRAFO);
			}

			if (m_procedure_division.matches()) {
				if (paragrafo != null) {
					artefato.adicionarArtefatosRelacionados(paragrafo);
				}

				isProcedureDivision = true;

				paragrafo = new Artefato();
				paragrafo.setNome("PROCEDURE DIVISION");
				paragrafo.setSistema(artefato.getSistema());
				paragrafo.setAmbiente(artefato.getAmbiente());
				paragrafo.setTipoArtefato(TipoArtefato.PROGRAMA_COBOL_PARAGRAFO);
			}

			if (paragrafo != null) {
				paragrafo.adicionarCodigoFonte(texto);
				paragrafo.setSistema(artefato.getSistema());
				paragrafo.setAmbiente(artefato.getAmbiente());
				paragrafo.setTipoArtefato(TipoArtefato.PROGRAMA_COBOL_PARAGRAFO);
			}
		}

		if (paragrafo != null) {
			artefato.adicionarArtefatosRelacionados(paragrafo);
		}

		return artefato;
	}

	private Artefato roteiroVariavel(Artefato artefato) throws Exception {
		Artefato dataDivision = null;
		List<String> roteiroVariavel = null;

		// DATA DIVISION
		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			if (entry.getNome().equals("DATA DIVISION")) {
				dataDivision = entry;

				roteiroVariavel = _navegaRoteiroVariavel(dataDivision.getCodigoFonte());
				artefato.setRoteiroVariavel(roteiroVariavel);
				break;
			}
		}

		return artefato;
	}

	private Artefato includeRoteiroVariavel(Artefato artefato) throws Exception {

		Matcher m_tabela = null;
		String dataDivision = "";

		// DATA DIVISION
		for (String entry : this.artefato.getRoteiroVariavel()) {
			if (entry.trim().equals("") || entry.length() < 8) {
				continue;
			}
			String textoCodigo = entry.substring(6);
			dataDivision = dataDivision + " " + textoCodigo;
		}

		dataDivision = dataDivision.replaceAll("[\\s]{2,}", " ");
		m_tabela = Patterns.COBOL_P_TABELA.matcher(dataDivision);

		while (m_tabela.matches()) {
			if (m_tabela.group("nomeTabela") != null) {
				String nomeTabela = m_tabela.group("nomeTabela");

				dataDivision = dataDivision
						.replace(dataDivision.substring(m_tabela.start(1), m_tabela.end("nomeTabela")), " ");

				if (!"oraca".toLowerCase().equalsIgnoreCase(nomeTabela)
						&& !"SQLCA".toLowerCase().equalsIgnoreCase(nomeTabela)) {
					artefato.adicionarRoteiroVariavel(
							"         COPY \"" + nomeTabela.replaceAll("[\"\']{1,}", "") + "\".");
				}
				m_tabela.reset(dataDivision);
			}
		}

		return artefato;
	}

	private Artefato operacoesArquivo(Artefato artefato) throws Exception {
		Artefato paragrafoEnviromentDivision = null;
		Artefato paragrafoDataDivision = null;

		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			if ("ENVIRONMENT DIVISION".equals(entry.getNome())) {
				paragrafoEnviromentDivision = entry;
				break;
			}
		}

		if (paragrafoEnviromentDivision != null) {
			paragrafoEnviromentDivision = _tratarEnviromentDivision(paragrafoEnviromentDivision);
		}

		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			if ("DATA DIVISION".equals(entry.getNome())) {
				paragrafoDataDivision = entry;
				break;
			}
		}

		if (paragrafoDataDivision != null) {
			paragrafoDataDivision = _tratarDataDivision(paragrafoDataDivision);
		}

		artefato = _tratarVerbosFile(artefato);

		return artefato;
	}

	private Artefato declaracaoSql(Artefato artefato) throws Exception {
		Matcher m_exec_sql_1 = null;
		Matcher m_exec_sql_2 = null;
		Matcher m_end_sql_1 = null;

		boolean marcador = false;
		StringBuilder sb = new StringBuilder();

		int countDeclaracaoSql = 1;
		for (String texto : artefato.getCodigoFonte()) {
			if (texto.trim().equals("") || texto.length() < 8) {
				continue;
			}

			String textoCodigo = texto.substring(6);

			m_exec_sql_1 = Patterns.COBOL_P_EXEC_SQL_1.matcher(textoCodigo);
			m_exec_sql_2 = Patterns.COBOL_P_EXEC_SQL_2.matcher(textoCodigo);
			m_end_sql_1 = Patterns.COBOL_P_END_SQL_1.matcher(textoCodigo);

			if ((m_exec_sql_1.matches() || m_exec_sql_2.matches()) & !marcador) {
				sb = new StringBuilder();
				marcador = true;
			}

			if (marcador) {
				sb.append(textoCodigo);
			}

			if (m_end_sql_1.matches() && marcador) {
				Artefato artefatoQuery = new Artefato();
				artefatoQuery.setNome("DECLARACAO-SQL-" + countDeclaracaoSql);
				artefatoQuery.setNomeInterno("DECLARACAO-SQL-" + countDeclaracaoSql);
				artefatoQuery.setAmbiente(artefato.getAmbiente());
				artefatoQuery.setSistema(artefato.getSistema());
				artefatoQuery.setTipoArtefato(TipoArtefato.DECLARACAO_SQL);

				Atributo atributoDeclaracaoCompleta = new Atributo();
				atributoDeclaracaoCompleta.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
				atributoDeclaracaoCompleta.setValor(sb.toString().replaceAll("[\\s]{2,}", " ").trim());
				// atributoDeclaracaoCompleta.setTipo("STRING");
				atributoDeclaracaoCompleta.setTipo("ARTEFATO");

				artefatoQuery.adicionarAtributo(atributoDeclaracaoCompleta);

				artefato.adicionarArtefatosRelacionados(artefatoQuery);
				marcador = false;
				countDeclaracaoSql++;
			}
		}

		return artefato;
	}

	private Artefato tabelas(Artefato artefato) throws Exception {

		Matcher m_Join = null;
		Matcher m_Insert_1 = null;
		Matcher m_From_where_1 = null;
		Matcher m_From_inner_1 = null;
		Matcher m_From_join_1 = null;
		Matcher m_From_left_1 = null;
		Matcher m_From_right_1 = null;

		Matcher m_From_1 = null;
		Matcher m_Delete_From_1 = null;
		Matcher m_Update_1 = null;
		Matcher m_Update_1_not = null;
		Matcher m_Delete_1_not = null;

		List<Artefato> tempLista = new ArrayList<>();
		tempLista.addAll(artefato.getArtefatosRelacionados());

		for (Artefato artefatoDeclaracaoSql : tempLista) {
			if (!TipoArtefato.DECLARACAO_SQL.equals(artefatoDeclaracaoSql.getTipoArtefato())) {
				continue;
			}

			Atributo atributoDeclaracaoCompleta = artefatoDeclaracaoSql.buscaAtributo(TipoAtributo.DECLARACAO_COMPLETA);

			String texto = atributoDeclaracaoCompleta.getValor();

			m_Join = Patterns.COBOL_P_JOIN.matcher(texto);
			m_From_1 = Patterns.COBOL_P_FROM_1.matcher(texto);
			m_From_where_1 = Patterns.COBOL_P_FROM_WHERE_1.matcher(texto);
			m_From_inner_1 = Patterns.COBOL_P_FROM_INNER_1.matcher(texto);
			m_From_join_1 = Patterns.COBOL_P_FROM_JOIN_1.matcher(texto);
			m_From_left_1 = Patterns.COBOL_P_FROM_LEFT_1.matcher(texto);
			m_From_right_1 = Patterns.COBOL_P_FROM_RIGHT_1.matcher(texto);

			m_Delete_From_1 = Patterns.COBOL_P_DELETE_FROM_1.matcher(texto);
			m_Update_1 = Patterns.COBOL_P_UPDATE_1.matcher(texto);
			m_Update_1_not = Patterns.COBOL_P_UPDATE_1_NOT.matcher(texto);
			m_Delete_1_not = Patterns.COBOL_P_DELETE_1_NOT.matcher(texto);
			m_Insert_1 = Patterns.COBOL_P_INSERT_1.matcher(texto);

			if (m_Update_1.matches() && !m_Update_1_not.matches()) {
				String nomeTabela = m_Update_1.group("tabela");
				nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1);

				if (!"of".toUpperCase().equals(nomeTabela.toUpperCase())) {
					if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
							nomeTabela)) {
						// String sistema = ArtefatoHandler.identificarSistema(nomeTabela,
						// TipoArtefato.TABELA);
						Artefato tabela = new Artefato();
						tabela.setNome(nomeTabela);
						tabela.setNomeInterno(nomeTabela);
						// tabela.setAmbiente(Configuracao.AMBIENTE);
						// tabela.setSistema(sistema);
						tabela.setTipoArtefato(TipoArtefato.TABELA);
						artefato.adicionarArtefatosRelacionados(tabela);
					}
				}
			}

			if (m_Insert_1.matches()) {
				String nomeTabela = m_Insert_1.group("tabela");
				nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1);

				if (!"of".toUpperCase().equals(nomeTabela.toUpperCase())) {
					if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
							nomeTabela)) {
						// String sistema = ArtefatoHandler.identificarSistema(nomeTabela,
						// TipoArtefato.TABELA);

						Artefato tabela = new Artefato();
						tabela.setNome(nomeTabela);
						tabela.setNomeInterno(nomeTabela);
						// tabela.setAmbiente(Configuracao.AMBIENTE);
						// tabela.setSistema(sistema);
						tabela.setTipoArtefato(TipoArtefato.TABELA);
						artefato.adicionarArtefatosRelacionados(tabela);
					}
				}
			}

			if (m_Delete_From_1.matches() && !m_Delete_1_not.matches()) {
				String nomeTabela = m_Delete_From_1.group("tabela").trim();

				if (nomeTabela.contains(".")) {
					nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1).trim();
				}

				if (nomeTabela.contains(" ")) {
					nomeTabela = nomeTabela.substring(0, nomeTabela.indexOf(" "));
				}

				if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
						nomeTabela)) {
					// String sistema = ArtefatoHandler.identificarSistema(nomeTabela,
					// TipoArtefato.TABELA);

					Artefato tabela = new Artefato();
					tabela.setNome(nomeTabela);
					tabela.setNomeInterno(nomeTabela);
					// tabela.setAmbiente(Configuracao.AMBIENTE);
					// tabela.setSistema(sistema);
					tabela.setTipoArtefato(TipoArtefato.TABELA);
					tabela.adicionarArtefatosRelacionados(tabela);
				}
			} else if (m_From_inner_1.matches()) {
				String[] arrNomeTabela = m_From_inner_1.group("tabela").split(",");

				for (String nome : arrNomeTabela) {
					String nomeTabela = nome.trim();

					if (nomeTabela.contains(".")) {
						nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1).trim();
					}

					if (nomeTabela.contains(" ")) {
						nomeTabela = nomeTabela.substring(0, nomeTabela.indexOf(" "));
					}

					if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
							nomeTabela)) {
						// String sistema = ArtefatoHandler.identificarSistema(nomeTabela,
						// TipoArtefato.TABELA);

						Artefato tabela = new Artefato();
						tabela.setNome(nomeTabela);
						tabela.setNomeInterno(nomeTabela);
						// tabela.setAmbiente(Configuracao.AMBIENTE);
						// tabela.setSistema(sistema);
						tabela.setTipoArtefato(TipoArtefato.TABELA);
						artefato.adicionarArtefatosRelacionados(tabela);
					}
				}
			} else if (m_From_join_1.matches()) {
				String[] arrNomeTabela = m_From_join_1.group("tabela").split(",");

				for (String nome : arrNomeTabela) {
					String nomeTabela = nome.trim();

					if (nomeTabela.contains(".")) {
						nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1).trim();
					}

					if (nomeTabela.contains(" ")) {
						nomeTabela = nomeTabela.substring(0, nomeTabela.indexOf(" "));
					}

					if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
							nomeTabela)) {
						// String sistema = ArtefatoHandler.identificarSistema(nomeTabela,
						// TipoArtefato.TABELA);

						Artefato tabela = new Artefato();
						tabela.setNome(nomeTabela);
						tabela.setNomeInterno(nomeTabela);
						// tabela.setAmbiente(Configuracao.AMBIENTE);
						// tabela.setSistema(sistema);
						tabela.setTipoArtefato(TipoArtefato.TABELA);
						artefato.adicionarArtefatosRelacionados(tabela);
					}
				}
			} else if (m_From_left_1.matches()) {
				String[] arrNomeTabela = m_From_left_1.group("tabela").split(",");

				for (String nome : arrNomeTabela) {
					String nomeTabela = nome.trim();

					if (nomeTabela.contains(".")) {
						nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1).trim();
					}

					if (nomeTabela.contains(" ")) {
						nomeTabela = nomeTabela.substring(0, nomeTabela.indexOf(" "));
					}

					if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
							nomeTabela)) {
						// String sistema = ArtefatoHandler.identificarSistema(nomeTabela,
						// TipoArtefato.TABELA);

						Artefato tabela = new Artefato();
						tabela.setNome(nomeTabela);
						tabela.setNomeInterno(nomeTabela);
						// tabela.setAmbiente(Configuracao.AMBIENTE);
						// tabela.setSistema(sistema);
						tabela.setTipoArtefato(TipoArtefato.TABELA);
						artefato.adicionarArtefatosRelacionados(tabela);
					}
				}
			} else if (m_From_right_1.matches()) {
				String[] arrNomeTabela = m_From_right_1.group("tabela").split(",");

				for (String nome : arrNomeTabela) {
					String nomeTabela = nome.trim();

					if (nomeTabela.contains(".")) {
						nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1).trim();
					}

					if (nomeTabela.contains(" ")) {
						nomeTabela = nomeTabela.substring(0, nomeTabela.indexOf(" "));
					}

					if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
							nomeTabela)) {
						// String sistema = ArtefatoHandler.identificarSistema(nomeTabela,
						// TipoArtefato.TABELA);

						Artefato tabela = new Artefato();
						tabela.setNome(nomeTabela);
						tabela.setNomeInterno(nomeTabela);
						// tabela.setAmbiente(Configuracao.AMBIENTE);
						// tabela.setSistema(sistema);
						tabela.setTipoArtefato(TipoArtefato.TABELA);
						artefato.adicionarArtefatosRelacionados(tabela);
					}
				}
			} else if (m_From_where_1.matches()) {
				String[] arrNomeTabela = m_From_where_1.group("tabela").split(",");

				for (String nome : arrNomeTabela) {
					String nomeTabela = nome.trim();

					if (nomeTabela.contains(".")) {
						nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1).trim();
					}

					if (nomeTabela.contains(" ")) {
						nomeTabela = nomeTabela.substring(0, nomeTabela.indexOf(" "));
					}

					if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
							nomeTabela)) {
						// String sistema = ArtefatoHandler.identificarSistema(nomeTabela,
						// TipoArtefato.TABELA);

						Artefato tabela = new Artefato();
						tabela.setNome(nomeTabela);
						tabela.setNomeInterno(nomeTabela);
						// tabela.setAmbiente(Configuracao.AMBIENTE);
						// tabela.setSistema(sistema);
						tabela.setTipoArtefato(TipoArtefato.TABELA);
						artefato.adicionarArtefatosRelacionados(tabela);
					}
				}
			} else if (m_From_1.matches()) {
				String nomeTabela = m_From_1.group("tabela").trim();

				if (nomeTabela.contains(".")) {
					nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1).trim();
				}

				if (nomeTabela.contains(" ")) {
					nomeTabela = nomeTabela.substring(0, nomeTabela.indexOf(" "));
				}

				if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
						nomeTabela)) {
					// String sistema = ArtefatoHandler.identificarSistema(nomeTabela,
					// TipoArtefato.TABELA);

					Artefato tabela = new Artefato();
					tabela.setNome(nomeTabela);
					tabela.setNomeInterno(nomeTabela);
					// tabela.setAmbiente(Configuracao.AMBIENTE);
					// tabela.setSistema(sistema);
					tabela.setTipoArtefato(TipoArtefato.TABELA);
					artefato.adicionarArtefatosRelacionados(tabela);
				}
			}

			while (m_Join.find()) {
				String nomeTabela = m_Join.group("tabela");
				nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1);

				if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
						nomeTabela)) {
					// String sistema = ArtefatoHandler.identificarSistema(nomeTabela,
					// TipoArtefato.TABELA);

					Artefato tabela = new Artefato();
					tabela.setNome(nomeTabela);
					tabela.setNomeInterno(nomeTabela);
					// tabela.setAmbiente(Configuracao.AMBIENTE);
					// tabela.setSistema(sistema);
					tabela.setTipoArtefato(TipoArtefato.TABELA);
					artefato.adicionarArtefatosRelacionados(tabela);
				}
			}
		}

		return artefato;
	}

	private Artefato variaveis(Artefato artefato) throws Exception {

		Artefato dataDivision = ArtefatoHandler.recuperarArtefato(artefato.getArtefatosRelacionados(),
				TipoArtefato.PROGRAMA_COBOL_PARAGRAFO, "DATA DIVISION");

		ExtratorCopybook converter = new ExtratorCopybook(dataDivision, deslocamento);
		dataDivision = converter.relacionarVariavel(dataDivision, deslocamento, artefato.getSistema(),
				artefato.getAmbiente());
		artefato.adicionarArtefatosRelacionados(dataDivision.getArtefatosRelacionados());

		return artefato;
	}

	private Artefato variaveisPesquisa(Artefato artefato) throws Exception {

		Artefato dataDivision = ArtefatoHandler.recuperarArtefato(artefato.getArtefatosRelacionados(),
				TipoArtefato.PROGRAMA_COBOL_PARAGRAFO, "DATA DIVISION");

		dataDivision.setCodigoFonte(artefato.getRoteiroVariavel());

		ExtratorCopybook converter = new ExtratorCopybook(dataDivision, deslocamento);
		dataDivision = converter.relacionarVariavel(dataDivision, deslocamento, artefato.getSistema(),
				artefato.getAmbiente());
		listaVariaveisPesquisa.addAll(dataDivision.getArtefatosRelacionados());

		return artefato;
	}

	private Artefato programas(Artefato artefato) throws Exception {

		Matcher m_call = null;
		Matcher m_call_ponto = null;
		Matcher m_move = null;
		Matcher m_move_aspas = null;
		

		Artefato programaChamado = null;

		List<Artefato> listaArtefato = new ArrayList<>();

		for (Artefato paragrafo : artefato.getArtefatosRelacionados()) {
			for (String texto : paragrafo.getCodigoFonte()) {
				if (texto.trim().equals("") || texto.length() < 8) {
					continue;
				}

				String textoCodigo = texto.substring(6);

				m_call = PatternsSIAOI.COBOL_P_CALL_AOI.matcher(textoCodigo);				
				m_call_ponto = PatternsSIAOI.COBOL_P_CALL_AOI.matcher(textoCodigo);
				m_move = PatternsSIAOI.COBOL_P_MOVE.matcher(textoCodigo);
				m_move_aspas = PatternsSIAOI.COBOL_P_MOVE_ASPAS.matcher(textoCodigo);

				if (m_call_ponto.matches()) {
					String nomePrograma = m_call_ponto.group("nomePrograma");
					if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), null, nomePrograma)) {
						programaChamado = new Artefato();
						programaChamado.setNome(nomePrograma);
						programaChamado.setNomeInterno(nomePrograma);
						programaChamado.setTipoArtefato(TipoArtefato.PROGRAMA_COBOL);
						listaArtefato.add(programaChamado);
						// artefato.adicionarArtefatosRelacionados();
					}
				} else if (m_call.matches()) {
					String nomePrograma = m_call.group("nomePrograma");
					if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), null, nomePrograma)) {
						programaChamado = new Artefato();
						programaChamado.setNome(nomePrograma);
						programaChamado.setNomeInterno(nomePrograma);
						programaChamado.setTipoArtefato(TipoArtefato.PROGRAMA_COBOL);
						listaArtefato.add(programaChamado);
						// artefato.adicionarArtefatosRelacionados(programaChamado);
					}
				} 
				
				if (m_move.matches()) {
					String nomePrograma = m_move.group("nomePrograma");
					nomePrograma = nomePrograma.replace("WAOI", "AOI");
					if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), null, nomePrograma)) {
						programaChamado = new Artefato();
						programaChamado.setNome(nomePrograma);
						programaChamado.setNomeInterno(nomePrograma);
						programaChamado.setTipoArtefato(TipoArtefato.PROGRAMA_COBOL);
						listaArtefato.add(programaChamado);
						// artefato.adicionarArtefatosRelacionados(programaChamado);
					}
				}
				if (m_move_aspas.matches()) {
					String nomePrograma = m_move_aspas.group("nomePrograma");
					nomePrograma = nomePrograma.replace("WAOI", "AOI");
					if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), null, nomePrograma)) {
						programaChamado = new Artefato();
						programaChamado.setNome(nomePrograma);
						programaChamado.setNomeInterno(nomePrograma);
						programaChamado.setTipoArtefato(TipoArtefato.PROGRAMA_COBOL);
						listaArtefato.add(programaChamado);
						// artefato.adicionarArtefatosRelacionados(programaChamado);
					}
				}
			}
		}

		artefato.adicionarArtefatosRelacionados(listaArtefato);

		return artefato;
	}

	private Artefato executeCics(Artefato artefato) throws Exception {
		List<String> listaExecCics = new ArrayList<>();

		Matcher m_exec_cics_2 = null;
		Matcher m_exec_cics_1 = null;
		Matcher m_end_exec_1 = null;

		StringBuilder sb = null;

		boolean marcadorInicioExec = false;

		// List<Artefato> tempLista = new ArrayList<>();
		for (Artefato paragrafo : artefato.getArtefatosRelacionados()) {
			for (String texto : paragrafo.getCodigoFonte()) {
				if (texto.trim().equals("") || texto.length() < 8) {
					continue;
				}

				String textoCodigo = texto.substring(6);

				m_exec_cics_2 = Patterns.COBOL_P_EXEC_CICS_2.matcher(textoCodigo);
				m_exec_cics_1 = Patterns.COBOL_P_EXEC_CICS_1.matcher(textoCodigo);
				m_end_exec_1 = Patterns.COBOL_P_END_EXEC_1.matcher(textoCodigo);

				if (m_exec_cics_1.matches()) {
					marcadorInicioExec = true;
					sb = new StringBuilder();
				} else if (m_exec_cics_2.matches()) {
					marcadorInicioExec = true;
					sb = new StringBuilder();
				}

				if (marcadorInicioExec) {
					sb.append(" ");
					sb.append(textoCodigo);
				}

				if (m_end_exec_1.matches() && marcadorInicioExec) {
					String execString = sb.toString().replaceAll("[\\s]{2,}", " ").trim();
					listaExecCics.add(execString);
					marcadorInicioExec = false;
				}
			}
		}

		Matcher m_exec_cics_another_1 = null;
		Matcher m_transid_1 = null;
		Matcher m_from_1 = null;
		Matcher m_handle_1 = null;

		Matcher m_label_1 = null;
		Matcher m_enddata_1 = null;
		Matcher m_error_1 = null;
		Matcher m_qname_1 = null;
		Matcher m_item_1 = null;
		Matcher m_numitems_1 = null;
		Matcher m_into_1 = null;
		Matcher m_termid_1 = null;
		Matcher m_reqid_1 = null;
		Matcher m_length_1 = null;
		Matcher m_abstime_1 = null;
		Matcher m_time_1 = null;
		Matcher m_queue_1 = null;
		Matcher m_rtermid_1 = null;
		Matcher m_rtransid_1 = null;
		Matcher m_sysid_1 = null;
		Matcher m_userid_1 = null;
		Matcher m_program_1 = null;
		Matcher m_commarea_1 = null;

		for (String texto : listaExecCics) {

			m_exec_cics_another_1 = Patterns.COBOL_P_EXEC_CICS_ANOTHER_1.matcher(texto);
			m_transid_1 = Patterns.COBOL_P_TRANSID_1.matcher(texto);
			m_from_1 = Patterns.COBOL_P_CICS_FROM_1.matcher(texto);

			m_enddata_1 = Patterns.COBOL_P_ENDDATA_1.matcher(texto);
			m_error_1 = Patterns.COBOL_P_ERROR_1.matcher(texto);

			m_label_1 = Patterns.COBOL_P_LABEL_1.matcher(texto);
			m_qname_1 = Patterns.COBOL_P_QNAME_1.matcher(texto);
			m_item_1 = Patterns.COBOL_P_ITEM_1.matcher(texto);
			m_numitems_1 = Patterns.COBOL_P_NUMITEMS_1.matcher(texto);
			m_into_1 = Patterns.COBOL_P_INTO_1.matcher(texto);
			m_termid_1 = Patterns.COBOL_P_TERMID_1.matcher(texto);
			m_reqid_1 = Patterns.COBOL_P_REQID_1.matcher(texto);
			m_length_1 = Patterns.COBOL_P_LENGTH_1.matcher(texto);
			m_abstime_1 = Patterns.COBOL_P_ABSTIME_1.matcher(texto);
			m_time_1 = Patterns.COBOL_P_TIME_1.matcher(texto);
			m_queue_1 = Patterns.COBOL_P_QUEUE_1.matcher(texto);
			m_rtermid_1 = Patterns.COBOL_P_RTERMID_1.matcher(texto);
			m_rtransid_1 = Patterns.COBOL_P_RTRANSID_1.matcher(texto);
			m_sysid_1 = Patterns.COBOL_P_SYSID_1.matcher(texto);
			m_userid_1 = Patterns.COBOL_P_USERID_1.matcher(texto);
			m_program_1 = Patterns.COBOL_P_PROGRAM_1.matcher(texto);
			m_handle_1 = Patterns.COBOL_P_HANDLE_1.matcher(texto);
			m_commarea_1 = Patterns.COBOL_P_COMMAREA_1.matcher(texto);

			String anotherName = null;
			String transId = null;
			String from = null;

			String label = null;
			String enddata = null;
			String error = null;
			String qname = null;
			String item = null;
			String numitems = null;
			String into = null;
			String termId = null;
			String reqId = null;
			String length = null;
			String abstime = null;
			String time = null;
			String queue = null;
			String rtermId = null;
			String rtransId = null;
			String sysid = null;
			String userid = null;
			String program = null;
			String handle = null;
			String commarea = null;

			if (m_exec_cics_another_1.matches()) {
				anotherName = m_exec_cics_another_1.group("parametro");
			}

			if (m_transid_1.matches()) {
				transId = m_transid_1.group("parametro");
				transId = transId.replaceAll("[\\s]{2,}", " ").trim();
			}
			if (m_from_1.matches()) {
				from = m_from_1.group("parametro");
				from = from.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_length_1.matches()) {
				length = m_length_1.group("parametro");
				length = length.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_label_1.matches()) {
				label = m_label_1.group("parametro");
				label = label.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_enddata_1.matches()) {
				enddata = m_enddata_1.group("parametro");
				enddata = enddata.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_error_1.matches()) {
				error = m_error_1.group("parametro");
				error = error.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_qname_1.matches()) {
				qname = m_qname_1.group("parametro");
				qname = qname.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_item_1.matches()) {
				item = m_item_1.group("parametro");
				item = item.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_numitems_1.matches()) {
				numitems = m_numitems_1.group("parametro");
				numitems = numitems.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_into_1.matches()) {
				into = m_into_1.group("parametro");
				into = into.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_termid_1.matches()) {
				termId = m_termid_1.group("parametro");
				termId = termId.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_reqid_1.matches()) {
				reqId = m_reqid_1.group("parametro");
				reqId = reqId.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_abstime_1.matches()) {
				abstime = m_abstime_1.group("parametro");
				abstime = abstime.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_time_1.matches()) {
				time = m_time_1.group("parametro");
				time = time.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_queue_1.matches()) {
				queue = m_queue_1.group("parametro");
				queue = queue.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_rtermid_1.matches()) {
				rtermId = m_rtermid_1.group("parametro");
				rtermId = rtermId.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_rtransid_1.matches()) {
				rtransId = m_rtransid_1.group("parametro");
				rtransId = rtransId.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_sysid_1.matches()) {
				sysid = m_sysid_1.group("parametro");
				sysid = sysid.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_userid_1.matches()) {
				userid = m_userid_1.group("parametro");
				userid = userid.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_program_1.matches()) {
				program = m_program_1.group("parametro");
				program = program.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_handle_1.matches()) {
				handle = m_handle_1.group("parametro");
				handle = handle.replaceAll("[\\s]{2,}", " ").trim();
			}

			if (m_commarea_1.matches()) {
				commarea = m_commarea_1.group("parametro");
				commarea = commarea.replaceAll("[\\s]{2,}", " ").trim();
			}

			Artefato artefatoTransaction = new Artefato();

			artefatoTransaction.setAmbiente(Configuracao.AMBIENTE);
			artefatoTransaction.setSistema(Configuracao.SISTEMA);
			artefatoTransaction.setTipoArtefato(TipoArtefato.CICS_TRANSACTION);

			// if (transId != null && !"".equals(transId)) {
			// artefatoTransaction.setNome(transId);
			// } else if (rtransId != null && !"".equals(rtransId)) {
			// artefatoTransaction.setNome(rtransId);
			// } else if (anotherName != null && !"".equals(anotherName)) {

			if ("LINK".equals(anotherName.toUpperCase().trim()) && program != null) {
				artefatoTransaction.setNome(program);
				artefatoTransaction.setNomeInterno(program);
			} else if ("HANDLE".equals(anotherName.toUpperCase().trim()) && handle != null) {
				artefatoTransaction.setNome(handle);
				artefatoTransaction.setNomeInterno(handle);
			} else {
				artefatoTransaction.setNome(anotherName);
				artefatoTransaction.setNomeInterno(anotherName);
			}
			// } else {
			// throw new RuntimeException(
			// "Nome da transação não foi identificada. Arquivo " +
			// artefato.getCaminhoArquivo());
			// }

			if (label != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_LABEL);
				atributo.setValor(label);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("TRANSACTION-ID", transId);
			}

			if (enddata != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_ENDDATA);
				atributo.setValor(enddata);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("TRANSACTION-ID", transId);
			}

			if (error != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_ERROR);
				atributo.setValor(error);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("TRANSACTION-ID", transId);
			}

			if (qname != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_QNAME);
				atributo.setValor(qname);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("TRANSACTION-ID", transId);
			}

			if (item != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_ITEM);
				atributo.setValor(item);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("TRANSACTION-ID", transId);
			}

			if (numitems != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_NUMITEMS);
				atributo.setValor(numitems);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("TRANSACTION-ID", transId);
			}

			if (into != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_INTO);
				atributo.setValor(into);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("TRANSACTION-ID", transId);
			}

			if (transId != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_TRANSACTION_ID);
				atributo.setValor(transId);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("TRANSACTION-ID", transId);
			}
			if (from != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_FROM);
				atributo.setValor(from);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("FROM", from);
			}
			if (termId != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_TERMINAL_ID);
				atributo.setValor(termId);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("TERMINAL-ID", termId);
			}
			if (reqId != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.REQ_ID);
				atributo.setValor(reqId);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("REQ-ID", reqId);
			}
			if (length != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_LENGTH);
				atributo.setValor(length);
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("LENGTH", length);
			}
			if (abstime != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_ABSTIME);
				atributo.setValor(abstime);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("TIME", time);
			}
			if (time != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_TIME);
				atributo.setValor(time);
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("TIME", time);
			}
			if (queue != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_QUEUE);
				atributo.setValor(queue);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("QUEUE", queue);
			}
			if (rtermId != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_R_TERMINAL_ID);
				atributo.setValor(rtermId);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("R-TERMINAL-ID", rtermId);
			}
			if (rtransId != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_R_TRANSACTION_ID);
				atributo.setValor(rtransId);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("R-TRANSACTION-ID", rtransId);
			}
			if (sysid != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_SYSTEM_ID);
				atributo.setValor(sysid);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("SYSTEM-ID", sysid);
			}
			if (userid != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_USER_ID);
				atributo.setValor(userid);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("USER-ID", userid);
			}
			if (program != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_PROGRAM);
				atributo.setValor(program);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("PROGRAM", program);
			}

			if (commarea != null) {
				Atributo atributo = new Atributo();
				atributo.setTipoAtributo(TipoAtributo.CICS_COMMAREA);
				atributo.setValor(commarea);
				// atributo.setTipo("STRING");
				atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				artefatoTransaction.adicionarAtributo(atributo);
				// transaction.getParametros().put("COMMAREA", commarea);
			}

			artefato.adicionarArtefatosRelacionados(artefatoTransaction);
		}

		return artefato;
	}

	private Artefato substituirReferencia(Artefato artefato) throws Exception {

		List<Artefato> tempTransaction = new ArrayList<>();

		for (Artefato artefatoEntry : artefato.getArtefatosRelacionados()) {

			if (TipoArtefato.COPYBOOK_VARIAVEL.equals(artefatoEntry.getTipoArtefato())
					|| TipoArtefato.PROGRAMA_COBOL_PARAGRAFO.equals(artefatoEntry.getTipoArtefato())) {
				continue;
			}

			Artefato variavel = ArtefatoHandler.recuperarArtefato(listaVariaveisPesquisa,
					TipoArtefato.COPYBOOK_VARIAVEL, artefatoEntry.getNome());

			if (variavel != null) {
				String novoNome = artefatoEntry.getNome();
				Atributo atributo = variavel.buscaAtributo(TipoAtributo.VALOR_PADRAO);

				if (atributo != null && atributo.getValor() != null && !"SPACES".equals(atributo.getValor())) {
					novoNome = atributo.getValor();
				} else {
					if (variavel.getArtefatosRelacionados() != null && variavel.getArtefatosRelacionados().size() > 0) {
						Atributo subvariavelAtributo = variavel.getArtefatosRelacionados().get(0)
								.buscaAtributo(TipoAtributo.VALOR_PADRAO);
						if (subvariavelAtributo != null) {
							novoNome = subvariavelAtributo.getValor();
						}
					}
				}

				artefatoEntry.setExcluir(true);

				Artefato novaArtefatoEntry = new Artefato();
				// novaArtefatoEntry.setAmbiente(Configuracao.AMBIENTE);
				// novaArtefatoEntry.setSistema(Configuracao.SISTEMA);
				novaArtefatoEntry.setTipoArtefato(artefatoEntry.getTipoArtefato());
				novaArtefatoEntry.setNome(novoNome);
				novaArtefatoEntry.setNomeInterno(artefatoEntry.getNome());

				novaArtefatoEntry.setAtributos(artefatoEntry.getAtributos());

				tempTransaction.add(novaArtefatoEntry);
			} else {
				tempTransaction.add(artefatoEntry);
			}
		}

		artefato.setArtefatosRelacionados(tempTransaction);

		return artefato;
	}

	private Artefato excluirArtefato(Artefato artefato) throws Exception {
		List<Artefato> tempLista = new ArrayList<>();

		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			if (!entry.isExcluir()) {
				tempLista.add(entry);
			} else {
				System.out.println(entry);
			}
		}

		artefato.setArtefatosRelacionados(tempLista);

		return artefato;
	}

	private Artefato tratarNomeArtefatos(Artefato artefato) throws Exception {

		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			String nomeTemp = entry.getNome().replaceAll("['\"]{1}", "").trim();
			entry.setNome(nomeTemp);
		}

		return artefato;
	}

	private Artefato atribuirSistemaTipo(Artefato artefato) throws Exception {
		if (artefato.getArtefatosRelacionados() == null) {
			return artefato;
		}

		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			atribuirSistemaTipo(entry);
			if (entry.getTipoArtefato() != null && !TipoArtefato.DESCONHECIDO.equals(entry.getTipoArtefato())
					&& !"DESCONHECIDO".equals(entry.getSistema())) {
				continue;
			}
			if (TipoArtefato.COPYBOOK_VARIAVEL.equals(entry.getTipoArtefato())
					|| TipoArtefato.PROGRAMA_COBOL_PARAGRAFO.equals(entry.getTipoArtefato())
					|| TipoArtefato.JCL_VARIAVEL.equals(entry.getTipoArtefato())) {
				continue;
			}

			ArtefatoPersistence artefatoPersquisa = ArtefatoHandler.buscarArtefatoPersistence(entry.getNome(),
					entry.getTipoArtefato(), entry.getSistema(), this.artefato.getNome(),
					this.artefato.getTipoArtefato(), this.artefato.getSistema());

			if (artefatoPersquisa != null) {
				entry.setSistema(artefatoPersquisa.getCoSistema());

				TipoArtefato[] arrTipo = TipoArtefato.values();
				TipoArtefato tipo = Arrays.asList(arrTipo).stream()
						.filter(p -> p.get().equals(artefatoPersquisa.getCoTipoArtefato())).findFirst().get();

				entry.setTipoArtefato(tipo);
			} else if (this.artefato.getNome().equals(entry.getNome())) {
				entry.setSistema("DESCONHECIDO");
				entry.setTipoArtefato(TipoArtefato.DESCONHECIDO);
			}

			if ("DESCONHECIDO".equals(entry.getSistema())) {
				String coSistema = "SI" + entry.getNome().substring(0, 3);
				SistemaPersistence sistemaPersistence = ArtefatoHandler.buscarSistemaPersistence(coSistema);

				if (sistemaPersistence != null) {
					entry.setSistema(coSistema);
				}
			}
		}

		return artefato;
	}

	private Artefato atribuirTipoArtefato(Artefato artefato) throws Exception {

		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			if (entry.getTipoArtefato() != null) {
				continue;
			}

			TipoArtefato tipoArtefato = ArtefatoHandler.identificarTipoArtefato(entry.getNome());
			entry.setTipoArtefato(tipoArtefato);
		}

		return artefato;
	}

	private Artefato atribuirSistema(Artefato artefato) throws Exception {

		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			if (entry.getSistema() != null) {
				continue;
			}

			String sistema = ArtefatoHandler.identificarSistema(entry.getNome(), entry.getTipoArtefato());
			entry.setSistema(sistema);
		}

		return artefato;
	}

	private Artefato atribuirAmbiente(Artefato artefato) throws Exception {

		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			if (entry.getAmbiente() != null) {
				continue;
			}

			TipoAmbiente ambiente = ArtefatoHandler.identificarAmbiente(entry.getNome(), entry.getTipoArtefato());
			entry.setAmbiente(ambiente);
		}

		return artefato;
	}

	private Artefato objetivo(Artefato artefato) throws Exception {

		Matcher m_identification_division = null;
		Matcher m_modificacao_1_inicio = null;

		Matcher m_descricao_1_inicio = null;
		Matcher m_descricao_2_inicio = null;
		Matcher m_descricao_3_inicio = null;

		Matcher m_objetivo_1_inicio = null;
		Matcher m_objetivo_2_inicio = null;
		Matcher m_objetivo_3_inicio = null;

		Matcher m_objetivo_1_meio = null;
		Matcher m_objetivo_2_meio = null;
		Matcher m_objetivo_3_meio = null;
		Matcher m_objetivo_4_meio = null;

		// private Matcher m_descricao_1_fim = null;
		Matcher m_objetivo_2_fim = null;
		Matcher m_objetivo_3_fim = null;
		Matcher m_objetivo_4_fim = null;

		// Incluir lógica para captura da descrição do programa
		StringBuilder descricao = new StringBuilder();
		StringBuilder comentariosAteIdentification = new StringBuilder();

		for (String texto : artefato.getComentarios()) {
			foiProcessado = false;
			if (texto.trim().equals("") || texto.length() < 7) {
				continue;
			}

			m_descricao_1_inicio = Patterns.COBOL_P_DESCRICAO_1_INICIO.matcher(texto);
			m_descricao_2_inicio = Patterns.COBOL_P_DESCRICAO_2_INICIO.matcher(texto);
			m_descricao_3_inicio = Patterns.COBOL_P_DESCRICAO_3_INICIO.matcher(texto);

			m_objetivo_1_inicio = Patterns.COBOL_P_OBJETIVO_1_INICIO.matcher(texto);
			m_objetivo_2_inicio = Patterns.COBOL_P_OBJETIVO_2_INICIO.matcher(texto);
			m_objetivo_3_inicio = Patterns.COBOL_P_OBJETIVO_3_INICIO.matcher(texto);

			m_objetivo_1_meio = Patterns.COBOL_P_OBJETIVO_1_MEIO.matcher(texto);
			m_objetivo_2_meio = Patterns.COBOL_P_OBJETIVO_2_MEIO.matcher(texto);
			m_objetivo_3_meio = Patterns.COBOL_P_OBJETIVO_3_MEIO.matcher(texto);
			m_objetivo_4_meio = Patterns.COBOL_P_OBJETIVO_4_MEIO.matcher(texto);

			// m_objetivo_1_fim = P_DESCRICAO_1_FIM.matcher(texto);
			m_objetivo_2_fim = Patterns.COBOL_P_OBJETIVO_2_FIM.matcher(texto);
			m_objetivo_3_fim = Patterns.COBOL_P_OBJETIVO_3_FIM.matcher(texto);
			m_objetivo_4_fim = Patterns.COBOL_P_OBJETIVO_4_FIM.matcher(texto);

			m_identification_division = Patterns.COBOL_P_IDENTIFICATION_DIVISION.matcher(texto);
			m_modificacao_1_inicio = Patterns.COBOL_P_MODIFICACAO_1_INICIO.matcher(texto);

			if (m_modificacao_1_inicio.matches() || m_descricao_1_inicio.matches() || m_descricao_2_inicio.matches()
					|| m_descricao_3_inicio.matches()) {
				break;
			}

			if (marcardor_objetivo && !foiProcessado && m_objetivo_4_fim.matches()) {
				foiProcessado = true;
				marcardor_objetivo = false;
				break;
			}

			if (marcardor_objetivo && !foiProcessado && m_objetivo_3_fim.matches()) {
				foiProcessado = true;
				marcardor_objetivo = false;
				break;
			}

			if (marcardor_objetivo && !foiProcessado && m_objetivo_2_fim.matches()) {
				foiProcessado = true;
				marcardor_objetivo = false;
				break;
			}

			if (!marcardor_objetivo && !foiProcessado && m_objetivo_3_inicio.matches()) {
				foiProcessado = true;
				marcardor_objetivo = true;
				descricao.append(m_objetivo_3_inicio.group("descricao"));
				descricao.append(" ");
			}

			if (!marcardor_objetivo && !foiProcessado && m_objetivo_2_inicio.matches()) {
				foiProcessado = true;
				marcardor_objetivo = true;
				descricao.append(m_objetivo_2_inicio.group("descricao"));
				descricao.append(" ");
			}
			if (!marcardor_objetivo && !foiProcessado && m_objetivo_1_inicio.matches()) {
				foiProcessado = true;
				marcardor_objetivo = true;
			}

			if (marcardor_objetivo && !foiProcessado && m_objetivo_2_meio.matches()) {
				foiProcessado = true;
				descricao.append(m_objetivo_2_meio.group("descricao"));
				descricao.append(" ");
			}

			if (marcardor_objetivo && !foiProcessado && m_objetivo_4_meio.matches()) {
				foiProcessado = true;
				descricao.append(m_objetivo_4_meio.group("descricao"));
				descricao.append(" ");
			}

			if (marcardor_objetivo && !foiProcessado && m_objetivo_1_meio.matches()) {
				foiProcessado = true;
				descricao.append(m_objetivo_1_meio.group("descricao"));
				descricao.append(" ");
			}
			if (marcardor_objetivo && !foiProcessado && m_objetivo_3_meio.matches()) {
				foiProcessado = true;
				descricao.append(m_objetivo_3_meio.group("descricao"));
				descricao.append(" ");
			}

			if (m_identification_division.matches()) {
				break;
			}

			comentariosAteIdentification.append(texto);
		}

		if (descricao.toString().trim().isEmpty()) {
			// cobol.setDescricao(comentariosAteIdentification.toString());
		} else {
			artefato.setDescricao(descricao.toString().replaceAll("[=-]{1,}", " ").replaceAll("[\\s]{2,}", " ").trim());
		}

		return artefato;
	}

	private Artefato descricaoMaiframe(Artefato artefato) throws Exception {
		StringBuilder sb = new StringBuilder();
		int QUANTIDADE_LINHA_COMENTARIO_DESCRICAO = 20;
		int linhaAtual = 0;
		boolean marcador = false;
		boolean marcador2 = false;

		for (String texto : artefato.getComentarios()) {
			if (texto.trim().equals("") || texto.length() < 7) {
				continue;
			}

			if (!marcador && texto.contains("SOURCE LEVEL INFORMATION")) {
				marcador = true;
				continue;
			}

			if (marcador && !marcador2 && (texto.startsWith("D") || texto.contains("NOT BEEN PROCESSED"))) {
				marcador2 = true;
				continue;
			}

			if (marcador && marcador2) {

				if (texto != null && texto.length() > (6)
						&& (texto.substring(6, 7).equals("*") || !texto.substring(6, 7).equals(" "))) {
					sb.append(texto.substring(6));
					linhaAtual++;
				}
			}

			if (linhaAtual > QUANTIDADE_LINHA_COMENTARIO_DESCRICAO) {
				break;
			}
		}

		linhaAtual = 0;

		for (String texto : artefato.getCodigoCompleto()) {

			if (texto.trim().equals("") || texto.length() < 7) {
				continue;
			}
			if (texto.trim().contains("IDENTIFICATION DIVISION")) {
				marcador = true;
				continue;
			}

			if (marcador) {
				if (texto != null && texto.length() > (deslocamento + 6)
						&& (texto.substring(deslocamento + 6, deslocamento + 7).equals("*")
								|| !texto.substring(deslocamento + 6, deslocamento + 7).equals(" "))) {
					sb.append(texto.substring(deslocamento + 6));
					linhaAtual++;
				}
			}

			if (linhaAtual > QUANTIDADE_LINHA_COMENTARIO_DESCRICAO) {
				break;
			}
		}
		String descricao = sb.toString().replaceAll("[-*_+=]{1,}", " ").replaceAll("[\\s]{2,}", " ").trim();

		artefato.setDescricao(descricao);

		return artefato;

	}

	private List<String> _navegaRoteiroVariavel(List<String> roteiro) throws Exception {

		Matcher m_copy = null;
		Matcher m_copy_replacing_1 = null;

		List<String> returnValue = new ArrayList<>();
		boolean marcador = false;
		String nomeCopybook = null;
		String replaceValorAntigo = null;
		String replaceNovoValor = null;

		for (String texto : roteiro) {
			if (texto.trim().equals("") || texto.length() < 8) {
				continue;
			}

			String textoCodigo = texto.substring(6);
			m_copy = Patterns.COBOL_P_COPY.matcher(textoCodigo);
			m_copy_replacing_1 = Patterns.COBOL_P_COPY_REPLACING_1.matcher(textoCodigo);

			marcador = false;

			if (m_copy.matches()) {
				nomeCopybook = m_copy.group("nomeChamado");
				marcador = true;
			} else if (m_copy_replacing_1.matches()) {
				nomeCopybook = m_copy_replacing_1.group("nomeChamado");
				marcador = true;

				replaceValorAntigo = m_copy_replacing_1.group("valorAntigo");
				replaceNovoValor = m_copy_replacing_1.group("novoValor");

			} // else {
				// returnValue.add(texto);
				// }

			if (marcador) {
				Artefato copybookChamado = null;
				Artefato copybookInclusao = null;

				nomeCopybook = UtilsHandler.removerPontoFinal(nomeCopybook);
				nomeCopybook = nomeCopybook.replaceAll("[\"\']{1,}", "");

				copybookChamado = ArtefatoHandler.extrairArtefato(nomeCopybook, TipoArtefato.COPYBOOK, 0);

				if (copybookChamado != null) {
					TipoAmbiente ambiente = ArtefatoHandler.identificarAmbiente(nomeCopybook, TipoArtefato.COPYBOOK);
					String sistema = ArtefatoHandler.identificarSistema(nomeCopybook, TipoArtefato.COPYBOOK);

					copybookChamado.setNome(nomeCopybook);
					copybookChamado.setTipoArtefato(TipoArtefato.COPYBOOK);
					copybookChamado.setAmbiente(ambiente);
					copybookChamado.setSistema(sistema);

					copybookInclusao = new Artefato();
					
					copybookInclusao.setNome(nomeCopybook);
					copybookInclusao.setTipoArtefato(TipoArtefato.COPYBOOK);
					copybookInclusao.setAmbiente(ambiente);
					copybookInclusao.setSistema(sistema);

					for (String entry : copybookChamado.getCodigoFonte()) {
						String entryTexto = null;
						if (replaceValorAntigo != null && replaceNovoValor != null) {
							entryTexto = entry.replace(replaceValorAntigo, replaceNovoValor);
						} else {
							entryTexto = entry;
						}
						returnValue.add(entryTexto);
					}
				}

				if (copybookChamado == null) {
					TipoAmbiente ambiente = ArtefatoHandler.identificarAmbiente(nomeCopybook, TipoArtefato.COPYBOOK);
					String sistema = ArtefatoHandler.identificarSistema(nomeCopybook, TipoArtefato.COPYBOOK);

					copybookInclusao = new Artefato();
					copybookInclusao.setNome(nomeCopybook);
					copybookInclusao.setTipoArtefato(TipoArtefato.COPYBOOK);
					copybookInclusao.setAmbiente(ambiente);
					copybookInclusao.setSistema(sistema);
				}
				artefato.adicionarArtefatosRelacionados(copybookInclusao);
			} else {
				returnValue.add(texto);
			}
		}

		// REPETE CASO AINDA EXISTA COPYBOOK SENDO IMPORTADA
		boolean repetir = false;
		for (String texto : returnValue) {
			m_copy = Patterns.COBOL_P_COPY.matcher(texto);
			if (m_copy.matches()) {
				repetir = true;
			}
		}
		if (repetir) {
			returnValue = _navegaRoteiroVariavel(returnValue);
		}

		return returnValue;
	}

	private Artefato _tratarVerbosFile(Artefato artefato) throws Exception {
		List<String> listaLinhasVerbos = new ArrayList<>();

		Matcher m_verbo_delete = null;

		for (String textoCompleto : artefato.getCodigoFonte()) {

			if (textoCompleto.trim().length() <= 6) {
				continue;
			}
			String texto = textoCompleto.substring(6).trim();

			m_verbo_delete = Patterns.COBOL_P_VERBO_DELETE.matcher(texto);

			if (texto.startsWith("WRITE ") || texto.startsWith("REWRITE ")
					|| (texto.startsWith("DELETE ") && m_verbo_delete.matches()) || texto.startsWith("READ ")) {
				listaLinhasVerbos.add(texto);
			}
		}

		Matcher m_verbo_arquivo = null;

		for (String texto : listaLinhasVerbos) {
			m_verbo_arquivo = Patterns.COBOL_P_VERBO_ARQUIVO.matcher(texto);

			if (m_verbo_arquivo.matches()) {
				String verbo = m_verbo_arquivo.group("verbo");
				String arquivo = m_verbo_arquivo.group("arquivo");

				for (Artefato artefatoFileDescription : artefato.getArtefatosRelacionados()) {
					if (!TipoArtefato.FILE_DESCRIPTION.equals(artefatoFileDescription.getTipoArtefato())) {
						continue;
					}

					/**
					 * TODO Incluir verbos
					 */

					Atributo atributoDataRecord = artefatoFileDescription.buscaAtributo(TipoAtributo.DATA_RECORD);

					/// if (arquivo.equals(artefato.getNome()) ||
					/// arquivo.equals(atributoDataRecord.getValor())
					// || arquivo.equals(atributo.getNomeVariavel())) {
					if (arquivo.equals(artefatoFileDescription.getNome())
							|| (atributoDataRecord != null && arquivo.equals(atributoDataRecord.getValor()))) {

						switch (verbo.toUpperCase()) {
						case "READ":

							Atributo atributoRead = new Atributo();
							atributoRead.setTipoAtributo(TipoAtributo.COBOL_READ);
							// atributoRead.setTipo("BOOLEAN");
							atributoRead.setTipo("ARTEFATO");
							atributoRead.setValor("true");

							artefatoFileDescription.adicionarAtributo(atributoRead);

							break;
						case "WRITE":
							Atributo atributoWrite = new Atributo();
							atributoWrite.setTipoAtributo(TipoAtributo.COBOL_WRITE);
							// atributoWrite.setTipo("BOOLEAN");
							atributoWrite.setValor("true");
							atributoWrite.setTipo("ARTEFATO");
							artefatoFileDescription.adicionarAtributo(atributoWrite);
							break;
						case "REWRITE":

							Atributo atributoRewrite = new Atributo();
							atributoRewrite.setTipoAtributo(TipoAtributo.COBOL_REWRITE);
							// atributoRewrite.setTipo("BOOLEAN");
							atributoRewrite.setValor("true");
							atributoRewrite.setTipo("ARTEFATO");
							artefatoFileDescription.adicionarAtributo(atributoRewrite);

							break;
						case "DELETE":

							Atributo atributoDelete = new Atributo();
							atributoDelete.setTipoAtributo(TipoAtributo.COBOL_DELETE);
							// atributoDelete.setTipo("BOOLEAN");
							atributoDelete.setValor("true");
							atributoDelete.setTipo("ARTEFATO");
							artefatoFileDescription.adicionarAtributo(atributoDelete);

							break;
						default:
							break;
						}
					}

				}
			}
		}

		return artefato;
	}

	private Artefato _tratarEnviromentDivision(Artefato artefato) throws Exception {
		Matcher m_file_control_inicio = null;
		Matcher m_select_inicio = null;
		Matcher m_select_fim = null;

		boolean marcadorFileControl = false;
		boolean marcadorSelect = false;

		StringBuilder sb = null;
		List<String> listaOperacoes = new ArrayList<>();

		for (String textoOriginal : artefato.getCodigoFonte()) {
			if (textoOriginal.trim().length() <= 6) {
				continue;
			}

			String texto = textoOriginal.substring(6);

			m_file_control_inicio = Patterns.COBOL_P_FILE_CONTROL_INICIO.matcher(texto);
			m_select_inicio = Patterns.COBOL_P_SELECT_INICIO.matcher(texto);
			m_select_fim = Patterns.COBOL_P_SELECT_FIM.matcher(texto);

			if (m_file_control_inicio.matches()) {
				marcadorFileControl = true;
			}

			if (m_select_fim.matches() && marcadorSelect && marcadorFileControl) {
				marcadorSelect = false;
				sb.append(texto);
				sb.append(" ");
				listaOperacoes.add(sb.toString().replaceAll("[\\s]{2,}", " ").trim());
				sb = null;
			}

			if (marcadorSelect && marcadorFileControl) {
				sb.append(texto);
				sb.append(" ");
			}

			if (m_select_inicio.matches() && !marcadorSelect && marcadorFileControl) {
				marcadorSelect = true;
				sb = new StringBuilder();
				sb.append(texto);
				sb.append(" ");
			}

			if (m_select_inicio.matches() && m_select_fim.matches() && marcadorFileControl) {
				listaOperacoes.add(sb.toString().replaceAll("[\\s]{2,}", " ").trim());
				marcadorSelect = false;
				sb = null;
			}
		}

		if (sb != null) {
			listaOperacoes.add(sb.toString().replaceAll("[\\s]{2,}", " ").trim());
		}

		Matcher m_identificador_optional = null;
		Matcher m_identificador = null;
		Matcher m_assign = null;
		Matcher m_assign_to = null;
		Matcher m_organization_is = null;
		Matcher m_access_mode = null;
		Matcher m_record_key_is = null;
		Matcher m_alternate_record_key_is = null;

		for (String texto : listaOperacoes) {
			boolean optional = false;
			String ddname = null;
			String fileName = null;
			String organization = null;
			String accessMode = null;
			String recordKey = null;
			String alternateRecordKey = null;

			m_identificador_optional = Patterns.COBOL_P_IDENTIFICADOR_OPTIONAL.matcher(texto);
			m_identificador = Patterns.COBOL_P_IDENTIFICADOR.matcher(texto);
			m_assign = Patterns.COBOL_P_ASSIGN.matcher(texto);
			m_assign_to = Patterns.COBOL_P_ASSIGN_TO.matcher(texto);
			m_organization_is = Patterns.COBOL_P_ORGANIZATION_IS.matcher(texto);
			m_access_mode = Patterns.COBOL_P_ACCESS_MODE.matcher(texto);
			m_record_key_is = Patterns.COBOL_P_RECORD_KEY_IS.matcher(texto);
			m_alternate_record_key_is = Patterns.COBOL_P_ALTERNATE_RECORD_KEY_IS.matcher(texto);

			if (m_identificador_optional.matches()) {
				fileName = m_identificador_optional.group("parametro");
				optional = true;
			} else if (m_identificador.matches()) {
				fileName = m_identificador.group("parametro");
			}
			if (m_assign_to.matches()) {
				ddname = m_assign_to.group("parametro");
			} else if (m_assign.matches()) {
				ddname = m_assign.group("parametro");
			}
			if (m_organization_is.matches()) {
				organization = m_organization_is.group("parametro");
			}
			if (m_access_mode.matches()) {
				accessMode = m_access_mode.group("parametro");
			}
			if (m_record_key_is.matches()) {
				recordKey = m_record_key_is.group("parametro");
			}
			if (m_alternate_record_key_is.matches()) {
				alternateRecordKey = m_alternate_record_key_is.group("parametro");
			}

			Artefato artefatoFileDescription = new Artefato();
			artefatoFileDescription.setAmbiente(this.artefato.getAmbiente());
			artefatoFileDescription.setSistema(this.artefato.getSistema());
			artefatoFileDescription.setNome(fileName);
			artefatoFileDescription.setTipoArtefato(TipoArtefato.FILE_DESCRIPTION);

			if (accessMode != null) {
				Atributo atributoAccessMode = new Atributo();
				atributoAccessMode.setValor(accessMode);
				// atributoAccessMode.setTipo("STRING");
				atributoAccessMode.setTipo("ARTEFATO");
				atributoAccessMode.setTipoAtributo(TipoAtributo.COBOL_ACCESS_MODE);
				artefatoFileDescription.adicionarAtributo(atributoAccessMode);
			}

			if (ddname != null) {
				Atributo atributoDdname = new Atributo();
				atributoDdname.setValor(ddname);
				// atributoDdname.setTipo("STRING");
				atributoDdname.setTipoAtributo(TipoAtributo.COBOL_DDNAME);
				atributoDdname.setTipo("ARTEFATO");
				artefatoFileDescription.adicionarAtributo(atributoDdname);
			}

			if (organization != null) {
				Atributo atributoOrganization = new Atributo();
				atributoOrganization.setValor(organization);
				// atributoOrganization.setTipo("STRING");
				atributoOrganization.setTipo("ARTEFATO");
				atributoOrganization.setTipoAtributo(TipoAtributo.COBOL_ORGANIZATION);
				artefatoFileDescription.adicionarAtributo(atributoOrganization);
			}

			if (recordKey != null) {
				Atributo atributoRecordKey = new Atributo();
				atributoRecordKey.setValor(recordKey);
				// atributoRecordKey.setTipo("STRING");
				atributoRecordKey.setTipo("ARTEFATO");
				atributoRecordKey.setTipoAtributo(TipoAtributo.COBOL_RECORD_KEY);
				artefatoFileDescription.adicionarAtributo(atributoRecordKey);
			}
			if (alternateRecordKey != null) {
				Atributo atributoAlternateRecordKey = new Atributo();
				atributoAlternateRecordKey.setValor(alternateRecordKey);
				// atributoAlternateRecordKey.setTipo("STRING");
				atributoAlternateRecordKey.setTipo("ARTEFATO");
				atributoAlternateRecordKey.setTipoAtributo(TipoAtributo.COBOL_ALTERNATE_RECORD_KEY);
				artefatoFileDescription.adicionarAtributo(atributoAlternateRecordKey);
			}

			Atributo atributoOptional = new Atributo();
			if (optional) {
				atributoOptional.setValor("TRUE");
			} else {
				atributoOptional.setValor("FALSE");
			}

			atributoOptional.setTipoAtributo(TipoAtributo.OPTIONAL);
			atributoOptional.setTipo("ARTEFATO");
			artefatoFileDescription.adicionarAtributo(atributoOptional);

			this.artefato.adicionarArtefatosRelacionados(artefatoFileDescription);
		}

		return artefato;
	}

	private Artefato _tratarDataDivision(Artefato artefato) throws Exception {

		Matcher m_file_section = null;
		Matcher m_fd_inicio = null;
		Matcher m_fd_fim = null;

		boolean marcadorFileSelection = false;
		boolean marcadorFdInicio = false;
		// boolean marcadorFdFim = false;

		StringBuilder sb = null;
		// String reservaTextoParaMapaVariavel = null;

		List<String> listaOperacoes = new ArrayList<>();

		for (String textoOriginal : artefato.getCodigoFonte()) {
			if (textoOriginal.trim().length() <= 6) {
				continue;
			}

			String texto = textoOriginal.substring(6);

			m_file_section = Patterns.COBOL_P_FILE_SECTION.matcher(texto);
			m_fd_inicio = Patterns.COBOL_P_FD_INICIO.matcher(texto);
			m_fd_fim = Patterns.COBOL_P_FD_FIM.matcher(texto);

			if (m_file_section.matches()) {
				marcadorFileSelection = true;
			}

			if (m_fd_fim.matches() && marcadorFdInicio && marcadorFileSelection) {
				marcadorFdInicio = false;
				// marcadorFdFim = true;
				sb.append(texto);
				sb.append(" ");
				listaOperacoes.add(sb.toString().replaceAll("[\\s]{2,}", " "));
				// reservaTextoParaMapaVariavel = sb.toString().replaceAll("[\\s]{2,}", " ");
				sb = null;
			}

			if (marcadorFdInicio && marcadorFileSelection) {
				sb.append(texto);
				sb.append(" ");
			}

			if (m_fd_inicio.matches() && !marcadorFdInicio && marcadorFileSelection) {
				sb = new StringBuilder();
				sb.append(texto);
				sb.append(" ");
				marcadorFdInicio = true;
				// marcadorFdFim = false;
				// reservaTextoParaMapaVariavel = null;
			}

			if (m_fd_inicio.matches() && m_fd_fim.matches() && marcadorFdInicio && marcadorFileSelection) {
				marcadorFdInicio = false;
				// marcadorFdFim = true;
				listaOperacoes.add(sb.toString().replaceAll("[\\s]{2,}", " "));
				// reservaTextoParaMapaVariavel = sb.toString().replaceAll("[\\s]{2,}", " ");
				sb = null;
			}

			// marcadorFdFim = false;
		}

		if (sb != null) {
			listaOperacoes.add(sb.toString().replaceAll("[\\s]{2,}", " ").trim());
		}

		Matcher m_fd = null;
		Matcher m_record_contains = null;
		Matcher m_record_is_varying = null;
		Matcher m_record_contains_n_to_n = null;

		Matcher m_block_contains = null;
		Matcher m_record_mode_is = null;
		Matcher m_data_record_is = null;

		for (String texto : listaOperacoes) {
			String nome = null;
			String recordContains = null;
			String blockContains = null;
			String recordingMode = null;
			String dataRecord = null;

			m_fd = Patterns.COBOL_P_FD.matcher(texto);
			m_record_contains = Patterns.COBOL_P_RECORD_CONTAINS.matcher(texto);
			m_block_contains = Patterns.COBOL_P_BLOCK_CONTAINS.matcher(texto);
			m_record_mode_is = Patterns.COBOL_P_RECORD_MODE_IS.matcher(texto);
			m_data_record_is = Patterns.COBOL_P_DATA_RECORD_IS.matcher(texto);
			m_record_is_varying = Patterns.COBOL_P_RECORD_IS_VARYING.matcher(texto);
			m_record_contains_n_to_n = Patterns.COBOL_P_RECORD_CONTAINS_N_TO_N.matcher(texto);

			if (m_fd.matches()) {
				nome = m_fd.group("parametro");
			}

			if (m_record_is_varying.matches()) {
				recordContains = "VARYING";
			} else if (m_record_contains_n_to_n.matches()) {
				recordContains = m_record_contains_n_to_n.group("parametro1") + " TO "
						+ m_record_contains_n_to_n.group("parametro2");
			} else if (m_record_contains.matches()) {
				recordContains = m_record_contains.group("parametro1") + " " + m_record_contains.group("parametro2");
			}
			if (m_block_contains.matches()) {
				blockContains = m_block_contains.group("parametro1");
			}

			if (m_record_mode_is.matches()) {
				recordingMode = m_record_mode_is.group("parametro");
			}
			if (m_data_record_is.matches()) {
				dataRecord = m_data_record_is.group("parametro");
			}

			boolean encontrou = false;
			for (Artefato entryFileDescription : this.artefato.getArtefatosRelacionados()) {

				if (TipoArtefato.FILE_DESCRIPTION.equals(entryFileDescription.getTipoArtefato())) {
					if (entryFileDescription.getNome().equals(nome)) {
						encontrou = true;

						if (recordContains != null) {
							Atributo atributoRecordContains = new Atributo();
							atributoRecordContains.setTipoAtributo(TipoAtributo.RECORD_CONTAINS);
							atributoRecordContains.setValor(recordContains);
							// atributoRecordContains.setTipo("STRING");
							atributoRecordContains.setTipo("ARTEFATO");

							entryFileDescription.adicionarAtributo(atributoRecordContains);
						}

						if (blockContains != null) {
							Atributo atributoBlockContains = new Atributo();
							atributoBlockContains.setTipoAtributo(TipoAtributo.BLOCK_CONTAINS);
							atributoBlockContains.setValor(blockContains);
							// atributoBlockContains.setTipo("STRING");
							atributoBlockContains.setTipo("ARTEFATO");
							entryFileDescription.adicionarAtributo(atributoBlockContains);
						}

						if (recordingMode != null) {
							Atributo atributoRecordingMode = new Atributo();
							atributoRecordingMode.setTipoAtributo(TipoAtributo.RECORDING_MODE);
							atributoRecordingMode.setValor(recordingMode);
							// atributoRecordingMode.setTipo("STRING");
							atributoRecordingMode.setTipo("ARTEFATO");
							entryFileDescription.adicionarAtributo(atributoRecordingMode);
						}

						if (dataRecord != null) {
							Atributo atributoDataRecord = new Atributo();
							atributoDataRecord.setTipoAtributo(TipoAtributo.DATA_RECORD);
							atributoDataRecord.setValor(dataRecord);
							// atributoDataRecord.setTipo("STRING");
							atributoDataRecord.setTipo("ARTEFATO");
							entryFileDescription.adicionarAtributo(atributoDataRecord);
						}

					}
				}
			}
			if (!encontrou) {
				System.out.println("Não encontrou relação DATA DIVSION e FILE CONTROL. (" + nome + ") "
						+ this.artefato.getCaminhoArquivo());
			}
		}

		return artefato;
	}

	private Artefato classificarRelacionamento(Artefato artefato) throws Exception {
		if (artefato.getArtefatosRelacionados() != null && artefato.getArtefatosRelacionados().size() > 0) {
			for (Artefato entry : artefato.getArtefatosRelacionados()) {
				if (entry.getArtefatosRelacionados() != null && entry.getArtefatosRelacionados().size() > 0) {
					entry = classificarRelacionamento(entry);
				}

				if (!TipoArtefato.COPYBOOK_VARIAVEL.equals(entry.getTipoArtefato())
						|| !TipoArtefato.JCL_VARIAVEL.equals(entry.getTipoArtefato())
						|| !TipoArtefato.PROGRAMA_COBOL_PARAGRAFO.equals(entry.getTipoArtefato())) {
					continue;
				}

				if (!TipoArtefato.DESCONHECIDO.equals(artefato.getTipoArtefato())
						&& !TipoArtefato.DESCONHECIDO.equals(entry.getTipoArtefato())
						&& !artefato.getTipoArtefato().equals(entry.getTipoArtefato())) {
					entry.setTipoRelacionamento(TipoRelacionamento.INTERFACE);
				}
			}
		}
		return artefato;
	}
}
