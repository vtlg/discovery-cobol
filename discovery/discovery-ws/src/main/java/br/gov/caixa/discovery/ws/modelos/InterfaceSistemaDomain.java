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
	private List<InterfaceSistemaDiagramaSankeyNode> nodes;

	@JsonProperty("links")
	private List<InterfaceSistemaDiagramaSankeyLink> links;

	private Long coArtefato;
	private String noNomeArtefato;
	private String noNomeExibicao;
	private String noNomeInterno;
	private String coTipoArtefato;
	private String coSistema;

	private Long coArtefatoPai;
	private String noNomeArtefatoPai;
	private String noNomeExibicaoPai;
	private String noNomeInternoPai;
	private String coTipoArtefatoPai;
	private String coSistemaPai;

	private String caminhoCoArtefato;
	private String coTipoRelacionamentoInicial;
	private String coSistemaDestino;

	public List<InterfaceSistemaDiagramaSankeyNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<InterfaceSistemaDiagramaSankeyNode> nodes) {
		this.nodes = nodes;
	}

	public List<InterfaceSistemaDiagramaSankeyLink> getLinks() {
		return links;
	}

	public void setLinks(List<InterfaceSistemaDiagramaSankeyLink> links) {
		this.links = links;
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

	public String getCoTipoArtefato() {
		return coTipoArtefato;
	}

	public void setCoTipoArtefato(String coTipoArtefato) {
		this.coTipoArtefato = coTipoArtefato;
	}

	public String getCoSistema() {
		return coSistema;
	}

	public void setCoSistema(String coSistema) {
		this.coSistema = coSistema;
	}

	public Long getCoArtefato() {
		return coArtefato;
	}

	public void setCoArtefato(Long coArtefato) {
		this.coArtefato = coArtefato;
	}

	public Long getCoArtefatoPai() {
		return coArtefatoPai;
	}

	public void setCoArtefatoPai(Long coArtefatoPai) {
		this.coArtefatoPai = coArtefatoPai;
	}

	public String getNoNomeArtefatoPai() {
		return noNomeArtefatoPai;
	}

	public void setNoNomeArtefatoPai(String noNomeArtefatoPai) {
		this.noNomeArtefatoPai = noNomeArtefatoPai;
	}

	public String getNoNomeExibicaoPai() {
		return noNomeExibicaoPai;
	}

	public void setNoNomeExibicaoPai(String noNomeExibicaoPai) {
		this.noNomeExibicaoPai = noNomeExibicaoPai;
	}

	public String getNoNomeInternoPai() {
		return noNomeInternoPai;
	}

	public void setNoNomeInternoPai(String noNomeInternoPai) {
		this.noNomeInternoPai = noNomeInternoPai;
	}

	public String getCoTipoArtefatoPai() {
		return coTipoArtefatoPai;
	}

	public void setCoTipoArtefatoPai(String coTipoArtefatoPai) {
		this.coTipoArtefatoPai = coTipoArtefatoPai;
	}

	public String getCoSistemaPai() {
		return coSistemaPai;
	}

	public void setCoSistemaPai(String coSistemaPai) {
		this.coSistemaPai = coSistemaPai;
	}

	public String getCaminhoCoArtefato() {
		return caminhoCoArtefato;
	}

	public void setCaminhoCoArtefato(String caminhoCoArtefato) {
		this.caminhoCoArtefato = caminhoCoArtefato;
	}

	public String getCoTipoRelacionamentoInicial() {
		return coTipoRelacionamentoInicial;
	}

	public void setCoTipoRelacionamentoInicial(String coTipoRelacionamentoInicial) {
		this.coTipoRelacionamentoInicial = coTipoRelacionamentoInicial;
	}

	public String getCoSistemaDestino() {
		return coSistemaDestino;
	}

	public void setCoSistemaDestino(String coSistemaDestino) {
		this.coSistemaDestino = coSistemaDestino;
	}

}
