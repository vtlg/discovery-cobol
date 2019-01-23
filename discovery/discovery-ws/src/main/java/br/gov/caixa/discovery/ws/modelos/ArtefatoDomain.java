package br.gov.caixa.discovery.ws.modelos;

import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import br.gov.caixa.discovery.ws.handlers.CalendarAdapter;

@XmlRootElement(name = "artefato")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class ArtefatoDomain {

	@JsonProperty("coArtefato")
	private Long coArtefato;

	@JsonProperty("noNomeArtefato")
	private String noNomeArtefato;

	@JsonProperty("noNomeExibicao")
	private String noNomeExibicao;

	@JsonProperty("noNomeInterno")
	private String noNomeInterno;

	@JsonProperty("coAmbiente")
	private String coAmbiente;

	@JsonProperty("coSistema")
	private String coSistema;

	@JsonProperty("coTipoArtefato")
	private String coTipoArtefato;

	@JsonProperty("deIdentificador")
	private String deIdentificador;

	@JsonProperty("deHash")
	private String deHash = " ";

	@JsonProperty("deDescricaoUsuario")
	private String deDescricaoUsuario = " ";

	@JsonProperty("deDescricaoArtefato")
	private String deDescricaoArtefato = " ";

	@JsonProperty("icInclusaoManual")
	private boolean icInclusaoManual = false;

	@JsonProperty("tsInicioVigencia")
	@XmlJavaTypeAdapter(CalendarAdapter.class)
	private Calendar tsInicioVigencia;

	@JsonProperty("tsUltimaModificacao")
	@XmlJavaTypeAdapter(CalendarAdapter.class)
	private Calendar tsUltimaModificacao;

	@JsonProperty("tsFimVigencia")
	@XmlJavaTypeAdapter(CalendarAdapter.class)
	private Calendar tsFimVigencia;

	@JsonProperty("tipoAtributo")
	private TipoArtefatoDomain tipoArtefato;

	@JsonProperty("listaArtefato")
	private List<RelacionamentoDomain> listaArtefato;

	@JsonProperty("listaArtefatoPai")
	private List<RelacionamentoDomain> listaArtefatoPai;

	@JsonProperty("listaArtefatoAnterior")
	private List<RelacionamentoDomain> listaArtefatoAnterior;

	@JsonProperty("listaArtefatoPosterior")
	private List<RelacionamentoDomain> listaArtefatoPosterior;

	@JsonProperty("listaArtefatoPrimeiro")
	private List<RelacionamentoDomain> listaArtefatoPrimeiro;

	@JsonProperty("listaArtefatoUltimo")
	private List<RelacionamentoDomain> listaArtefatoUltimo;

	public Long getCoArtefato() {
		return coArtefato;
	}

	public void setCoArtefato(Long coArtefato) {
		this.coArtefato = coArtefato;
	}

	public String getNoNomeArtefato() {
		return noNomeArtefato;
	}

	public void setNoNomeArtefato(String noNomeArtefato) {
		this.noNomeArtefato = noNomeArtefato;
	}

	public String getNoNomeExibicao() {
		return noNomeExibicao;
	}

	public void setNoNomeExibicao(String noNomeExibicao) {
		this.noNomeExibicao = noNomeExibicao;
	}

	public String getNoNomeInterno() {
		return noNomeInterno;
	}

	public void setNoNomeInterno(String noNomeInterno) {
		this.noNomeInterno = noNomeInterno;
	}

	public String getCoAmbiente() {
		return coAmbiente;
	}

	public void setCoAmbiente(String coAmbiente) {
		this.coAmbiente = coAmbiente;
	}

	public String getCoSistema() {
		return coSistema;
	}

	public void setCoSistema(String coSistema) {
		this.coSistema = coSistema;
	}

	public String getCoTipoArtefato() {
		return coTipoArtefato;
	}

	public void setCoTipoArtefato(String coTipoArtefato) {
		this.coTipoArtefato = coTipoArtefato;
	}

	public String getDeIdentificador() {
		return deIdentificador;
	}

	public void setDeIdentificador(String deIdentificador) {
		this.deIdentificador = deIdentificador;
	}

	public String getDeHash() {
		return deHash;
	}

	public void setDeHash(String deHash) {
		this.deHash = deHash;
	}

	public String getDeDescricaoUsuario() {
		return deDescricaoUsuario;
	}

	public void setDeDescricaoUsuario(String deDescricaoUsuario) {
		this.deDescricaoUsuario = deDescricaoUsuario;
	}

	public String getDeDescricaoArtefato() {
		return deDescricaoArtefato;
	}

	public void setDeDescricaoArtefato(String deDescricaoArtefato) {
		this.deDescricaoArtefato = deDescricaoArtefato;
	}

	public boolean isIcInclusaoManual() {
		return icInclusaoManual;
	}

	public void setIcInclusaoManual(boolean icInclusaoManual) {
		this.icInclusaoManual = icInclusaoManual;
	}

	public Calendar getTsInicioVigencia() {
		return tsInicioVigencia;
	}

	public void setTsInicioVigencia(Calendar tsInicioVigencia) {
		this.tsInicioVigencia = tsInicioVigencia;
	}

	public Calendar getTsUltimaModificacao() {
		return tsUltimaModificacao;
	}

	public void setTsUltimaModificacao(Calendar tsUltimaModificacao) {
		this.tsUltimaModificacao = tsUltimaModificacao;
	}

	public Calendar getTsFimVigencia() {
		return tsFimVigencia;
	}

	public void setTsFimVigencia(Calendar tsFimVigencia) {
		this.tsFimVigencia = tsFimVigencia;
	}

	public TipoArtefatoDomain getTipoArtefato() {
		return tipoArtefato;
	}

	public void setTipoArtefato(TipoArtefatoDomain tipoArtefato) {
		this.tipoArtefato = tipoArtefato;
	}

	public List<RelacionamentoDomain> getListaArtefato() {
		return listaArtefato;
	}

	public void setListaArtefato(List<RelacionamentoDomain> listaArtefato) {
		this.listaArtefato = listaArtefato;
	}

	public List<RelacionamentoDomain> getListaArtefatoPai() {
		return listaArtefatoPai;
	}

	public void setListaArtefatoPai(List<RelacionamentoDomain> listaArtefatoPai) {
		this.listaArtefatoPai = listaArtefatoPai;
	}

	public List<RelacionamentoDomain> getListaArtefatoAnterior() {
		return listaArtefatoAnterior;
	}

	public void setListaArtefatoAnterior(List<RelacionamentoDomain> listaArtefatoAnterior) {
		this.listaArtefatoAnterior = listaArtefatoAnterior;
	}

	public List<RelacionamentoDomain> getListaArtefatoPosterior() {
		return listaArtefatoPosterior;
	}

	public void setListaArtefatoPosterior(List<RelacionamentoDomain> listaArtefatoPosterior) {
		this.listaArtefatoPosterior = listaArtefatoPosterior;
	}

	public List<RelacionamentoDomain> getListaArtefatoPrimeiro() {
		return listaArtefatoPrimeiro;
	}

	public void setListaArtefatoPrimeiro(List<RelacionamentoDomain> listaArtefatoPrimeiro) {
		this.listaArtefatoPrimeiro = listaArtefatoPrimeiro;
	}

	public List<RelacionamentoDomain> getListaArtefatoUltimo() {
		return listaArtefatoUltimo;
	}

	public void setListaArtefatoUltimo(List<RelacionamentoDomain> listaArtefatoUltimo) {
		this.listaArtefatoUltimo = listaArtefatoUltimo;
	}

}
