package br.gov.caixa.discovery.ejb.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "VW_ARTEFATO_COUNTS")
public class ArtefatoCountsView {

	@Id
	@Column(name = "CO_ARTEFATO")
	private Long coArtefato;

	@Column(name = "COUNT_RELACIONAMENTO")
	private Long countRelacionamento;

	@Column(name = "COUNT_RELACIONAMENTO_INTERFACE")
	private Long countRelacionamentoInterface;

	@Column(name = "COUNT_RELACIONAMENTO_NORMAL")
	private Long countRelacionamentoNormal;

	@Column(name = "COUNT_RELACIONAMENTO_CONTROL_M")
	private Long countRelacionamentoControlM;

	public Long getCoArtefato() {
		return coArtefato;
	}

	public void setCoArtefato(Long coArtefato) {
		this.coArtefato = coArtefato;
	}

	public Long getCountRelacionamento() {
		return countRelacionamento;
	}

	public void setCountRelacionamento(Long countRelacionamento) {
		this.countRelacionamento = countRelacionamento;
	}

	public Long getCountRelacionamentoInterface() {
		return countRelacionamentoInterface;
	}

	public void setCountRelacionamentoInterface(Long countRelacionamentoInterface) {
		this.countRelacionamentoInterface = countRelacionamentoInterface;
	}

	public Long getCountRelacionamentoNormal() {
		return countRelacionamentoNormal;
	}

	public void setCountRelacionamentoNormal(Long countRelacionamentoNormal) {
		this.countRelacionamentoNormal = countRelacionamentoNormal;
	}

	public Long getCountRelacionamentoControlM() {
		return countRelacionamentoControlM;
	}

	public void setCountRelacionamentoControlM(Long countRelacionamentoControlM) {
		this.countRelacionamentoControlM = countRelacionamentoControlM;
	}

	public ArtefatoCountsView() {
		super();
	}

	public ArtefatoCountsView(Long coArtefato, Long countRelacionamento, Long countRelacionamentoInterface,
			Long countRelacionamentoNormal, Long countRelacionamentoControlM) {
		super();
		this.coArtefato = coArtefato;
		this.countRelacionamento = countRelacionamento;
		this.countRelacionamentoInterface = countRelacionamentoInterface;
		this.countRelacionamentoNormal = countRelacionamentoNormal;
		this.countRelacionamentoControlM = countRelacionamentoControlM;
	}

}
