package br.gov.caixa.discovery.injetores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.gov.caixa.discovery.core.modelos.Artefato;
import br.gov.caixa.discovery.core.modelos.ArtefatoSorted;
import br.gov.caixa.discovery.core.modelos.Atributo;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.tipos.TipoAtributo;
import br.gov.caixa.discovery.core.utils.Configuracao;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoRelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.tipos.Tabelas;

public class Conversor {
	
	public static List<ArtefatoPersistence> executar(Artefato artefato, boolean isControlM) throws Exception {
		List<ArtefatoPersistence> output = new ArrayList<>();

		if (artefato != null) {
			ArtefatoPersistence artefatoPersistence = null;
			artefatoPersistence = converterArtefato(artefato);
			output.add(artefatoPersistence);
		}
		
		return output;
	}
	
	private static List<ArtefatoPersistence> getListaArtefatos(List<ArtefatoPersistence> lista,
			ArtefatoPersistence artefato) {

		lista.add(artefato);

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

			// if (entry.isIcInclusaoMalha()) {
			artefatoPrimeiro = existeArtefato(lista, entry.getArtefatoPrimeiro());
			artefatoUltimo = existeArtefato(lista, entry.getArtefatoUltimo());
			artefatoAnterior = existeArtefato(lista, entry.getArtefatoAnterior());
			artefatoPosterior = existeArtefato(lista, entry.getArtefatoPosterior());
			artefato = existeArtefato(lista, entry.getArtefato());
			artefatoPai = existeArtefato(lista, entry.getArtefatoPai());
//			} else {
//				artefatoPrimeiro = existeArtefato(lista, entry.getArtefatoPrimeiro());
//				artefatoUltimo = existeArtefato(lista, entry.getArtefatoUltimo());
//				artefatoAnterior = existeArtefato(lista, entry.getArtefatoAnterior());
//				artefatoPosterior = existeArtefato(lista, entry.getArtefatoPosterior());
//				artefato = existeArtefato(lista, entry.getArtefato());
//				artefatoPai = existeArtefato(lista, entry.getArtefatoPai());
//			}

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
				ArtefatoPersistence artefatoAnterior = null;
				ArtefatoPersistence artefatoPosterior = null;
				ArtefatoPersistence artefatoPrimeiro = null;
				ArtefatoPersistence artefatoUltimo = null;

				// Recupera o artefato anterior caso o artefato objeto não seja o primeiro da
				// lista
				if (i > 0) {
					artefatoAnterior = converterArtefato(artefato.getArtefatosRelacionados().get(i - 1),
							artefatoPersistence, null, null);
				}

				// Recupera o artefato posterior caso o artefato objeto não seja o último da
				// lista
				if (i < artefato.getArtefatosRelacionados().size() - 1) {
					artefatoPosterior = converterArtefato(artefato.getArtefatosRelacionados().get(i + 1),
							artefatoPersistence, null, null);
				}

				if (TipoArtefato.COPYBOOK.equals(artefato.getTipoArtefato())
						&& TipoArtefato.COPYBOOK_VARIAVEL.equals(entry.getTipoArtefato())) {
					continue;
				}

				ArtefatoPersistence artefatoRelacionado = converterArtefato(entry, artefatoPersistence,
						artefatoAnterior, artefatoPosterior);

				// Recupera o artefato primeiro caso o artefato objeto tenha relacionamento
				if (entry != null && entry.getArtefatosRelacionados() != null
						&& entry.getArtefatosRelacionados().size() > 0) {
					artefatoPrimeiro = converterArtefato(entry.getArtefatosRelacionados().get(0), artefatoRelacionado,
							null, null);
					artefatoUltimo = converterArtefato(
							entry.getArtefatosRelacionados().get(entry.getArtefatosRelacionados().size() - 1),
							artefatoRelacionado, null, null);
				}

				RelacionamentoPersistence relacionamento = new RelacionamentoPersistence();

				if (entry.isMalhaControlm()) {
					relacionamento.setIcInclusaoMalha(true);
				} else {
					relacionamento.setIcInclusaoMalha(false);
				}

				relacionamento.setIcInclusaoManual(false);
				relacionamento.setArtefatoPrimeiro(artefatoPrimeiro);
				relacionamento.setArtefatoUltimo(artefatoUltimo);
				relacionamento.setArtefatoAnterior(artefatoAnterior);
				relacionamento.setArtefatoPosterior(artefatoPosterior);
				relacionamento.setArtefatoPai(artefatoPersistence);
				relacionamento.setArtefato(artefatoRelacionado);
				relacionamento.setTsInicioVigencia(Configuracao.TS_ATUAL);
				relacionamento.setCoTipoRelacionamento(entry.getTipoRelacionamento().get());

				Atributo entryAtributo = new Atributo(TipoAtributo.POSICAO, Long.toString(entry.getPosicao()), "",
						Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get());
				entry.adicionarAtributo(entryAtributo);

				relacionamento.setTransientListaAtributos(
						converterAtributoRelacionamento(entry.getAtributos(), Tabelas.TBL_RELACIONAMENTO_ARTEFATO));

				artefatoPersistence.adicionarRelacionamentoTransient(relacionamento);
			}
		}

		// CRIA UMA RELACIONAMENTO SEM PAI
//		if ((artefatoPaiEntrada == null) && !artefato.isMalhaControlm()) {
//			RelacionamentoPersistence relacionamento = new RelacionamentoPersistence();
//			relacionamento.setIcInclusaoMalha(false);
//			relacionamento.setIcInclusaoManual(false);
//			relacionamento.setCoTipoRelacionamento(TipoRelacionamento.NORMAL.get());
//			relacionamento.setTsInicioVigencia(Configuracao.TS_ATUAL);
//			relacionamento.setIcInclusaoMalha(false);
//
//			relacionamento.setArtefatoPai(null);
//			relacionamento.setArtefato(artefatoPersistence);
//			artefatoPersistence.adicionarRelacionamentoTransient(relacionamento);
//		}

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

			if (Tabelas.TBL_RELACIONAMENTO_ARTEFATO.get().equals(entry.getTipo())
					&& Tabelas.TBL_RELACIONAMENTO_ARTEFATO.equals(tabela)) {

				AtributoRelacionamentoPersistence atributoPersistence = new AtributoRelacionamentoPersistence();

				atributoPersistence.setCoTipoAtributo(entry.getTipoAtributo().get());
				atributoPersistence.setDeValor(entry.getValor());
				atributoPersistence.setIcEditavel(false);
				atributoPersistence.setIcOpcional(false);
				// atributoPersistence.setCoTabela(tabela.get());
				// atributoPersistence.setCoExterno(coExterno);

				if (!existeAtributo(output, atributoPersistence)) {
					output.add(atributoPersistence);
				}
			}
		}
		return output;
	}

	private static boolean existeAtributo(List<AtributoRelacionamentoPersistence> lista,
			AtributoRelacionamentoPersistence atributo) {

		for (AtributoRelacionamentoPersistence entry : lista) {
			if (entry.getCoTipoAtributo().equals(atributo.getCoTipoAtributo())
					&& entry.getDeValor().equals(atributo.getDeValor())
					&& entry.getIcEditavel().equals(atributo.getIcEditavel())) {
				return true;
			}
		}

		return false;
	}

}
