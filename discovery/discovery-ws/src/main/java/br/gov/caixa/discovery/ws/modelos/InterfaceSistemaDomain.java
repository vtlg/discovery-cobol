package br.gov.caixa.discovery.ws.modelos;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement(name = "interface")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class InterfaceSistemaDomain {

	@JsonProperty("nodes")
	private List<InterfaceSistemaNode> nodes;

	@JsonProperty("links")
	private List<InterfaceSistemaLink> links;

	public List<InterfaceSistemaNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<InterfaceSistemaNode> nodes) {
		this.nodes = nodes;
	}

	public List<InterfaceSistemaLink> getLinks() {
		return links;
	}

	public void setLinks(List<InterfaceSistemaLink> links) {
		this.links = links;
	}

}
