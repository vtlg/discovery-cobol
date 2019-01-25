package br.gov.caixa.discovery.ws.modelos;

import java.util.List;

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

	@JsonProperty("icInclusaoManual")
	private boolean icInclusaoManual = false;

	@JsonProperty("icInclusaoMalha")
	private boolean icInclusaoMalha = false;

	@JsonProperty("atributos")
	private List<AtributoDomain> atributos;

	@JsonProperty("tipoRelacionamento")
	private TipoDomain tipoRelacionamento;

	@JsonProperty("descendente")
	private ArtefatoDomain descendente;

	@JsonProperty("ascendente")
	private ArtefatoDomain ascendente;

	@JsonProperty("anterior")
	private ArtefatoDomain anterior;

	@JsonProperty("posterior")
	private ArtefatoDomain posterior;

	@JsonProperty("primeiro")
	private ArtefatoDomain primeiro;

	@JsonProperty("ultimo")
	private ArtefatoDomain ultimo;

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

	public List<AtributoDomain> getAtributos() {
		return atributos;
	}

	public void setAtributos(List<AtributoDomain> atributos) {
		this.atributos = atributos;
	}

	public ArtefatoDomain getAscendente() {
		return ascendente;
	}

	public void setAscendente(ArtefatoDomain ascendente) {
		this.ascendente = ascendente;
	}

	public ArtefatoDomain getAnterior() {
		return anterior;
	}

	public void setAnterior(ArtefatoDomain anterior) {
		this.anterior = anterior;
	}

	public ArtefatoDomain getPosterior() {
		return posterior;
	}

	public void setPosterior(ArtefatoDomain posterior) {
		this.posterior = posterior;
	}

	public ArtefatoDomain getPrimeiro() {
		return primeiro;
	}

	public void setPrimeiro(ArtefatoDomain primeiro) {
		this.primeiro = primeiro;
	}

	public ArtefatoDomain getUltimo() {
		return ultimo;
	}

	public void setUltimo(ArtefatoDomain ultimo) {
		this.ultimo = ultimo;
	}

	public ArtefatoDomain getDescendente() {
		return descendente;
	}

	public void setDescendente(ArtefatoDomain descendente) {
		this.descendente = descendente;
	}

	public TipoDomain getTipoRelacionamento() {
		return tipoRelacionamento;
	}

	public void setTipoRelacionamento(TipoDomain tipoRelacionamento) {
		this.tipoRelacionamento = tipoRelacionamento;
	}

}
