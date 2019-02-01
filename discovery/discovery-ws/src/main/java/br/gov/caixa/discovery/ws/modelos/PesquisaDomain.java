package br.gov.caixa.discovery.ws.modelos;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement(name = "pesquisa")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class PesquisaDomain {

	@JsonProperty("expNome")
	private String expNome;

	@JsonProperty("expDescricao")
	private String expDescricao;

	@JsonProperty("listaTipoArtefato")
	private String[] listaTipoArtefato;

	@JsonProperty("listaSistema")
	private String[] listaSistema;

	@JsonProperty("icProcessoCritico")
	private Boolean icProcessoCritico;

	@JsonProperty("icInterface")
	private Boolean icInterface;

	public String getExpNome() {
		return expNome;
	}

	public void setExpNome(String expNome) {
		this.expNome = expNome;
	}

	public String getExpDescricao() {
		return expDescricao;
	}

	public void setExpDescricao(String expDescricao) {
		this.expDescricao = expDescricao;
	}

	public String[] getListaTipoArtefato() {
		return listaTipoArtefato;
	}

	public void setListaTipoArtefato(String[] listaTipoArtefato) {
		this.listaTipoArtefato = listaTipoArtefato;
	}

	public Boolean getIcProcessoCritico() {
		return icProcessoCritico;
	}

	public void setIcProcessoCritico(Boolean icProcessoCritico) {
		this.icProcessoCritico = icProcessoCritico;
	}

	public Boolean getIcInterface() {
		return icInterface;
	}

	public void setIcInterface(Boolean icInterface) {
		this.icInterface = icInterface;
	}

	public String[] getListaSistema() {
		return listaSistema;
	}

	public void setListaSistema(String[] listaSistema) {
		this.listaSistema = listaSistema;
	}

}
