package br.gov.caixa.discovery.injetores;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;

import javax.swing.text.DefaultStyledDocument.AttributeUndoableEdit;

import br.gov.caixa.discovery.core.extratores.Extrator;
import br.gov.caixa.discovery.core.modelos.ArquivoConfiguracao;
import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.modelos.ArtefatoSorted;
import br.gov.caixa.discovery.core.modelos.Atributo;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.tipos.TipoAtributo;
import br.gov.caixa.discovery.core.tipos.TipoRelacionamento;
import br.gov.caixa.discovery.core.utils.Configuracao;
import br.gov.caixa.discovery.core.utils.Patterns;
import br.gov.caixa.discovery.core.utils.UtilsHandler;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoRelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.tipos.Tabelas;

public class Conversor {

	private static ExecutorService executor = Executors.newFixedThreadPool(Configuracao.NUMERO_THREAD);

	@SuppressWarnings("unused")
	public static List<ArtefatoPersistence> executar() {
		return executar(false);
	}

	@SuppressWarnings("unused")
	public static List<ArtefatoPersistence> executar(boolean controlM) {
		List<ArtefatoPersistence> listaOutput = new ArrayList<>();

		List<Callable<List<ArtefatoPersistence>>> listaThread = new ArrayList<>();

		if (!controlM && true) {
			List<Callable<List<ArtefatoPersistence>>> threadsCopybook = _criarThreads(
					Configuracao.getConfiguracao(TipoArtefato.COPYBOOK), TipoArtefato.COPYBOOK, 0);
			List<Callable<List<ArtefatoPersistence>>> threadsProgramaCobol = _criarThreads(
					Configuracao.getConfiguracao(TipoArtefato.PROGRAMA_COBOL), TipoArtefato.PROGRAMA_COBOL, 0);
			List<Callable<List<ArtefatoPersistence>>> threadsJcl = _criarThreads(
					Configuracao.getConfiguracao(TipoArtefato.JCL), TipoArtefato.JCL, 0);

			listaThread.addAll(threadsCopybook);
			listaThread.addAll(threadsProgramaCobol);
			listaThread.addAll(threadsJcl);
		} else if (controlM && true) {
			List<Path> listaArquivos = UtilsHandler.recuperarListaArquivo(
					Configuracao.getConfiguracao(TipoArtefato.CONTROL_M).getCaminhoPasta(), false);
			Extrator extrator = new Extrator();
			extrator.inicializar(listaArquivos, TipoArtefato.CONTROL_M);

			List<Artefato> listaArtefato = extrator.converter();

			if (listaArtefato != null) {
				ArtefatoPersistence artefatoPersistence = null;
				HashMap<String, ArtefatoPersistence> map = new HashMap<>();

				for (Artefato entry : listaArtefato) {
					artefatoPersistence = converterArtefato(entry);
					if (map.containsKey(artefatoPersistence.getNoNomeArtefato())) {
						map.get(artefatoPersistence.getNoNomeArtefato()).adicionarRelacionamentoTransient(
								artefatoPersistence.getTransientListaRelacionamentos());
					} else {
						map.put(artefatoPersistence.getNoNomeArtefato(), artefatoPersistence);
					}
				}

				listaOutput.addAll(map.values());

				for (ArtefatoPersistence entry : listaOutput) {
					List<ArtefatoPersistence> listaArtefatos = getListaArtefatos(new ArrayList<>(), entry);

					entry = substituirArtefatos(listaArtefatos, entry);
				}

			}
		}

		listaOutput.addAll(executarConverter(listaThread));
		executor.shutdown();

		return listaOutput;
	}

	private static List<ArtefatoPersistence> getListaArtefatos(List<ArtefatoPersistence> lista,
			ArtefatoPersistence artefato) {

		// for (RelacionamentoPersistence relacionamento :
		// artefato.getTransientListaRelacionamentos()) {
		lista.add(artefato);
		// }

		for (RelacionamentoPersistence relacionamento : artefato.getTransientListaRelacionamentos()) {
			ArtefatoPersistence result = existeArtefato(lista, relacionamento.getArtefato());

			if (result == null) {
				getListaArtefatos(lista, relacionamento.getArtefato());
			}
		}

		return lista;
	}

	private static ArtefatoPersistence substituirArtefatos(List<ArtefatoPersistence> lista,
			ArtefatoPersistence artefatoEntrada) {

		for (RelacionamentoPersistence entry : artefatoEntrada.getTransientListaRelacionamentos()) {
			ArtefatoPersistence artefatoPrimeiro = null;
			ArtefatoPersistence artefatoUltimo = null;
			ArtefatoPersistence artefatoAnterior = null;
			ArtefatoPersistence artefatoPosterior = null;
			ArtefatoPersistence artefato = null;
			ArtefatoPersistence artefatoPai = null;

			if (entry.isIcInclusaoMalha()) {
				artefatoPrimeiro = existeArtefato(lista, entry.getArtefatoPrimeiro());
				artefatoUltimo = existeArtefato(lista, entry.getArtefatoUltimo());
				artefatoAnterior = existeArtefato(lista, entry.getArtefatoAnterior());
				artefatoPosterior = existeArtefato(lista, entry.getArtefatoPosterior());
				artefato = existeArtefato(lista, entry.getArtefato());
				artefatoPai = existeArtefato(lista, entry.getArtefatoPai());
			} else {
				artefatoPrimeiro = existeArtefato(lista, entry.getArtefatoPrimeiro());
				artefatoUltimo = existeArtefato(lista, entry.getArtefatoUltimo());
				artefatoAnterior = existeArtefato(lista, entry.getArtefatoAnterior());
				artefatoPosterior = existeArtefato(lista, entry.getArtefatoPosterior());
				artefato = existeArtefato(lista, entry.getArtefato());
				artefatoPai = existeArtefato(lista, entry.getArtefatoPai());
			}

			entry.setArtefatoPrimeiro(artefatoPrimeiro);
			entry.setArtefatoUltimo(artefatoUltimo);
			entry.setArtefatoAnterior(artefatoAnterior);
			entry.setArtefatoPosterior(artefatoPosterior);
			entry.setArtefato(artefato);
			entry.setArtefatoPai(artefatoPai);

			if (artefatoPai != null) {
				artefato = substituirArtefatos(lista, artefato);
			}
		}

		return artefatoEntrada;
	}

	private static ArtefatoPersistence existeArtefato(List<ArtefatoPersistence> lista, ArtefatoPersistence artefato) {
		if (artefato == null) {
			return null;
		}

		for (ArtefatoPersistence entry : lista) {

//			if (entry.getNoNomeArtefato().equals(artefato.getNoNomeArtefato())
//					&& entry.getCoTipoArtefato().equals(artefato.getCoTipoArtefato())
//					&& entry.getCoSistema().equals(artefato.getCoSistema())
//					&& entry.getCoAmbiente().equals(artefato.getCoAmbiente())) {
			if (entry.getNoNomeArtefato().equals(artefato.getNoNomeArtefato())
					&& entry.getCoTipoArtefato().equals(artefato.getCoTipoArtefato())) {
				return entry;
			}

		}

		return null;
	}

	private static List<ArtefatoPersistence> executarConverter(List<Callable<List<ArtefatoPersistence>>> listaThread) {

		Collection<List<ArtefatoPersistence>> collectionFuture = new ArrayList<>();
		List<ArtefatoPersistence> collectionOutput = new ArrayList<>();
		try {
			executor.invokeAll(listaThread).stream().forEach((future) -> {
				try {
					collectionFuture.add(future.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
		}

		if (collectionFuture != null && collectionFuture.size() > 0) {
			for (List<ArtefatoPersistence> entry : collectionFuture) {
				collectionOutput.addAll(entry);

			}
		}

		return collectionOutput;
	}

	private static List<Callable<List<ArtefatoPersistence>>> _criarThreads(ArquivoConfiguracao arquivoConfiguracao,
			TipoArtefato tipoArtefato, int deslocamento) {

		if (arquivoConfiguracao == null) {
			System.out.println("Arquivo Configuração é null!");
			return null;
		}

		String caminhoPasta = arquivoConfiguracao.getCaminhoPasta();

		List<Path> listaArquivos = null;
		try {
			listaArquivos = UtilsHandler.recuperarListaArquivo(caminhoPasta, false);
		} catch (Exception e) {
			return null;
		}

		List<Callable<List<ArtefatoPersistence>>> listaThreads = new ArrayList<Callable<List<ArtefatoPersistence>>>();

		listaArquivos.forEach((path) -> {
			Callable<List<ArtefatoPersistence>> thread = new Callable<List<ArtefatoPersistence>>() {
				@Override
				public List<ArtefatoPersistence> call() throws Exception {
					List<ArtefatoPersistence> listaOutput = new ArrayList<>();
					Extrator extrator = new Extrator();
					extrator.inicializar(path.toAbsolutePath().toString(), tipoArtefato, deslocamento);

					List<Artefato> listaArtefato = extrator.converter();

					if (listaArtefato != null) {
						ArtefatoPersistence artefatoPersistence = null;
						for (Artefato entry : listaArtefato) {
							artefatoPersistence = converterArtefato(entry);
							listaOutput.add(artefatoPersistence);
						}
					}

					return listaOutput;
				}
			};
			listaThreads.add(thread);
		});

		return listaThreads;
	}

	private static ArtefatoPersistence converterArtefato(Artefato artefato) {
		ArtefatoPersistence artefatoPersistence = converterArtefato(artefato, null, null, null);
		List<ArtefatoPersistence> listaArtefatos = getListaArtefatos(new ArrayList<>(), artefatoPersistence);

		if (artefato.isMalhaControlm() == false) {
			artefatoPersistence = substituirArtefatos(listaArtefatos, artefatoPersistence);
		}

		return artefatoPersistence;
	}

	private static ArtefatoPersistence converterArtefato(Artefato artefato, ArtefatoPersistence artefatoPaiEntrada,
			ArtefatoPersistence artefatoAnteriorEntrada, ArtefatoPersistence artefatoPosteriorEntrada) {
		if (artefato == null) {
			return null;
		}

		String nomeArtefato = artefato.getNome();
		String nomeInterno = artefato.getNomeInterno();

		if (TipoArtefato.DECLARACAO_SQL.equals(artefato.getTipoArtefato())
				// || TipoArtefato.DSN.equals(artefato.getTipoArtefato())
				|| TipoArtefato.JCL_STEP.equals(artefato.getTipoArtefato())
				|| TipoArtefato.COPYBOOK_VARIAVEL.equals(artefato.getTipoArtefato())
				|| TipoArtefato.DDNAME.equals(artefato.getTipoArtefato())
				|| TipoArtefato.CICS_TRANSACTION.equals(artefato.getTipoArtefato())
				|| TipoArtefato.TABELA_CAMPO.equals(artefato.getTipoArtefato())
				|| TipoArtefato.FILE_DESCRIPTION.equals(artefato.getTipoArtefato())) {
			nomeArtefato = artefatoPaiEntrada.getNoNomeArtefato() + "/" + artefato.getNome();
		}

		if (nomeInterno == null || nomeInterno.trim().equals("")) {
			nomeInterno = artefato.getNome();
		}

		ArtefatoPersistence artefatoPersistence = new ArtefatoPersistence();

		artefatoPersistence.setNoNomeArtefato(nomeArtefato);
		artefatoPersistence.setNoNomeExibicao(artefato.getNome());
		artefatoPersistence.setNoNomeInterno(nomeInterno);
		artefatoPersistence.setCoAmbiente(artefato.getAmbiente().get());
		artefatoPersistence.setCoSistema(artefato.getSistema());
		artefatoPersistence.setCoTipoArtefato(artefato.getTipoArtefato().get());
		artefatoPersistence.setDeHash(artefato.getHash());
		artefatoPersistence.setDeIdentificador(artefato.getIdentificador());
		artefatoPersistence.setIcInclusaoManual(false);
		artefatoPersistence.setNoNomeInterno(artefato.getNomeInterno());
		artefatoPersistence.setTsFimVigencia(null);
		artefatoPersistence.setTsInicioVigencia(Configuracao.TS_ATUAL);
		artefatoPersistence.setTsUltimaModificacao(Configuracao.TS_ATUAL);

		artefatoPersistence
				.setTransientListaAtributos(converterAtributoArtefato(artefato.getAtributos(), Tabelas.TBL_ARTEFATO));

		if (artefato.getDescricao() != null && !"".equals(artefato.getDescricao().trim())) {
			artefatoPersistence.setDeDescricaoArtefato(artefato.getDescricao());
		} else {
			artefatoPersistence.setDeDescricaoArtefato(" ");
		}

		// CRIA LISTA DE RELACIONAMENTOS
		if (artefato.getArtefatosRelacionados() != null && artefato.getArtefatosRelacionados().size() > 0) {
			Collections.sort(artefato.getArtefatosRelacionados(), new ArtefatoSorted());

			for (int i = 0; i < artefato.getArtefatosRelacionados().size(); i++) {
				// for (Artefato entry : artefato.getArtefatosRelacionados()) {
				Artefato entry = artefato.getArtefatosRelacionados().get(i);
//				ArtefatoPersistence artefatoAnterior = null;
//				ArtefatoPersistence artefatoPosterior = null;

				if (TipoArtefato.COPYBOOK.equals(artefato.getTipoArtefato())
						&& TipoArtefato.COPYBOOK_VARIAVEL.equals(entry.getTipoArtefato())) {
					continue;
				}

				ArtefatoPersistence artefatoRelacionado = converterArtefato(entry, artefatoPersistence, null, null);

				RelacionamentoPersistence relacionamento = new RelacionamentoPersistence();

				if (entry.isMalhaControlm()) {
					relacionamento.setIcInclusaoMalha(true);
				} else {
					relacionamento.setIcInclusaoMalha(false);
				}

				relacionamento.setIcInclusaoManual(false);
				relacionamento.setArtefatoPai(artefatoPersistence);
				relacionamento.setArtefato(artefatoRelacionado);
				relacionamento.setTsInicioVigencia(Configuracao.TS_ATUAL);

				Matcher m_injetor_p_dsn_cardlib = Patterns.INJETOR_P_DSN_CARDLIB
						.matcher(artefatoRelacionado.getNoNomeExibicao());

				if (TipoArtefato.DSN.get().equals(artefatoRelacionado.getCoTipoArtefato())
						&& !artefatoRelacionado.getNoNomeExibicao().substring(0, 3).equals("CND")
						&& !artefatoRelacionado.getNoNomeExibicao().substring(0, 3).equals("DB2")
						&& !artefatoRelacionado.getNoNomeExibicao().substring(0, 3).equals("V01")
						&& !artefatoRelacionado.getNoNomeExibicao().startsWith("&&")
						&& !artefatoRelacionado.getNoNomeExibicao().startsWith("%%")
						&& !m_injetor_p_dsn_cardlib.matches()) {
					String siglaSistemaDsn = artefatoRelacionado.getNoNomeExibicao().substring(0, 3);
					String siglaSistemaPai = artefatoPersistence.getCoSistema().substring(2);

					if (!siglaSistemaPai.equals(siglaSistemaDsn)) {
						artefatoRelacionado.setCoSistema("SI" + siglaSistemaDsn);
						relacionamento.setCoTipoRelacionamento(TipoRelacionamento.INTERFACE.get());
					}
				} else if (entry.isMalhaControlm()) {
					relacionamento.setCoTipoRelacionamento(TipoRelacionamento.CONTROL_M.get());
				} else if (("DESCONHECIDO".equals(artefatoRelacionado.getCoSistema())
						|| "DESCONHECIDO".equals(artefatoPersistence.getCoSistema()))) {
					relacionamento.setCoTipoRelacionamento(TipoRelacionamento.NORMAL.get());
				}

				Atributo entryAtributo = new Atributo(TipoAtributo.POSICAO, Long.toString(entry.getPosicao()), "",
						"RELACIONAMENTO");
				entry.adicionarAtributo(entryAtributo);

				relacionamento.setTransientListaAtributos(
						converterAtributoRelacionamento(entry.getAtributos(), Tabelas.TBL_RELACIONAMENTO_ARTEFATO));

				artefatoPersistence.adicionarRelacionamentoTransient(relacionamento);
			}
		}

		// CRIA UMA RELACIONAMENTO SEM PAI
		if ((artefatoPaiEntrada == null) && !artefato.isMalhaControlm()) {
			RelacionamentoPersistence relacionamento = new RelacionamentoPersistence();
			relacionamento.setIcInclusaoMalha(false);
			relacionamento.setIcInclusaoManual(false);
			relacionamento.setCoTipoRelacionamento(TipoRelacionamento.NORMAL.get());
			relacionamento.setTsInicioVigencia(Configuracao.TS_ATUAL);
			relacionamento.setIcInclusaoMalha(false);

			relacionamento.setArtefatoPai(null);
			relacionamento.setArtefato(artefatoPersistence);
			artefatoPersistence.adicionarRelacionamentoTransient(relacionamento);
		}

		return artefatoPersistence;
	}

	private static List<AtributoArtefatoPersistence> converterAtributoArtefato(List<Atributo> lista, Tabelas tabela) {
		List<AtributoArtefatoPersistence> output = new ArrayList<>();

		for (Atributo entry : lista) {

			if ("ARTEFATO".equals(entry.getTipo()) && Tabelas.TBL_ARTEFATO.equals(tabela)) {

				AtributoArtefatoPersistence atributoPersistence = new AtributoArtefatoPersistence();

				atributoPersistence.setCoTipoAtributo(entry.getTipoAtributo().get());
				atributoPersistence.setDeValor(entry.getValor());
				atributoPersistence.setIcEditavel(false);
				atributoPersistence.setIcOpcional(false);
				// atributoPersistence.setCoTabela(tabela.get());
				// atributoPersistence.setCoExterno(coExterno);

				output.add(atributoPersistence);
			}
		}
		return output;
	}

	private static List<AtributoRelacionamentoPersistence> converterAtributoRelacionamento(List<Atributo> lista,
			Tabelas tabela) {
		List<AtributoRelacionamentoPersistence> output = new ArrayList<>();

		for (Atributo entry : lista) {

			if ("RELACIONAMENTO".equals(entry.getTipo()) && Tabelas.TBL_RELACIONAMENTO_ARTEFATO.equals(tabela)) {

				AtributoRelacionamentoPersistence atributoPersistence = new AtributoRelacionamentoPersistence();

				atributoPersistence.setCoTipoAtributo(entry.getTipoAtributo().get());
				atributoPersistence.setDeValor(entry.getValor());
				atributoPersistence.setIcEditavel(false);
				atributoPersistence.setIcOpcional(false);
				// atributoPersistence.setCoTabela(tabela.get());
				// atributoPersistence.setCoExterno(coExterno);

				output.add(atributoPersistence);
			}
		}
		return output;
	}

}
