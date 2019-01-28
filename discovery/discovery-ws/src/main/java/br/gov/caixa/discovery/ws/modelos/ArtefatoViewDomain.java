package br.gov.caixa.discovery.ws.modelos;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import br.gov.caixa.discovery.ws.handlers.CalendarAdapter;

@XmlRootElement(name = "artefato-view")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class ArtefatoViewDomain {

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
	private String deHash;

	@JsonProperty("deDescricaoUsuario")
	private String deDescricaoUsuario;

	@JsonProperty("deDescricaoArtefato")
	private String deDescricaoArtefato;

	@JsonProperty("icProcessoCritico")
	private Boolean icProcessoCritico;

	@JsonProperty("icInclusaoManual")
	private Boolean icInclusaoManual;

	@JsonProperty("tsInicioVigencia")
	@XmlJavaTypeAdapter(CalendarAdapter.class)
	private Calendar tsInicioVigencia;

	@JsonProperty("tsUltimaModificacao")
	@XmlJavaTypeAdapter(CalendarAdapter.class)
	private Calendar tsUltimaModificacao;

	@JsonProperty("coTipoRelacionamento")
	private String coTipoRelacionamento;

	@JsonProperty("icInclusaoManualRelacionamento")
	private Boolean icInclusaoManualRelacionamento;

	@JsonProperty("icInclusaoMalhaRelacionamento")
	private Boolean icInclusaoMalhaRelacionamento;

	@JsonProperty("countRelacionamento")
	private Long countRelacionamento;

	@JsonProperty("countRelacionamentoInterface")
	private Long countRelacionamentoInterface;

	@JsonProperty("countRelacionamentoNormal")
	private Long countRelacionamentoNormal;

	@JsonProperty("countRelacionamentoControlM")
	private Long countRelacionamentoControlM;

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

	public Boolean getIcProcessoCritico() {
		return icProcessoCritico;
	}

	public void setIcProcessoCritico(Boolean icProcessoCritico) {
		this.icProcessoCritico = icProcessoCritico;
	}

	public Boolean getIcInclusaoManual() {
		return icInclusaoManual;
	}

	public void setIcInclusaoManual(Boolean icInclusaoManual) {
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

	public String getCoTipoRelacionamento() {
		return coTipoRelacionamento;
	}

	public void setCoTipoRelacionamento(String coTipoRelacionamento) {
		this.coTipoRelacionamento = coTipoRelacionamento;
	}

	public Boolean getIcInclusaoManualRelacionamento() {
		return icInclusaoManualRelacionamento;
	}

	public void setIcInclusaoManualRelacionamento(Boolean icInclusaoManualRelacionamento) {
		this.icInclusaoManualRelacionamento = icInclusaoManualRelacionamento;
	}

	public Boolean getIcInclusaoMalhaRelacionamento() {
		return icInclusaoMalhaRelacionamento;
	}

	public void setIcInclusaoMalhaRelacionamento(Boolean icInclusaoMalhaRelacionamento) {
		this.icInclusaoMalhaRelacionamento = icInclusaoMalhaRelacionamento;
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
