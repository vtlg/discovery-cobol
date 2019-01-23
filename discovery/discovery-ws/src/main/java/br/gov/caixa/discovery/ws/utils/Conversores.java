package br.gov.caixa.discovery.ws.utils;

import java.util.ArrayList;
import java.util.List;

import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.modelos.TipoArtefatoPersistence;
import br.gov.caixa.discovery.ws.modelos.ArtefatoDomain;
import br.gov.caixa.discovery.ws.modelos.RelacionamentoDomain;
import br.gov.caixa.discovery.ws.modelos.TipoArtefatoDomain;

public class Conversores {

	public static ArtefatoDomain converter(ArtefatoPersistence entrada, boolean converterLista) {
		if (entrada == null) {
			return null;
		}

		ArtefatoDomain saida = new ArtefatoDomain();

		saida.setCoAmbiente(entrada.getCoAmbiente());
		saida.setCoArtefato(entrada.getCoArtefato());
		saida.setCoSistema(entrada.getCoSistema());
		saida.setCoTipoArtefato(entrada.getCoTipoArtefato());
		saida.setDeDescricaoArtefato(entrada.getDeDescricaoArtefato());
		saida.setDeDescricaoUsuario(entrada.getDeDescricaoUsuario());
		saida.setDeHash(entrada.getDeHash());
		saida.setDeIdentificador(entrada.getDeIdentificador());
		saida.setIcInclusaoManual(entrada.isIcInclusaoManual());
		saida.setNoNomeArtefato(entrada.getNoNomeArtefato());
		saida.setNoNomeExibicao(entrada.getNoNomeExibicao());
		saida.setNoNomeInterno(entrada.getNoNomeInterno());
		saida.setTsFimVigencia(entrada.getTsFimVigencia());
		saida.setTsInicioVigencia(entrada.getTsInicioVigencia());
		saida.setTsUltimaModificacao(entrada.getTsUltimaModificacao());

		if (converterLista) {
			saida.setListaArtefato(converterListaRelacionamento(entrada.getListaArtefato()));
//			saida.setListaArtefatoPai(converterListaRelacionamento(entrada.getListaArtefatoPai()));
//			saida.setListaArtefatoAnterior(converterListaRelacionamento(entrada.getListaArtefatoAnterior()));
//			saida.setListaArtefatoPosterior(converterListaRelacionamento(entrada.getListaArtefatoPosterior()));
//			saida.setListaArtefatoPrimeiro(converterListaRelacionamento(entrada.getListaArtefatoPrimeiro()));
//			saida.setListaArtefatoUltimo(converterListaRelacionamento(entrada.getListaArtefatoUltimo()));
		}

		return saida;
	}

	public static List<RelacionamentoDomain> converterListaRelacionamento(List<RelacionamentoPersistence> entrada) {
		if (entrada == null || entrada.size() == 0) {
			return null;
		}

		List<RelacionamentoDomain> saida = new ArrayList<>();

		for (RelacionamentoPersistence entry : entrada) {
			saida.add(converter(entry));
		}

		return saida;
	}

	public static RelacionamentoDomain converter(RelacionamentoPersistence entrada) {
		RelacionamentoDomain saida = new RelacionamentoDomain();

		saida.setArtefato(converter(entrada.getArtefato(), false));
		saida.setArtefatoAnterior(converter(entrada.getArtefatoAnterior(), false));
		saida.setArtefatoPai(converter(entrada.getArtefatoPai(), false));
		saida.setArtefatoPosterior(converter(entrada.getArtefatoPosterior(), false));
		saida.setArtefatoPrimeiro(converter(entrada.getArtefatoPrimeiro(), false));
		saida.setArtefatoUltimo(converter(entrada.getArtefatoUltimo(), false));
		saida.setCoRelacionamento(entrada.getCoRelacionamento());
		saida.setIcInclusaoMalha(entrada.isIcInclusaoMalha());
		saida.setIcInclusaoManual(entrada.isIcInclusaoManual());

		return saida;
	}

	public static List<TipoArtefatoDomain> converterListaTipoArtefato(List<TipoArtefatoPersistence> entrada) {
		if (entrada == null || entrada.size() == 0) {
			return null;
		}

		List<TipoArtefatoDomain> saida = new ArrayList<>();

		for (TipoArtefatoPersistence entry : entrada) {
			saida.add(converter(entry));
		}

		return saida;
	}

	public static TipoArtefatoDomain converter(TipoArtefatoPersistence entrada) {
		TipoArtefatoDomain saida = new TipoArtefatoDomain();

		saida.setCoCor(entrada.getCoCor());
		saida.setCoTipoArtefato(entrada.getCoTipoArtefato());
		saida.setDeTipoArtefato(entrada.getDeTipoArtefato());
		saida.setIcAtributo(entrada.getIcAtributo());
		saida.setIcGrafo(entrada.getIcGrafo());
		saida.setIcPesquisavel(entrada.getIcPesquisavel());

		return saida;
	}
}
