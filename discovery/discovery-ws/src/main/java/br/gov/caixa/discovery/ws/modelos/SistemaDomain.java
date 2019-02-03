package br.gov.caixa.discovery.ws.modelos;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement(name = "sistema")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class SistemaDomain {

	@JsonProperty("coSistema")
	private String coSistema;

	@JsonProperty("deSistema")
	private String deSistema;

	public String getCoSistema() {
		return coSistema;
	}

	public void setCoSistema(String coSistema) {
		this.coSistema = coSistema;
	}

	public String getDeSistema() {
		return deSistema;
	}

	public void setDeSistema(String deSistema) {
		this.deSistema = deSistema;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("coSistema : (" + this.coSistema + "), ");
		sb.append("deSistema : (" + this.deSistema + "), ");

		return sb.toString();
	}
	
}
