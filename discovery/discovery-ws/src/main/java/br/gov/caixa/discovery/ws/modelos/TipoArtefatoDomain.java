package br.gov.caixa.discovery.ws.modelos;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement(name = "tipoArtefato")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class TipoArtefatoDomain {

	@JsonProperty("coTipoArtefato")
	private String coTipoArtefato;

	@JsonProperty("deTipoArtefato")
	private String deTipoArtefato;

	@JsonProperty("icPesquisavel")
	private Boolean icPesquisavel;

	@JsonProperty("icAtributo")
	private Boolean icAtributo;

	@JsonProperty("icGrafo")
	private Boolean icGrafo;

	@JsonProperty("coCor")
	private String coCor;

	@JsonProperty("coCorBorda")
	private String coCorBorda;

	public TipoArtefatoDomain() {
		super();
	}

	public TipoArtefatoDomain(String coTipoArtefato) {
		super();
		this.coTipoArtefato = coTipoArtefato;
	}

	public String getCoTipoArtefato() {
		return coTipoArtefato;
	}

	public void setCoTipoArtefato(String coTipoArtefato) {
		this.coTipoArtefato = coTipoArtefato;
	}

	public String getDeTipoArtefato() {
		return deTipoArtefato;
	}

	public void setDeTipoArtefato(String deTipoArtefato) {
		this.deTipoArtefato = deTipoArtefato;
	}

	public Boolean getIcPesquisavel() {
		return icPesquisavel;
	}

	public void setIcPesquisavel(Boolean icPesquisavel) {
		this.icPesquisavel = icPesquisavel;
	}

	public Boolean getIcAtributo() {
		return icAtributo;
	}

	public void setIcAtributo(Boolean icAtributo) {
		this.icAtributo = icAtributo;
	}

	public Boolean getIcGrafo() {
		return icGrafo;
	}

	public void setIcGrafo(Boolean icGrafo) {
		this.icGrafo = icGrafo;
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

}
