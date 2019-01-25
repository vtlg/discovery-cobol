package br.gov.caixa.discovery.ws.modelos;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement(name = "tipoAtributo")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class AtributoDomain {

	@JsonProperty("nuSequencial")
	private Long nuSequencial;

	@JsonProperty("coTipoAtributo")
	private String coTipoAtributo;

	@JsonProperty("coExterno")
	private Long coExterno;

	@JsonProperty("deValor")
	private String deValor;

	@JsonProperty("icEditavel")
	private Boolean icEditavel;

	@JsonProperty("icOpcional")
	private Boolean icOpcional;

	public Long getNuSequencial() {
		return nuSequencial;
	}

	public void setNuSequencial(Long nuSequencial) {
		this.nuSequencial = nuSequencial;
	}

	public String getCoTipoAtributo() {
		return coTipoAtributo;
	}

	public void setCoTipoAtributo(String coTipoAtributo) {
		this.coTipoAtributo = coTipoAtributo;
	}

	public Long getCoExterno() {
		return coExterno;
	}

	public void setCoExterno(Long coExterno) {
		this.coExterno = coExterno;
	}

	public String getDeValor() {
		return deValor;
	}

	public void setDeValor(String deValor) {
		this.deValor = deValor;
	}

	public Boolean getIcEditavel() {
		return icEditavel;
	}

	public void setIcEditavel(Boolean icEditavel) {
		this.icEditavel = icEditavel;
	}

	public Boolean getIcOpcional() {
		return icOpcional;
	}

	public void setIcOpcional(Boolean icOpcional) {
		this.icOpcional = icOpcional;
	}

}
