package br.gov.caixa.discovery.ws.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoRelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.modelos.TipoPersistence;
import br.gov.caixa.discovery.ejb.view.ArtefatoView;
import br.gov.caixa.discovery.ws.modelos.ArtefatoDomain;
import br.gov.caixa.discovery.ws.modelos.ArtefatoViewDomain;
import br.gov.caixa.discovery.ws.modelos.AtributoDomain;
import br.gov.caixa.discovery.ws.modelos.RelacionamentoDomain;
import br.gov.caixa.discovery.ws.modelos.TipoDomain;

public class Conversores {

	public static ArtefatoViewDomain converter(ArtefatoView entrada) {
		ArtefatoViewDomain saida = new ArtefatoViewDomain();

		saida.setCoAmbiente(entrada.getCoAmbiente());
		saida.setCoArtefato(entrada.getCoArtefato());
		saida.setCoSistema(entrada.getCoSistema());
		saida.setCoTipoArtefato(entrada.getCoTipoArtefato());
		// saida.setCoTipoRelacionamento(entrada.getCoTipoRelacionamento());
		saida.setDeDescricaoArtefato(entrada.getDeDescricaoArtefato());
		saida.setDeDescricaoUsuario(entrada.getDeDescricaoUsuario());
		saida.setDeHash(entrada.getDeHash());
		saida.setDeIdentificador(entrada.getDeIdentificador());
		saida.setIcInclusaoManual(entrada.getIcInclusaoManual());
		saida.setIcProcessoCritico(entrada.getIcProcessoCritico());
		saida.setNoNomeArtefato(entrada.getNoNomeArtefato());
		saida.setNoNomeExibicao(entrada.getNoNomeExibicao());
		saida.setNoNomeInterno(entrada.getNoNomeInterno());
		saida.setTsInicioVigencia(entrada.getTsInicioVigencia());
		saida.setTsUltimaModificacao(entrada.getTsUltimaModificacao());

		saida.setCountRelacionamento(entrada.getCountRelacionamento());
		saida.setCountRelacionamentoControlM(entrada.getCountRelacionamentoControlM());
		saida.setCountRelacionamentoInterface(entrada.getCountRelacionamentoInterface());
		saida.setCountRelacionamentoNormal(entrada.getCountRelacionamentoNormal());

		return saida;
	}

	public static ArtefatoDomain converter(ArtefatoPersistence entrada, boolean converterLista,
			boolean converterAtributo) {
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
		saida.setIcProcessoCritico(entrada.isIcProcessoCritico());
		saida.setNoNomeArtefato(entrada.getNoNomeArtefato());
		saida.setNoNomeExibicao(entrada.getNoNomeExibicao());
		saida.setNoNomeInterno(entrada.getNoNomeInterno());
		saida.setTsFimVigencia(entrada.getTsFimVigencia());
		saida.setTsInicioVigencia(entrada.getTsInicioVigencia());
		saida.setTsUltimaModificacao(entrada.getTsUltimaModificacao());

		if (entrada.getTipoArtefato() == null) {
			TipoDomain tipoArtefatoDomain = new TipoDomain();
			tipoArtefatoDomain.setCoTipo(entrada.getCoTipoArtefato());
			saida.setTipo(tipoArtefatoDomain);
		} else {
			saida.setTipo(converter(entrada.getTipoArtefato()));
		}

		if (converterAtributo && entrada.getListaAtributos() != null && entrada.getListaAtributos().size() > 0) {
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

		saida.setDescendente(converter(entrada.getArtefato(), false, false));
		saida.setAscendente(converter(entrada.getArtefatoPai(), false, false));

		saida.setAnterior(converter(entrada.getArtefatoAnterior(), false, false));
		saida.setPosterior(converter(entrada.getArtefatoPosterior(), false, false));

		saida.setPrimeiro(converter(entrada.getArtefatoPrimeiro(), false, false));
		saida.setUltimo(converter(entrada.getArtefatoUltimo(), false, false));

		saida.setCoRelacionamento(entrada.getCoRelacionamento());
		saida.setIcInclusaoMalha(entrada.isIcInclusaoMalha());
		saida.setIcInclusaoManual(entrada.isIcInclusaoManual());

		if (entrada.getTipoRelacionamento() == null && entrada.getCoTipoRelacionamento() == null) {
			TipoDomain tipoArtefatoDomain = new TipoDomain();
			tipoArtefatoDomain.setCoTipo("NORMAL");
			saida.setTipoRelacionamento(tipoArtefatoDomain);
		} else if (entrada.getCoTipoRelacionamento() == null) {
			saida.setTipoRelacionamento(converter(entrada.getTipoRelacionamento()));
		} else {
			TipoDomain tipoArtefatoDomain = new TipoDomain();
			tipoArtefatoDomain.setCoTipo(entrada.getCoTipoRelacionamento());
			saida.setTipoRelacionamento(tipoArtefatoDomain);
		}

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

		saida.setCoTipoAtributo(entrada.getCoTipoAtributo());
		saida.setCoExterno(entrada.getCoExterno());
		saida.setDeValor(entrada.getDeValor());
		saida.setIcEditavel(entrada.getIcEditavel());
		saida.setIcOpcional(entrada.getIcOpcional());
		saida.setNuSequencial(entrada.getNuSequencial());

		return saida;
	}

	public static TipoDomain converter(TipoPersistence entrada) {
		if (entrada == null) {
			return null;
		}

		TipoDomain saida = new TipoDomain();

		saida.setCoTipo(entrada.getCoTipo());
		saida.setCoTabela(entrada.getCoTabela());
		saida.setDeTipo(entrada.getDeTipo());
		saida.setIcExibirAtributo(entrada.getIcExibirAtributo());
		saida.setIcExibirGrafo(entrada.getIcExibirGrafo());
		saida.setIcPesquisavel(entrada.getIcPesquisavel());
		saida.setCoCor(entrada.getCoCor());
		saida.setCoCorBorda(entrada.getCoCorBorda());
		saida.setNuLarguraBorda(entrada.getNuLarguraBorda());

		return saida;
	}

	public static List<ArtefatoDomain> converterListaArtefato(List<ArtefatoPersistence> entrada) {
		if (entrada == null || entrada.size() == 0) {
			return null;
		}

		List<ArtefatoDomain> saida = new ArrayList<>();

		for (ArtefatoPersistence entry : entrada) {
			saida.add(converter(entry, false, false));
		}

		return saida;
	}

	public static List<ArtefatoViewDomain> converterListaArtefatoView(Collection<ArtefatoView> entrada) {
		if (entrada == null || entrada.size() == 0) {
			return null;
		}

		List<ArtefatoViewDomain> saida = new ArrayList<>();

		for (ArtefatoView entry : entrada) {
			saida.add(converter(entry));
		}

		return saida;
	}

	public static List<RelacionamentoDomain> converterListaRelacionamento(Set<RelacionamentoPersistence> entrada) {
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
			Set<AtributoRelacionamentoPersistence> entrada) {
		if (entrada == null || entrada.size() == 0) {
			return null;
		}

		List<AtributoDomain> saida = new ArrayList<>();

		for (AtributoPersistence entry : entrada) {
			saida.add(converter(entry));
		}

		return saida;
	}

	public static List<TipoDomain> converterListaTipo(List<TipoPersistence> entrada) {
		if (entrada == null || entrada.size() == 0) {
			return null;
		}

		List<TipoDomain> saida = new ArrayList<>();

		for (TipoPersistence entry : entrada) {
			saida.add(converter(entry));
		}

		return saida;
	}

}
