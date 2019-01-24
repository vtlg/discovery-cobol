package br.gov.caixa.discovery.ws.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoRelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.modelos.TipoArtefatoPersistence;
import br.gov.caixa.discovery.ws.modelos.ArtefatoDomain;
import br.gov.caixa.discovery.ws.modelos.AtributoDomain;
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

		if (entrada.getTipoArtefato() == null) {
			TipoArtefatoDomain tipoArtefatoDomain = new TipoArtefatoDomain();
			tipoArtefatoDomain.setCoTipoArtefato(entrada.getCoTipoArtefato());
			saida.setTipoArtefato(tipoArtefatoDomain);
		} else {
			saida.setTipoArtefato(converter(entrada.getTipoArtefato()));
		}

		if (entrada.getListaAtributos() != null && entrada.getListaAtributos().size() > 0) {
			saida.setAtributos(converterListaAtributoArtefato(entrada.getListaAtributos()));
		}

		if (converterLista) {
			saida.setDescendentes(converterListaRelacionamento(entrada.getListaArtefato()));
			saida.setAscendentes(converterListaRelacionamento(entrada.getListaArtefatoPai()));
			saida.setAnteriores(converterListaRelacionamento(entrada.getListaArtefatoAnterior()));
			saida.setPosteriores(converterListaRelacionamento(entrada.getListaArtefatoPosterior()));
			saida.setPrimeiros(converterListaRelacionamento(entrada.getListaArtefatoPrimeiro()));
			saida.setUltimos(converterListaRelacionamento(entrada.getListaArtefatoUltimo()));
		}

		return saida;
	}

	public static RelacionamentoDomain converter(RelacionamentoPersistence entrada) {
		if (entrada == null) {
			return null;
		}

		RelacionamentoDomain saida = new RelacionamentoDomain();

		saida.setDescendente(converter(entrada.getArtefato(), false));
		saida.setAscendente(converter(entrada.getArtefatoPai(), false));

		saida.setAnterior(converter(entrada.getArtefatoAnterior(), false));
		saida.setPosterior(converter(entrada.getArtefatoPosterior(), false));

		saida.setPrimeiro(converter(entrada.getArtefatoPrimeiro(), false));
		saida.setUltimo(converter(entrada.getArtefatoUltimo(), false));

		saida.setCoRelacionamento(entrada.getCoRelacionamento());
		saida.setIcInclusaoMalha(entrada.isIcInclusaoMalha());
		saida.setIcInclusaoManual(entrada.isIcInclusaoManual());

		if (entrada.getListaAtributos() != null && entrada.getListaAtributos().size() > 0) {
			saida.setAtributos(converterListaAtributoRelacionamento(entrada.getListaAtributos()));
		}

		return saida;
	}

	public static AtributoDomain converter(AtributoPersistence entrada) {
		if (entrada == null) {
			return null;
		}

		AtributoDomain saida = new AtributoDomain();

		saida.setCoAtributo(entrada.getCoAtributo());
		saida.setCoExterno(entrada.getCoExterno());
		saida.setDeValor(entrada.getDeValor());
		saida.setIcEditavel(entrada.getIcEditavel());
		saida.setIcOpcional(entrada.getIcOpcional());
		saida.setNuSequencial(entrada.getNuSequencial());

		return saida;
	}

	public static TipoArtefatoDomain converter(TipoArtefatoPersistence entrada) {
		if (entrada == null) {
			return null;
		}

		TipoArtefatoDomain saida = new TipoArtefatoDomain();

		saida.setCoCorBorda(entrada.getCoCorBorda());
		saida.setCoCor(entrada.getCoCor());
		saida.setCoTipoArtefato(entrada.getCoTipoArtefato());
		saida.setDeTipoArtefato(entrada.getDeTipoArtefato());
		saida.setIcAtributo(entrada.getIcAtributo());
		saida.setIcGrafo(entrada.getIcGrafo());
		saida.setIcPesquisavel(entrada.getIcPesquisavel());

		return saida;
	}

	public static List<ArtefatoDomain> converterListaArtefato(List<ArtefatoPersistence> entrada) {
		if (entrada == null || entrada.size() == 0) {
			return null;
		}

		List<ArtefatoDomain> saida = new ArrayList<>();

		for (ArtefatoPersistence entry : entrada) {
			saida.add(converter(entry, false));
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

	public static List<AtributoDomain> converterListaAtributoArtefato(Set<AtributoArtefatoPersistence> entrada) {
		if (entrada == null || entrada.size() == 0) {
			return null;
		}

		List<AtributoDomain> saida = new ArrayList<>();

		for (AtributoPersistence entry : entrada) {
			saida.add(converter(entry));
		}

		return saida;
	}

	public static List<AtributoDomain> converterListaAtributoRelacionamento(
			List<AtributoRelacionamentoPersistence> entrada) {
		if (entrada == null || entrada.size() == 0) {
			return null;
		}

		List<AtributoDomain> saida = new ArrayList<>();

		for (AtributoPersistence entry : entrada) {
			saida.add(converter(entry));
		}

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

}
