package br.gov.caixa.discovery.ws.modelos;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement(name = "tipo")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class TipoDomain {

	@JsonProperty("coTipo")
	private String coTipo;

	@JsonProperty("coTabela")
	private String coTabela;

	@JsonProperty("deTipo")
	private String deTipo;

	@JsonProperty("icPesquisavel")
	private Boolean icPesquisavel;

	@JsonProperty("icExibirAtributo")
	private Boolean icExibirAtributo;

	@JsonProperty("icExibirGrafo")
	private Boolean icExibirGrafo;

	@JsonProperty("coCor")
	private String coCor;

	@JsonProperty("coCorBorda")
	private String coCorBorda;

	@JsonProperty("nuLarguraBorda")
	private Integer nuLarguraBorda;

	public TipoDomain() {
		super();
	}

	public TipoDomain(String coTipo) {
		super();
		this.coTipo = coTipo;
	}

	public String getCoTipo() {
		return coTipo;
	}

	public void setCoTipo(String coTipo) {
		this.coTipo = coTipo;
	}

	public String getDeTipo() {
		return deTipo;
	}

	public void setDeTipo(String deTipo) {
		this.deTipo = deTipo;
	}

	public Boolean getIcPesquisavel() {
		return icPesquisavel;
	}

	public void setIcPesquisavel(Boolean icPesquisavel) {
		this.icPesquisavel = icPesquisavel;
	}

	public Boolean getIcExibirAtributo() {
		return icExibirAtributo;
	}

	public void setIcExibirAtributo(Boolean icExibirAtributo) {
		this.icExibirAtributo = icExibirAtributo;
	}

	public Boolean getIcExibirGrafo() {
		return icExibirGrafo;
	}

	public void setIcExibirGrafo(Boolean icExibirGrafo) {
		this.icExibirGrafo = icExibirGrafo;
	}

	public String getCoCor() {
		return coCor;
	}

	public void setCoCor(String coCor) {
		this.coCor = coCor;
	}

	public String getCoCorBorda() {
		return coCorBorda;
	}

	public void setCoCorBorda(String coCorBorda) {
		this.coCorBorda = coCorBorda;
	}
//	

	public String getCoTabela() {
		return coTabela;
	}

	public void setCoTabela(String coTabela) {
		this.coTabela = coTabela;
	}

	public Integer getNuLarguraBorda() {
		return nuLarguraBorda;
	}

	public void setNuLarguraBorda(Integer nuLarguraBorda) {
		this.nuLarguraBorda = nuLarguraBorda;
	}

}
