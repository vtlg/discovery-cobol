package br.gov.caixa.discovery.core.extratores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.modelos.Atributo;
import br.gov.caixa.discovery.core.tipos.TipoAmbiente;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.tipos.TipoAtributo;
import br.gov.caixa.discovery.core.tipos.TipoRelacionamento;
import br.gov.caixa.discovery.core.utils.ArtefatoHandler;
import br.gov.caixa.discovery.core.utils.Configuracao;
import br.gov.caixa.discovery.core.utils.Patterns;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.SistemaPersistence;
import br.gov.caixa.discovery.ejb.tipos.Tabelas;

public class ExtratorJcl {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private Artefato artefato = null;
	private int deslocamento = 0;
	private int countDeclaracaoSql = 1;

	public ExtratorJcl() {
		super();
	}

	public ExtratorJcl(Artefato artefato, int deslocamento) {
		this.artefato = artefato;
		this.deslocamento = deslocamento;
	}

	public Artefato executa() {
		try {
			this.artefato = processarCodigoCompleto(this.artefato);
			this.artefato = identificarVariaveis(this.artefato);
			this.artefato = separarSteps(this.artefato);
			this.artefato = tratarCodigo(this.artefato);
			this.artefato = tratarSteps(this.artefato);
			this.artefato = atribuirTipoArtefato(this.artefato);
			this.artefato = atribuirAmbiente(this.artefato);
			this.artefato = atribuirSistema(this.artefato);
			this.artefato = substituirReferencias(this.artefato);
			this.artefato = atribuirSistemaTipo(this.artefato);
			this.artefato = classificarRelacionamento(this.artefato);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Erro ao tentar converter " + this.artefato.getCaminhoArquivo(), e);
			e.printStackTrace();
		}

		return artefato;
	}

	private Artefato classificarRelacionamento(Artefato artefato) throws Exception {
		if (artefato.getArtefatosRelacionados() != null && artefato.getArtefatosRelacionados().size() > 0) {

			Matcher m_dsn_cardlib = null;
			Matcher m_dsn_connect = null;
			Matcher m_dsn_connect_padrao_2 = null;

			for (Artefato entry : artefato.getArtefatosRelacionados()) {
				if (entry.getArtefatosRelacionados() != null && entry.getArtefatosRelacionados().size() > 0) {
					entry = classificarRelacionamento(entry);
				}

				// Descarta os artefatos que não serão incluídos no banco de dados
				if (TipoArtefato.COPYBOOK_VARIAVEL.equals(entry.getTipoArtefato())
						|| TipoArtefato.JCL_VARIAVEL.equals(entry.getTipoArtefato())
						|| TipoArtefato.PROGRAMA_COBOL_PARAGRAFO.equals(entry.getTipoArtefato())) {
					continue;
				}

				if (!TipoArtefato.DESCONHECIDO.equals(artefato.getTipoArtefato())
						&& !TipoArtefato.DESCONHECIDO.equals(entry.getTipoArtefato())
						&& !"DESCONHECIDO".equals(entry.getSistema())
						&& !artefato.getSistema().equals(entry.getSistema())) {
					entry.setTipoRelacionamento(TipoRelacionamento.INTERFACE);
				} else if (TipoArtefato.DSN.equals(entry.getTipoArtefato()) && entry.getNome().startsWith("CNT")) {
					m_dsn_connect = Patterns.JCL_P_DSN_CONNECT.matcher(entry.getNome());
					m_dsn_connect_padrao_2 = Patterns.JCL_P_DSN_CONNECT_PADRAO_2.matcher(entry.getNome());
					if (m_dsn_connect.matches()) {
						String sistemaOrigem = m_dsn_connect.group("sistemaOrigem");
						String sistemaDestino = m_dsn_connect.group("sistemaDestino");

						Set<String> chavesMapa = Configuracao.MAPA_DE_PARA.keySet();

						String novoSistemaOrigem = Configuracao.MAPA_DE_PARA.get(sistemaOrigem);

						for (String chave : chavesMapa) {
							if (sistemaDestino.toUpperCase().startsWith(chave.toUpperCase())) {
								String novoSistemaDestino = Configuracao.MAPA_DE_PARA.get(chave);

								if (novoSistemaOrigem != null) {
									entry.setSistema(novoSistemaOrigem);
								} else {
									entry.setSistema("SI" + sistemaOrigem);
								}

								entry.setTipoRelacionamento(TipoRelacionamento.INTERFACE);

								Atributo atributo = new Atributo();
								atributo.setValor(novoSistemaDestino);
								atributo.setTipoAtributo(TipoAtributo.SISTEMA_DESTINO);
								atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());

								entry.adicionarAtributo(atributo);
							}
						}
					} else if (m_dsn_connect_padrao_2.matches()) {
						String sistemaOrigem = m_dsn_connect_padrao_2.group("sistemaOrigem");
						String sistemaDestino = m_dsn_connect_padrao_2.group("sistemaDestino");

						Set<String> chavesMapa = Configuracao.MAPA_DE_PARA.keySet();

						String novoSistemaOrigem = Configuracao.MAPA_DE_PARA.get(sistemaOrigem);

						for (String chave : chavesMapa) {
							if (sistemaDestino.toUpperCase().startsWith(chave.toUpperCase())) {
								String novoSistemaDestino = Configuracao.MAPA_DE_PARA.get(chave);

								if (novoSistemaOrigem != null) {
									entry.setSistema(novoSistemaOrigem);
								} else {
									entry.setSistema("SI" + sistemaOrigem);
								}

								entry.setTipoRelacionamento(TipoRelacionamento.INTERFACE);

								Atributo atributo = new Atributo();
								atributo.setValor(novoSistemaDestino);
								atributo.setTipoAtributo(TipoAtributo.SISTEMA_DESTINO);
								atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());

								entry.adicionarAtributo(atributo);
							}
						}
					}

				} else if (TipoArtefato.DSN.equals(entry.getTipoArtefato())) {
					m_dsn_cardlib = Patterns.JCL_P_DSN_CARDLIB.matcher(entry.getNome());

					if (entry.getNome().startsWith("SIPUB") || entry.getNome().startsWith("SISEL")) {
						// entry.setSistema("SIGDB");
						entry.setSistema(this.artefato.getSistema());
						entry.setTipoRelacionamento(TipoRelacionamento.INTERFACE);

						Atributo atributo = new Atributo();
						atributo.setValor("SIGDB");
						atributo.setTipoAtributo(TipoAtributo.SISTEMA_DESTINO);
						atributo.setTipo(Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());

						entry.adicionarAtributo(atributo);

					} else if (!entry.getNome().startsWith("CND") && !entry.getNome().startsWith("DB2")
							&& !entry.getNome().startsWith("V01") && !entry.getNome().startsWith("&&")
							&& !entry.getNome().startsWith("COD") && !entry.getNome().startsWith("CEE")
							&& !entry.getNome().startsWith("JV2") && !entry.getNome().startsWith("END")
							&& !entry.getNome().startsWith("%%") && !entry.getNome().startsWith("CNT")
							&& !entry.getNome().startsWith("IBM") && !entry.getNome().startsWith("PRD")
							&& !entry.getNome().startsWith("MG3") && !m_dsn_cardlib.matches()
							&& !entry.getNome().startsWith("TCP") && !entry.getNome().startsWith("SUP") 
							&& !entry.getNome().startsWith("SMS.PRD") ) {

						String siglaSistemaDsn = entry.getNome().substring(0, 3);
						String siglaSistemaPai = this.artefato.getSistema();

						String novoSistemaOrigem = Configuracao.MAPA_DE_PARA.get(siglaSistemaDsn);
						if (novoSistemaOrigem != null) {
							entry.setSistema(novoSistemaOrigem);

							if (!entry.getSistema().equals(siglaSistemaPai)) {
								entry.setTipoRelacionamento(TipoRelacionamento.INTERFACE);
							}
						} else if (!siglaSistemaPai.equals("SI" + siglaSistemaDsn)) {
							entry.setSistema("SI" + siglaSistemaDsn);
							entry.setTipoRelacionamento(TipoRelacionamento.INTERFACE);
						}
					}
				}

			}
		}
		return artefato;
	}

	private Artefato processarCodigoCompleto(Artefato artefato) throws Exception {
		boolean inicioJcl = false;

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

			if ((texto != null) && (texto.length() > (deslocamento))
					&& (texto.substring(deslocamento).startsWith("//"))) {
				inicioJcl = true;
			}

			if (!inicioJcl || texto.startsWith("**")) {
			} else if ((texto != null) && (texto.length() > (deslocamento)) && (!texto.trim().equals("/*"))
					&& (texto.trim().length() != 2) && (texto.trim().length() != 1)
					&& (texto.substring(deslocamento, deslocamento + 3).equals("//*")
							|| texto.substring(deslocamento, deslocamento + 3).equals("/* "))) {

				artefato.adicionarComentario(texto.substring(deslocamento));
			} else {
				artefato.adicionarCodigoFonte(texto.substring(deslocamento));
			}
		}
		return artefato;
	}

	private Artefato identificarVariaveis(Artefato artefato) throws Exception {
		Matcher m_jcl_variavel = null;

		for (String texto : artefato.getCodigoCompleto()) {
			m_jcl_variavel = Patterns.JCL_P_VARIAVEL.matcher(texto);

			if (m_jcl_variavel.matches()) {
				String nomeVariavel = m_jcl_variavel.group("variavel");
				String valorVariavel = m_jcl_variavel.group("valor");

				Artefato artefatoVariavel = new Artefato();
				artefatoVariavel.setNome(nomeVariavel.trim());
				artefatoVariavel.setTipoArtefato(TipoArtefato.JCL_VARIAVEL);
				artefatoVariavel.setAmbiente(TipoAmbiente.DESCONHECIDO);
				artefatoVariavel.setSistema("DESCONHECIDO");

				Atributo atributoValorPadrao = new Atributo(TipoAtributo.VALOR_PADRAO, valorVariavel.trim(), "",
						"ARTEFATO");

				artefatoVariavel.adicionarAtributo(atributoValorPadrao);

				this.artefato.adicionarArtefatosRelacionados(artefatoVariavel);

			}

		}

		return artefato;
	}

	private Artefato separarSteps(Artefato artefato) throws Exception {

		Matcher m_pgm_1 = null;
		Matcher m_proc_1 = null;
		Matcher m_exec_1 = null;
		Matcher m_jobname_1 = null;

		Artefato artefatoStep = null;
		String jobName = null;
		String nomeStep = null;
		String nomePgmProc = null;
		String tipoExec = null;
		int posicaoLinha = 0;
		Long contadorNomeRepetido = 0L;

		List<String> listaNomeStep = new ArrayList<>();

		// "^.*[/]{1,2}(?<nomeStep>[\\S]{1,})[\\s]{1,}EXEC[\\s]{1,}PROC=(?<programa>[\\S&&[^,]]{1,})[\\s]{0,}.*$");

		for (String texto : this.artefato.getCodigoFonte()) {
			posicaoLinha++;

			m_pgm_1 = Patterns.JCL_P_PGM_1.matcher(texto);
			m_proc_1 = Patterns.JCL_P_PROC_1.matcher(texto);
			m_exec_1 = Patterns.JCL_P_EXEC_1.matcher(texto);
			m_jobname_1 = Patterns.JCL_P_JOBNAME.matcher(texto);

			if (m_jobname_1.matches()) {
				jobName = m_jobname_1.group("jobName");
				artefato.setNomeInterno(jobName);
			} else if (m_pgm_1.matches() || m_proc_1.matches() || m_exec_1.matches()) {
				if (artefatoStep != null) {
					artefato.adicionarArtefatosRelacionados(artefatoStep);
				}

				if (m_pgm_1.matches()) {
					nomeStep = m_pgm_1.group("nomeStep");
					nomePgmProc = m_pgm_1.group("programa");
					tipoExec = "EXEC_PGM";
				} else if (m_proc_1.matches()) {
					nomeStep = m_proc_1.group("nomeStep");
					nomePgmProc = m_proc_1.group("programa");
					tipoExec = "EXEC_PROC";
				} else if (m_exec_1.matches()) {
					nomeStep = m_exec_1.group("nomeStep");
					nomePgmProc = m_exec_1.group("programa");
					tipoExec = "EXEC";
				}

				artefatoStep = new Artefato();

				if (nomeStep == null || "".equals(nomeStep.trim())) {
					nomeStep = "!!RASTR_" + posicaoLinha;
				}

				if (listaNomeStep.contains(nomeStep)) {
					nomeStep = nomeStep + "(" + contadorNomeRepetido + ")";
					contadorNomeRepetido++;
				}

				listaNomeStep.add(nomeStep);
				artefatoStep.setAmbiente(artefato.getAmbiente());
				artefatoStep.setSistema(artefato.getSistema());
				artefatoStep.setTipoArtefato(TipoArtefato.JCL_STEP);
				artefatoStep.setNome(nomeStep);
				artefatoStep.setNomeInterno(nomeStep);
				artefatoStep.setPosicao(posicaoLinha);

				// Atributo atributoPosicao = new Atributo();
				// atributoPosicao.setTipoAtributo(TipoAtributo.POSICAO);
				// atributoPosicao.setTipo("LONG");
				// atributoPosicao.setValor(Long.toString(posicaoLinha));
				// artefatoStep.adicionarAtributo(atributoPosicao);

				Atributo atributoExecTipo = new Atributo();
				atributoExecTipo.setTipoAtributo(TipoAtributo.JCL_TIPO_EXEC);
				atributoExecTipo.setTipo("ARTEFATO");
				atributoExecTipo.setValor(tipoExec);

				artefatoStep.adicionarAtributo(atributoExecTipo);

				Atributo atributoNomePgmProc = new Atributo();
				atributoNomePgmProc.setTipoAtributo(TipoAtributo.JCL_NOME_PGM_PROC);
				atributoNomePgmProc.setTipo("ARTEFATO");
				atributoNomePgmProc.setValor(nomePgmProc);

				artefatoStep.adicionarAtributo(atributoNomePgmProc);

			}

			if (artefatoStep != null) {
				artefatoStep.adicionarCodigoFonte(texto);
			}

		}

		if (artefatoStep != null) {
			artefato.adicionarArtefatosRelacionados(artefatoStep);
		}

		return artefato;
	}

	private Artefato tratarCodigo(Artefato artefato) throws Exception {

		Matcher m_dd_inicio_2 = null;

		for (Artefato artefatoStep : artefato.getArtefatosRelacionados()) {
			if (!TipoArtefato.JCL_STEP.equals(artefatoStep.getTipoArtefato())) {
				continue;
			}

			boolean marcadorInicioDD = false;
			StringBuilder sbDD = null;
			for (String texto : artefatoStep.getCodigoFonte()) {

				// m_P_DD_UMA_LINHA = P_DD_UMA_LINHA.matcher(texto.trim());
				// m_P_DD_INICIO = P_DD_INICIO.matcher(texto);
				// m_P_DD_MEIO = P_DD_MEIO.matcher(texto);
				// m_P_DD_FIM = P_DD_FIM.matcher(texto);

				m_dd_inicio_2 = Patterns.JCL_P_DD_INICIO_2.matcher(texto);

				if (m_dd_inicio_2.matches()) {
					if (sbDD != null) {
						// artefatoStep.adicionarCodigoFonteDDTratado(sbDD.toString().replaceAll("[\\s]{2,}",
						// " "));
						artefatoStep.adicionarCodigoFonteTratado(sbDD.toString().replaceAll("[\\s]{2,}", " "));
						sbDD = null;
					}

					sbDD = new StringBuilder();
					sbDD.append(texto);
					sbDD.append(" ");
					marcadorInicioDD = true;
				} else if (marcadorInicioDD) {
					sbDD.append(texto);
					sbDD.append(" ");
				}
			}

			if (sbDD != null) {
				artefatoStep.adicionarCodigoFonteTratado(sbDD.toString().replaceAll("[\\s]{2,}", " "));
				sbDD = null;
			}
		}

		return artefato;
	}

	private Artefato tratarSteps(Artefato artefato) throws Exception {

		for (Artefato artefatoStep : artefato.getArtefatosRelacionados()) {

			if (!TipoArtefato.JCL_STEP.equals(artefatoStep.getTipoArtefato())) {
				continue;
			}

			Atributo atributoExecPgm = artefatoStep.buscaAtributo(TipoAtributo.JCL_NOME_PGM_PROC);

			if (atributoExecPgm != null && atributoExecPgm.getValor().startsWith("IKJEFT")) {
				artefatoStep = dsn(artefatoStep);
				artefatoStep = programa(artefatoStep);
				artefatoStep = incluirExecProgram(artefatoStep);
				artefatoStep = declaracaoSql(artefatoStep);
				artefatoStep = tabelas(artefatoStep);
			} else {
				artefatoStep = dsn(artefatoStep);
				artefatoStep = programa(artefatoStep);
				artefatoStep = incluirExecProgram(artefatoStep);
				artefatoStep = declaracaoSql(artefatoStep);
				artefatoStep = tabelas(artefatoStep);
			}
		}

		return artefato;
	}

	private Artefato dsn(Artefato artefato) throws Exception {

		Matcher m_dsn = null;
		Matcher m_dsn_numero_find = null;
		Matcher m_dsn_numero = null;
		Matcher m_disp_shr = null;
		Matcher m_disp_completo = null;
		Matcher m_dsn_delete = null;
		Matcher m_dsn_delete_parenteses = null;
		// Matcher m_dsn_cardlib = null;
		Matcher m_dsn_listc_entries = null;
		Matcher m_identificador = null;

		String identificador = null;
		int posicaoLinha = 0;
		Artefato artefatoDdname = null;

		for (String texto : artefato.getCodigoFonteTratado()) {
			posicaoLinha++;

			m_dsn = Patterns.JCL_P_DSN.matcher(texto);
			m_identificador = Patterns.JCL_P_IDENTIFICADOR.matcher(texto);
			m_dsn_numero = Patterns.JCL_P_DSN_NUMERO.matcher(texto);
			m_disp_shr = Patterns.JCL_P_DISP_SHR.matcher(texto);
			m_disp_completo = Patterns.JCL_P_DISP_COMPLETO.matcher(texto);
			m_dsn_delete = Patterns.JCL_P_DSN_DELETE.matcher(texto);
			m_dsn_delete_parenteses = Patterns.JCL_P_DSN_DELETE_PARENTESES.matcher(texto);
			m_dsn_listc_entries = Patterns.JCL_P_DSN_LISTC_ENTRIES.matcher(texto);
			m_dsn_numero_find = Patterns.JCL_P_DSN_NUMERO_FIND.matcher(texto);

			Atributo atributoPgmProc = artefato.buscaAtributo(TipoAtributo.JCL_NOME_PGM_PROC);
			String strExecPgmProc = null;
			if (atributoPgmProc != null) {
				strExecPgmProc = atributoPgmProc.getValor();
			}

			if (m_identificador.matches()) {
				String temp = m_identificador.group("identificador");
				if (temp != null && !"".equals(temp.trim()) && !"/".equals(temp.trim())) {
					identificador = m_identificador.group("identificador").trim();

					artefatoDdname = new Artefato();
					// artefatoDdname.setNome(_tratarNomeDsn(identificador));
					artefatoDdname.setNome(identificador);
					artefatoDdname.setNomeInterno(identificador);
					artefatoDdname.setTipoArtefato(TipoArtefato.DDNAME);
					artefatoDdname.setPosicao(artefato.getPosicao() + posicaoLinha);
					artefatoDdname.setSistema(artefato.getSistema());
					artefatoDdname.setAmbiente(artefato.getAmbiente());

					artefato.adicionarArtefatosRelacionados(artefatoDdname);

				}
			}

			if ("IDCAMS".equals(strExecPgmProc) && m_dsn_delete_parenteses.matches()) {
				posicaoLinha++;
				String nomeDsn = m_dsn_delete_parenteses.group("dsn");

				Artefato artefatoDsn = new Artefato();
				// artefatoDsn.setAmbiente(this.artefato.getAmbiente());
				// artefatoDsn.setSistema(this.artefato.getSistema());
				// artefatoDsn.setIdentificador(identificador);
				artefatoDsn.setNome(_tratarNomeDsn(nomeDsn));
				artefatoDsn.setNomeInterno(_tratarNomeDsn(nomeDsn));
				artefatoDsn.setTipoArtefato(TipoArtefato.DSN);
				artefatoDsn.setPosicao(artefato.getPosicao() + posicaoLinha);

				Atributo atributoDeleteOnJcl = new Atributo();
				atributoDeleteOnJcl.setTipoAtributo(TipoAtributo.JCL_DELETE_ON_JCL);
				atributoDeleteOnJcl.setValor("true");
				atributoDeleteOnJcl.setTipo("RELACIONAMENTO");

				artefatoDsn.adicionarAtributo(atributoDeleteOnJcl);

				// artefato.adicionarArtefatosRelacionados(artefatoDsn);
				artefatoDdname.adicionarArtefatosRelacionados(artefatoDsn);

			} else if ("IDCAMS".equals(strExecPgmProc) && m_dsn_delete.matches()) {
				posicaoLinha++;
				Matcher matcher = Patterns.JCL_P_DELETE_DSN.matcher(texto);

				matcher.matches();
				while (matcher.find()) {
					String nomeDsn = matcher.group("dsn");

					Artefato artefatoDsn = new Artefato();
					// artefatoDsn.setAmbiente(this.artefato.getAmbiente());
					// artefatoDsn.setSistema(this.artefato.getSistema());
					// artefatoDsn.setIdentificador(identificador);
					artefatoDsn.setNome(_tratarNomeDsn(nomeDsn));
					artefatoDsn.setNomeInterno(_tratarNomeDsn(nomeDsn));
					artefatoDsn.setTipoArtefato(TipoArtefato.DSN);
					artefatoDsn.setPosicao(artefato.getPosicao() + posicaoLinha);

					Atributo atributoDeleteOnJcl = new Atributo();
					atributoDeleteOnJcl.setTipoAtributo(TipoAtributo.JCL_DELETE_ON_JCL);
					atributoDeleteOnJcl.setValor("true");
					atributoDeleteOnJcl.setTipo("RELACIONAMENTO");

					artefatoDsn.adicionarAtributo(atributoDeleteOnJcl);

					// artefato.adicionarArtefatosRelacionados(artefatoDsn);
					artefatoDdname.adicionarArtefatosRelacionados(artefatoDsn);
				}
			}

			if (m_dsn_listc_entries.matches() && "IDCAMS".equals(strExecPgmProc)) {
				posicaoLinha++;
				Artefato artefatoDsn = new Artefato();
				String nomeDsn = m_dsn_listc_entries.group("dsn");

				nomeDsn = _tratarNomeDsn(nomeDsn);

				artefatoDsn.setNome(nomeDsn);
				artefatoDsn.setNomeInterno(_tratarNomeDsn(nomeDsn));
				// artefatoDsn.setAmbiente(this.artefato.getAmbiente());
				// artefatoDsn.setSistema(this.artefato.getSistema());
				// artefatoDsn.setIdentificador(identificador);
				artefatoDsn.setTipoArtefato(TipoArtefato.DSN);
				// dsn.setIdentificador(identificadorDsn);
				artefatoDsn.setPosicao(artefato.getPosicao() + posicaoLinha);

				Atributo atributoDispStatus = new Atributo();
				atributoDispStatus.setTipoAtributo(TipoAtributo.JCL_DISP_STATUS);
				atributoDispStatus.setValor("SHR");
				atributoDispStatus.setTipo("RELACIONAMENTO");

				artefatoDsn.adicionarAtributo(atributoDispStatus);

				Atributo atributoNormalDisposition = new Atributo();
				atributoNormalDisposition.setTipoAtributo(TipoAtributo.JCL_DISP_NORMAL_DISPOSITION);
				atributoNormalDisposition.setValor("KEEP");
				atributoNormalDisposition.setTipo("RELACIONAMENTO");

				artefatoDsn.adicionarAtributo(atributoNormalDisposition);

				Atributo atributoAbnormalDisposition = new Atributo();
				atributoAbnormalDisposition.setTipoAtributo(TipoAtributo.JCL_DISP_ABNORMAL_DISPOSITION);
				atributoAbnormalDisposition.setValor("KEEP");
				atributoAbnormalDisposition.setTipo("RELACIONAMENTO");

				artefatoDsn.adicionarAtributo(atributoAbnormalDisposition);

				// artefato.adicionarArtefatosRelacionados(artefatoDsn);
				artefatoDdname.adicionarArtefatosRelacionados(artefatoDsn);

			}

			// **AQUI

			if (m_dsn_numero.matches()) {

				while (m_dsn_numero_find.find()) {
					posicaoLinha++;
					String nomeDsn = m_dsn_numero_find.group("dsn");

					Artefato artefatoDsn = new Artefato();
					// artefatoDsn.setAmbiente(this.artefato.getAmbiente());
					// artefatoDsn.setSistema(this.artefato.getSistema());
					// artefatoDsn.setIdentificador(identificador);
					artefatoDsn.setNome(_tratarNomeDsn(nomeDsn));
					artefatoDsn.setNomeInterno(_tratarNomeDsn(nomeDsn));
					artefatoDsn.setTipoArtefato(TipoArtefato.DSN);
					artefatoDsn.setPosicao(artefato.getPosicao() + posicaoLinha);

					Atributo atributoDispStatus = new Atributo();
					atributoDispStatus.setTipoAtributo(TipoAtributo.JCL_DISP_STATUS);
					atributoDispStatus.setValor("SHR");
					atributoDispStatus.setTipo("RELACIONAMENTO");

					artefatoDsn.adicionarAtributo(atributoDispStatus);

					Atributo atributoNormalDisposition = new Atributo();
					atributoNormalDisposition.setTipoAtributo(TipoAtributo.JCL_DISP_NORMAL_DISPOSITION);
					atributoNormalDisposition.setValor("KEEP");
					atributoNormalDisposition.setTipo("RELACIONAMENTO");

					artefatoDsn.adicionarAtributo(atributoNormalDisposition);

					Atributo atributoAbnormalDisposition = new Atributo();
					atributoAbnormalDisposition.setTipoAtributo(TipoAtributo.JCL_DISP_ABNORMAL_DISPOSITION);
					atributoAbnormalDisposition.setValor("KEEP");
					atributoAbnormalDisposition.setTipo("RELACIONAMENTO");

					artefatoDsn.adicionarAtributo(atributoAbnormalDisposition);

					// artefato.adicionarArtefatosRelacionados(artefatoDsn);
					artefatoDdname.adicionarArtefatosRelacionados(artefatoDsn);
				}

			} else if (m_dsn.matches()) {
				posicaoLinha++;
				String nomeDsn = m_dsn.group("dsn");
				String dispStatus = "NEW";
				String dispNormalDisposition = "DELETE";
				String dispAbormalDisposition = "DELETE";

				if (m_disp_shr.matches()) {
					dispStatus = "SHR";
					dispNormalDisposition = "KEEP";
					dispAbormalDisposition = "KEEP";
				}

				if (m_disp_completo.matches()) {
					posicaoLinha++;
					String parametro = m_disp_completo.group("parametros");
					String[] arrParametro = parametro.split(",");

					switch (arrParametro.length) {
					case 3:
						dispAbormalDisposition = arrParametro[2];
					case 2:
						dispNormalDisposition = arrParametro[1];
					case 1:
						dispStatus = arrParametro[0];
						if (dispStatus == null || "".equals(dispStatus)) {
							dispStatus = "NEW";
						}
						break;
					case 0:
						break;
					default:
						break;
					}
				}

				Artefato artefatoDsn = new Artefato();
				// artefatoDsn.setAmbiente(this.artefato.getAmbiente());
				// artefatoDsn.setSistema(this.artefato.getSistema());
				// artefatoDsn.setIdentificador(identificador);
				artefatoDsn.setNome(_tratarNomeDsn(nomeDsn));
				artefatoDsn.setNomeInterno(_tratarNomeDsn(nomeDsn));
				artefatoDsn.setTipoArtefato(TipoArtefato.DSN);
				artefatoDsn.setPosicao(artefato.getPosicao() + posicaoLinha);

				Atributo atributoDispStatus = new Atributo();
				atributoDispStatus.setTipoAtributo(TipoAtributo.JCL_DISP_STATUS);
				atributoDispStatus.setValor(dispStatus);
				atributoDispStatus.setTipo("RELACIONAMENTO");

				artefatoDsn.adicionarAtributo(atributoDispStatus);

				Atributo atributoNormalDisposition = new Atributo();
				atributoNormalDisposition.setTipoAtributo(TipoAtributo.JCL_DISP_NORMAL_DISPOSITION);
				atributoNormalDisposition.setValor(dispNormalDisposition);
				atributoNormalDisposition.setTipo("RELACIONAMENTO");

				artefatoDsn.adicionarAtributo(atributoNormalDisposition);

				Atributo atributoAbnormalDisposition = new Atributo();
				atributoAbnormalDisposition.setTipoAtributo(TipoAtributo.JCL_DISP_ABNORMAL_DISPOSITION);
				atributoAbnormalDisposition.setValor(dispAbormalDisposition);
				atributoAbnormalDisposition.setTipo("RELACIONAMENTO");

				artefatoDsn.adicionarAtributo(atributoAbnormalDisposition);

				// artefato.adicionarArtefatosRelacionados(artefatoDsn);
				artefatoDdname.adicionarArtefatosRelacionados(artefatoDsn);
			}
		}

		return artefato;
	}

	private Artefato programa(Artefato artefato) throws Exception {

		Matcher m_run_programa_1 = null;
		Matcher m_submit_proc_1 = null;

		int posicaoLinha = 0;

		for (String texto : artefato.getCodigoFonte()) {
			posicaoLinha++;
			m_run_programa_1 = Patterns.JCL_P_RUN_PROGRAMA_1.matcher(texto);
			m_submit_proc_1 = Patterns.JCL_P_SUBMIT_PROC_1.matcher(texto);

			if (m_run_programa_1.matches()) {

				String nomePrograma = m_run_programa_1.group("programa");
				// TipoArtefato tipoArtefato =
				// ArtefatoHandler.identificarTipoArtefato(nomePrograma.trim());

				Artefato programa = new Artefato();

				programa.setNome(nomePrograma.trim());
				programa.setNomeInterno(nomePrograma.trim());
				// programa.setTipoArtefato(tipoArtefato);
				// programa.setAmbiente(this.artefato.getAmbiente());
				// programa.setSistema(ArtefatoHandler.identificarSistema(nomePrograma,
				// tipoArtefato));
				programa.setPosicao(artefato.getPosicao() + posicaoLinha);

				artefato.adicionarArtefatosRelacionados(programa);
			}

			if (m_submit_proc_1.matches()) {

				String nomePrograma = m_submit_proc_1.group("programa");
				// TipoArtefato tipoArtefato =
				// ArtefatoHandler.identificarTipoArtefato(nomePrograma.trim());

				Artefato programa = new Artefato();

				programa.setNome(nomePrograma.trim());
				programa.setNomeInterno(nomePrograma.trim());
				// programa.setAmbiente(this.artefato.getAmbiente());
				// programa.setTipoArtefato(tipoArtefato);
				// programa.setSistema(ArtefatoHandler.identificarSistema(nomePrograma,
				// tipoArtefato));
				programa.setPosicao(artefato.getPosicao() + posicaoLinha);

				artefato.adicionarArtefatosRelacionados(programa);
			}
		}

		return artefato;
	}

	private Artefato incluirExecProgram(Artefato artefato) throws Exception {

		Atributo atributoExcPgmProc = artefato.buscaAtributo(TipoAtributo.JCL_NOME_PGM_PROC);

		// TipoArtefato tipoArtefato =
		// ArtefatoHandler.identificarTipoArtefato(atributoExcPgmProc.getValor());

		Artefato programa = new Artefato();
		programa.setNome(atributoExcPgmProc.getValor());
		programa.setNomeInterno(atributoExcPgmProc.getValor());
		// programa.setTipoArtefato(tipoArtefato);
		// programa.setAmbiente(this.artefato.getAmbiente());
		programa.setPosicao(artefato.getPosicao());

		// if (tipoArtefato != null) {
		// programa.setSistema(ArtefatoHandler.identificarSistema(atributoExcPgmProc.getValor(),
		// tipoArtefato));
		// } else {
		// programa.setSistema(this.artefato.getSistema());
		// }

		artefato.adicionarArtefatosRelacionados(programa);

		return artefato;
	}

	private Artefato declaracaoSql(Artefato artefato) throws Exception {

		Matcher m_Insert_1_Inicio = null;
		Matcher m_Insert_1_Fim = null;

		Matcher m_Delete_1_Inicio = null;
		Matcher m_Delete_1_Fim = null;

		Matcher m_Select_1_Inicio = null;
		Matcher m_Select_1_Fim = null;

		// Artefato programa = null;
		Artefato artefatoDeclaracaoSql = null;
		boolean marcador = false;
		StringBuilder sb = new StringBuilder();
		int posicaoLinha = 0;

		for (String texto : artefato.getCodigoFonte()) {
			posicaoLinha++;

			m_Insert_1_Inicio = Patterns.JCL_P_INSERT_1_INICIO.matcher(texto);
			m_Insert_1_Fim = Patterns.JCL_P_INSERT_1_FIM.matcher(texto);

			m_Delete_1_Inicio = Patterns.JCL_P_DELETE_1_INICIO.matcher(texto);
			m_Delete_1_Fim = Patterns.JCL_P_DELETE_1_FIM.matcher(texto);

			m_Select_1_Inicio = Patterns.JCL_P_SELECT_1_INICIO.matcher(texto);
			m_Select_1_Fim = Patterns.JCL_P_SELECT_1_FIM.matcher(texto);

			if (m_Insert_1_Inicio.matches()) {
				sb = new StringBuilder();
				marcador = true;
			}

			if (m_Delete_1_Inicio.matches()) {
				sb = new StringBuilder();
				marcador = true;
			}

			if (m_Select_1_Inicio.matches()) {
				sb = new StringBuilder();
				marcador = true;
			}

			if (marcador) {
				sb.append(texto);
			}

			if ((m_Insert_1_Fim.matches() || m_Delete_1_Fim.matches() || m_Select_1_Fim.matches()) && marcador) {
				artefatoDeclaracaoSql = new Artefato();

				artefatoDeclaracaoSql.setNome("DECLARACAO-SQL-" + countDeclaracaoSql);
				artefatoDeclaracaoSql.setNomeInterno("DECLARACAO-SQL-" + countDeclaracaoSql);
				artefatoDeclaracaoSql.setAmbiente(this.artefato.getAmbiente());
				artefatoDeclaracaoSql.setTipoArtefato(TipoArtefato.DECLARACAO_SQL);
				artefatoDeclaracaoSql.setAmbiente(this.artefato.getAmbiente());
				artefatoDeclaracaoSql.setSistema(this.artefato.getSistema());
				artefatoDeclaracaoSql.setPosicao(posicaoLinha);

				// atributo.setDeclaracaoCompleta(sb.toString().replaceAll("[\\s]{2,}", "
				// ").trim());
				Atributo atributoDeclaracaoCompleta = new Atributo();
				// atributoDeclaracaoCompleta.setTipo("STRING");
				atributoDeclaracaoCompleta.setTipo("ARTEFATO");
				atributoDeclaracaoCompleta.setTipoAtributo(TipoAtributo.DECLARACAO_COMPLETA);
				atributoDeclaracaoCompleta.setValor(sb.toString().replaceAll("[\\s]{2,}", " ").trim());

				artefatoDeclaracaoSql.adicionarAtributo(atributoDeclaracaoCompleta);

				artefato.adicionarArtefatosRelacionados(artefatoDeclaracaoSql);
				countDeclaracaoSql++;

				marcador = false;
			}
		}

		return artefato;
	}

	private Artefato tabelas(Artefato artefato) throws Exception {

		Matcher m_Insert = null;
		Matcher m_Delete = null;
		Matcher m_Select = null;
		Matcher m_Join = null;

		List<Artefato> tempLista = new ArrayList<>();

		for (Artefato artefatoDeclaracaoSql : artefato.getArtefatosRelacionados()) {
			int posicao = artefatoDeclaracaoSql.getPosicao() + 1;
			if (!TipoArtefato.DECLARACAO_SQL.equals(artefatoDeclaracaoSql.getTipoArtefato())) {
				continue;
			}

			Atributo declaracaoCompleta = artefatoDeclaracaoSql.buscaAtributo(TipoAtributo.DECLARACAO_COMPLETA);
			String texto = declaracaoCompleta.getValor();

			m_Insert = Patterns.JCL_P_INSERT.matcher(texto);
			m_Delete = Patterns.JCL_P_DELETE.matcher(texto);
			m_Select = Patterns.JCL_P_SELECT.matcher(texto);
			m_Join = Patterns.JCL_P_JOIN.matcher(texto);

			if (m_Insert.matches()) {
				posicao++;
				String nomeTabela = m_Insert.group("tabela");
				nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1);

				if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
						nomeTabela)) {
					Artefato artefatoTabela = new Artefato();
					artefatoTabela.setNome(nomeTabela);
					artefatoTabela.setNomeInterno(nomeTabela);
					// artefatoTabela.setAmbiente(this.artefato.getAmbiente());
					artefatoTabela.setTipoArtefato(TipoArtefato.TABELA);
					// artefatoTabela.setSistema(ArtefatoHandler.identificarSistema(nomeTabela,
					// TipoArtefato.TABELA));
					artefatoTabela.setPosicao(posicao);

					// artefato.adicionarArtefatosRelacionados(artefatoTabela);
					tempLista.add(artefatoTabela);
				}
			}

			if (m_Delete.matches()) {
				posicao++;
				String nomeTabela = m_Delete.group("tabela");
				nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1);

				if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
						nomeTabela)) {
					Artefato artefatoTabela = new Artefato();
					artefatoTabela.setNome(nomeTabela);
					artefatoTabela.setNomeInterno(nomeTabela);
					// artefatoTabela.setAmbiente(this.artefato.getAmbiente());
					artefatoTabela.setTipoArtefato(TipoArtefato.TABELA);
					// artefatoTabela.setSistema(ArtefatoHandler.identificarSistema(nomeTabela,
					// TipoArtefato.TABELA));
					artefatoTabela.setPosicao(posicao);
					// artefato.adicionarArtefatosRelacionados(artefatoTabela);
					tempLista.add(artefatoTabela);
				}
			}

			if (m_Select.matches()) {
				posicao++;
				String nomeTabela = m_Select.group("tabela");

				nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1);
				nomeTabela = nomeTabela.replace(";", "");
				nomeTabela = nomeTabela.trim();

				if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
						nomeTabela)) {
					Artefato artefatoTabela = new Artefato();
					artefatoTabela.setNome(nomeTabela);
					artefatoTabela.setNomeInterno(nomeTabela);
					// artefatoTabela.setAmbiente(this.artefato.getAmbiente());
					artefatoTabela.setTipoArtefato(TipoArtefato.TABELA);
					// artefatoTabela.setSistema(ArtefatoHandler.identificarSistema(nomeTabela,
					// TipoArtefato.TABELA));
					artefatoTabela.setPosicao(posicao);
					// artefato.adicionarArtefatosRelacionados(artefatoTabela);
					tempLista.add(artefatoTabela);
				}
			}

			while (m_Join.find()) {
				posicao++;
				String nomeTabela = m_Join.group("tabela");
				nomeTabela = nomeTabela.substring(nomeTabela.indexOf(".") + 1);

				if (!ArtefatoHandler.existeArtefato(artefato.getArtefatosRelacionados(), TipoArtefato.TABELA,
						nomeTabela)) {
					Artefato artefatoTabela = new Artefato();
					artefatoTabela.setNome(nomeTabela);
					artefatoTabela.setNomeInterno(nomeTabela);
					// artefatoTabela.setAmbiente(this.artefato.getAmbiente());
					artefatoTabela.setTipoArtefato(TipoArtefato.TABELA);
					// artefatoTabela.setSistema(ArtefatoHandler.identificarSistema(nomeTabela,
					// TipoArtefato.TABELA));
					artefatoTabela.setPosicao(posicao);
					// artefato.adicionarArtefatosRelacionados(artefatoTabela);
					tempLista.add(artefatoTabela);
				}
			}
		}

		artefato.adicionarArtefatosRelacionados(tempLista);

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

			if ("DESCONHECIDO".equals(entry.getSistema()) && !TipoArtefato.UTILITARIO.equals(entry.getTipoArtefato())
					&& !entry.getNome().startsWith("%") && !entry.getNome().startsWith("&")) {
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
		if (artefato.getArtefatosRelacionados() == null) {
			return artefato;
		}
		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			atribuirTipoArtefato(entry);
			if (entry.getTipoArtefato() != null && !TipoArtefato.DESCONHECIDO.equals(entry.getTipoArtefato())) {
				continue;
			}
			TipoArtefato tipoArtefato = ArtefatoHandler.identificarTipoArtefato(entry.getNome());
			entry.setTipoArtefato(tipoArtefato);
		}
		return artefato;
	}

	private Artefato atribuirAmbiente(Artefato artefato) throws Exception {
		if (artefato.getArtefatosRelacionados() == null) {
			return artefato;
		}

		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			atribuirAmbiente(entry);
			if (entry.getAmbiente() != null) {
				continue;
			}

			TipoAmbiente ambiente = ArtefatoHandler.identificarAmbiente(entry.getNome(), entry.getTipoArtefato());
			entry.setAmbiente(ambiente);

		}

		return artefato;
	}

	private Artefato atribuirSistema(Artefato artefato) throws Exception {
		if (artefato.getArtefatosRelacionados() == null) {
			return artefato;
		}

		for (Artefato entry : artefato.getArtefatosRelacionados()) {
			atribuirSistema(entry);
			if (entry.getSistema() != null) {
				continue;
			}

			String sistema = ArtefatoHandler.identificarSistema(entry.getNome(), entry.getTipoArtefato());
			entry.setSistema(sistema);

		}

		return artefato;
	}

	private Artefato substituirReferencias(Artefato artefato) throws Exception {

		List<Artefato> listaVariaveis = this.artefato.getArtefatosRelacionados().stream()
				.filter((p) -> TipoArtefato.JCL_VARIAVEL.equals(p.getTipoArtefato())).collect(Collectors.toList());

		if (listaVariaveis != null && listaVariaveis.size() > 0) {
			List<Artefato> tempLista = new ArrayList<>();
			tempLista.addAll(artefato.getArtefatosRelacionados());

			for (Artefato entry : artefato.getArtefatosRelacionados()) {
				if (TipoArtefato.JCL_VARIAVEL.equals(entry.getTipoArtefato())) {
					continue;
				}

				if (entry.getArtefatosRelacionados() != null && entry.getArtefatosRelacionados().size() > 0) {
					substituirReferencias(entry);
				}

				for (Artefato variavel : listaVariaveis) {

					Atributo atributoValorPadrao = variavel.getAtributos().get(0);
					String nomeArtefato = entry.getNome();
					if (nomeArtefato.contains(variavel.getNome() + "%%..")) {
						nomeArtefato = nomeArtefato.replace(variavel.getNome() + "%%..",
								atributoValorPadrao.getValor() + ".");
					} else if (nomeArtefato.contains(variavel.getNome() + "%%")) {
						nomeArtefato = nomeArtefato.replace(variavel.getNome() + "%%", atributoValorPadrao.getValor());
					} else if (nomeArtefato.contains(variavel.getNome() + "..")) {
						nomeArtefato = nomeArtefato.replace(variavel.getNome() + "..",
								atributoValorPadrao.getValor() + ".");
					} else if (nomeArtefato.contains(variavel.getNome())) {
						nomeArtefato = nomeArtefato.replace(variavel.getNome(), atributoValorPadrao.getValor());
					} else {
						nomeArtefato = null;
					}

					if (nomeArtefato != null) {
						entry.setExcluir(true);
						Artefato novoArtefato = entry.copiar();

						if (TipoArtefato.DSN.equals(novoArtefato.getTipoArtefato())) {
							novoArtefato.setNome(_tratarNomeDsn(nomeArtefato));
						} else {
							novoArtefato.setNome(nomeArtefato);
						}

						if (!_existeArtefato(tempLista, novoArtefato)) {
							tempLista.add(novoArtefato);
						}
					}
				}
			}

			List<Artefato> novaListaRelacionados = new ArrayList<>();
			novaListaRelacionados = tempLista.stream().filter((p) -> p.isExcluir() == false)
					.collect(Collectors.toList());
			artefato.setArtefatosRelacionados(novaListaRelacionados);

		}
		return artefato;
	}

	private boolean _existeArtefato(List<Artefato> lista, Artefato artefato) {

		for (Artefato entry : lista) {
			if (entry.getNome().equals(artefato.getNome())
					&& entry.getTipoArtefato().equals(artefato.getTipoArtefato())) {
				return true;
			}
		}

		return false;
	}

	private String _tratarNomeDsn(String nomeDsn) throws Exception {

		String valorSaida = nomeDsn;
		valorSaida = nomeDsn.replace("%%ALIAS%%.", "");

		Matcher m_nome_tira_data_fixa = Patterns.JCL_P_NOME_TIRA_DATA_FIXA.matcher(valorSaida);

		if (m_nome_tira_data_fixa.matches()) {
			valorSaida = m_nome_tira_data_fixa.group("nome") + ".D%%ODATE";
		}

		// Matcher m_nome_tira_ambiente_fixo =
		// Patterns.JCL_P_NOME_TIRA_AMBIENTE_FIXO.matcher(valorSaida);

		if ((valorSaida.startsWith("COD") || valorSaida.startsWith("PRE") || valorSaida.startsWith("REL")
				|| valorSaida.startsWith("DES") || valorSaida.startsWith("TQS") || valorSaida.startsWith("HMP")
				|| valorSaida.startsWith("PRD") || valorSaida.startsWith("PRD0")) && !valorSaida.startsWith("PRD.V0")) {
			valorSaida = valorSaida.substring(4);
		}
		
		
		
		

		if (valorSaida.startsWith(".")) {
			valorSaida = valorSaida.substring(1);
		}

		if (valorSaida.endsWith("D%%CALCDATE %%ODATE -1")) {
			valorSaida = valorSaida.replace("D%%CALCDATE %%ODATE -1", "D%%ODATE");
		}
		//

		return valorSaida;
	}
}
