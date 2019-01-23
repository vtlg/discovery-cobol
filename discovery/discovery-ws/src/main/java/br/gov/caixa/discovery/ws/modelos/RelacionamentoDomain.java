package br.gov.caixa.discovery.ws.modelos;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement(name = "relacionamento")
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class RelacionamentoDomain {

	@JsonProperty("coRelacionamento")
	private Long coRelacionamento;

	// @JsonProperty("coArtefato")
	// private Long coArtefato;

	// @JsonProperty("coArtefatoPai")
	// private Long coArtefatoPai;

	// @JsonProperty("coArtefatoAnterior")
	// private Long coArtefatoAnterior;

	// @JsonProperty("coArtefatoPosterior")
	// private Long coArtefatoPosterior;

	// @JsonProperty("coArtefatoPrimeiro")
	// private Long coArtefatoPrimeiro;

	// @JsonProperty("coArtefatoUltimo")
	// private Long coArtefatoUltimo;

	@JsonProperty("icInclusaoManual")
	private boolean icInclusaoManual = false;

	@JsonProperty("icInclusaoMalha")
	private boolean icInclusaoMalha = false;

	@JsonProperty("artefato")
	private ArtefatoDomain artefato;

	@JsonProperty("artefatoPai")
	private ArtefatoDomain artefatoPai;

	@JsonProperty("artefatoAnterior")
	private ArtefatoDomain artefatoAnterior;

	@JsonProperty("artefatoPosterior")
	private ArtefatoDomain artefatoPosterior;

	@JsonProperty("artefatoPrimeiro")
	private ArtefatoDomain artefatoPrimeiro;

	@JsonProperty("artefatoUltimo")
	private ArtefatoDomain artefatoUltimo;

	public Long getCoRelacionamento() {
		return coRelacionamento;
	}

	public void setCoRelacionamento(Long coRelacionamento) {
		this.coRelacionamento = coRelacionamento;
	}

	public boolean isIcInclusaoManual() {
		return icInclusaoManual;
	}

	public void setIcInclusaoManual(boolean icInclusaoManual) {
		this.icInclusaoManual = icInclusaoManual;
	}

	public boolean isIcInclusaoMalha() {
		return icInclusaoMalha;
	}

	public void setIcInclusaoMalha(boolean icInclusaoMalha) {
		this.icInclusaoMalha = icInclusaoMalha;
	}

	public ArtefatoDomain getArtefato() {
		return artefato;
	}

	public void setArtefato(ArtefatoDomain artefato) {
		this.artefato = artefato;
	}

	public ArtefatoDomain getArtefatoPai() {
		return artefatoPai;
	}

	public void setArtefatoPai(ArtefatoDomain artefatoPai) {
		this.artefatoPai = artefatoPai;
	}

	public ArtefatoDomain getArtefatoAnterior() {
		return artefatoAnterior;
	}

	public void setArtefatoAnterior(ArtefatoDomain artefatoAnterior) {
		this.artefatoAnterior = artefatoAnterior;
	}

	public ArtefatoDomain getArtefatoPosterior() {
		return artefatoPosterior;
	}

	public void setArtefatoPosterior(ArtefatoDomain artefatoPosterior) {
		this.artefatoPosterior = artefatoPosterior;
	}

	public ArtefatoDomain getArtefatoPrimeiro() {
		return artefatoPrimeiro;
	}

	public void setArtefatoPrimeiro(ArtefatoDomain artefatoPrimeiro) {
		this.artefatoPrimeiro = artefatoPrimeiro;
	}

	public ArtefatoDomain getArtefatoUltimo() {
		return artefatoUltimo;
	}

	public void setArtefatoUltimo(ArtefatoDomain artefatoUltimo) {
		this.artefatoUltimo = artefatoUltimo;
	}

}
