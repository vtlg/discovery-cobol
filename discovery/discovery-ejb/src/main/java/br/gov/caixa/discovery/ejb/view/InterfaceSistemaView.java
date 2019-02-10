package br.gov.caixa.discovery.ejb.view;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "VW_INTERFACE_SISTEMA")
@IdClass(value = InterfaceSistemaId.class)
public class InterfaceSistemaView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -720840549351622878L;

	@Id
	@Column(name = "CO_ARTEFATO")
	private Long coArtefato;

	@Column(name = "NO_NOME_ARTEFATO")
	private String noNomeArtefato;

	@Column(name = "NO_NOME_EXIBICAO")
	private String noNomeExibicao;

	@Column(name = "NO_NOME_INTERNO")
	private String noNomeInterno;

	@Column(name = "CO_TIPO_ARTEFATO")
	private String coTipoArtefato;

	@Column(name = "CO_SISTEMA")
	private String coSistema;

	@Id
	@Column(name = "CO_ARTEFATO_PAI")
	private Long coArtefatoPai;

	@Column(name = "NO_NOME_ARTEFATO_PAI")
	private String noNomeArtefatoPai;

	@Column(name = "NO_NOME_EXIBICAO_PAI")
	private String noNomeExibicaoPai;

	@Column(name = "NO_NOME_INTERNO_PAI")
	private String noNomeInternoPai;

	@Column(name = "CO_TIPO_ARTEFATO_PAI")
	private String coTipoArtefatoPai;

	@Column(name = "CO_SISTEMA_PAI")
	private String coSistemaPai;

	@Id
	@Column(name = "CAMINHO_CO_ARTEFATO")
	private String caminhoCoArtefato;

	@Column(name = "CO_TIPO_RELACIONAMENTO_INICIAL")
	private String coTipoRelacionamentoInicial;

	@Column(name = "CO_SISTEMA_DESTINO")
	private String coSistemaDestino;

	public InterfaceSistemaView() {
		super();
	}

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
