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

	@JsonProperty("icProcessoCritico")
	private boolean icProcessoCritico = false;

	@JsonProperty("tsInicioVigencia")
	@XmlJavaTypeAdapter(CalendarAdapter.class)
	private Calendar tsInicioVigencia;

	@JsonProperty("tsUltimaModificacao")
	@XmlJavaTypeAdapter(CalendarAdapter.class)
	private Calendar tsUltimaModificacao;

	@JsonProperty("tsFimVigencia")
	@XmlJavaTypeAdapter(CalendarAdapter.class)
	private Calendar tsFimVigencia;

	@JsonProperty("countRelacionamento")
	private Long countRelacionamento;

	@JsonProperty("countRelacionamentoInterface")
	private Long countRelacionamentoInterface;

	@JsonProperty("countRelacionamentoNormal")
	private Long countRelacionamentoNormal;

	@JsonProperty("countRelacionamentoControlM")
	private Long countRelacionamentoControlM;

	@JsonProperty("tipoArtefato")
	private TipoDomain tipoArtefato;

	@JsonProperty("atributos")
	private List<AtributoDomain> atributos;

	@JsonProperty("descendentes")
	private List<RelacionamentoDomain> descendentes;

	@JsonProperty("ascendentes")
	private List<RelacionamentoDomain> ascendentes;

	@JsonProperty("anteriores")
	private List<RelacionamentoDomain> anteriores;

	@JsonProperty("posteriores")
	private List<RelacionamentoDomain> posteriores;

	@JsonProperty("primeiros")
	private List<RelacionamentoDomain> primeiros;

	@JsonProperty("ultimos")
	private List<RelacionamentoDomain> ultimos;

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

	public TipoDomain getTipoArtefato() {
		return tipoArtefato;
	}

	public void setTipo(TipoDomain tipo) {
		this.tipoArtefato = tipo;
	}

	public List<AtributoDomain> getAtributos() {
		return atributos;
	}

	public void setAtributos(List<AtributoDomain> atributos) {
		this.atributos = atributos;
	}

	public List<RelacionamentoDomain> getDescendentes() {
		return descendentes;
	}

	public void setDescendentes(List<RelacionamentoDomain> descendentes) {
		this.descendentes = descendentes;
	}

	public List<RelacionamentoDomain> getAscendentes() {
		return ascendentes;
	}

	public void setAscendentes(List<RelacionamentoDomain> ascendentes) {
		this.ascendentes = ascendentes;
	}

	public List<RelacionamentoDomain> getAnteriores() {
		return anteriores;
	}

	public void setAnteriores(List<RelacionamentoDomain> anteriores) {
		this.anteriores = anteriores;
	}

	public List<RelacionamentoDomain> getPosteriores() {
		return posteriores;
	}

	public void setPosteriores(List<RelacionamentoDomain> posteriores) {
		this.posteriores = posteriores;
	}

	public List<RelacionamentoDomain> getPrimeiros() {
		return primeiros;
	}

	public void setPrimeiros(List<RelacionamentoDomain> primeiros) {
		this.primeiros = primeiros;
	}

	public List<RelacionamentoDomain> getUltimos() {
		return ultimos;
	}

	public void setUltimos(List<RelacionamentoDomain> ultimos) {
		this.ultimos = ultimos;
	}

	public boolean isIcProcessoCritico() {
		return icProcessoCritico;
	}

	public void setIcProcessoCritico(boolean icProcessoCritico) {
		this.icProcessoCritico = icProcessoCritico;
	}

	public void setTipoArtefato(TipoDomain tipoArtefato) {
		this.tipoArtefato = tipoArtefato;
	}

	public Long getCountRelacionamento() {
		return countRelacionamento;
	}

	public void setCountRelacionamento(Long countRelacionamento) {
		this.countRelacionamento = countRelacionamento;
	}

	public Long getCountRelacionamentoInterface() {
		return countRelacionamentoInterface;
	}

	public void setCountRelacionamentoInterface(Long countRelacionamentoInterface) {
		this.countRelacionamentoInterface = countRelacionamentoInterface;
	}

	public Long getCountRelacionamentoNormal() {
		return countRelacionamentoNormal;
	}

	public void setCountRelacionamentoNormal(Long countRelacionamentoNormal) {
		this.countRelacionamentoNormal = countRelacionamentoNormal;
	}

	public Long getCountRelacionamentoControlM() {
		return countRelacionamentoControlM;
	}

	public void setCountRelacionamentoControlM(Long countRelacionamentoControlM) {
		this.countRelacionamentoControlM = countRelacionamentoControlM;
	}

}
