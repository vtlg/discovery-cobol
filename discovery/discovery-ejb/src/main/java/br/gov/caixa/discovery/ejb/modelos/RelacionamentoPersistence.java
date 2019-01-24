package br.gov.caixa.discovery.ejb.modelos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity(name = "TBL_RELACIONAMENTO_ARTEFATO")
@Table(schema = "public", uniqueConstraints = { @UniqueConstraint(columnNames = { "CO_ARTEFATO", "CO_ARTEFATO_PAI",
		"CO_ARTEFATO_ANTERIOR", "CO_ARTEFATO_POSTERIOR", "CO_ARTEFATO_PRIMEIRO", "CO_ARTEFATO_ULTIMO" }) })
public class RelacionamentoPersistence {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CO_RELACIONAMENTO")
	private Long coRelacionamento;

	@Column(name = "CO_ARTEFATO")
	private Long coArtefato;

	@Column(name = "CO_ARTEFATO_PAI")
	private Long coArtefatoPai;

	@Column(name = "CO_ARTEFATO_ANTERIOR")
	private Long coArtefatoAnterior;

	@Column(name = "CO_ARTEFATO_POSTERIOR")
	private Long coArtefatoPosterior;

	@Column(name = "CO_ARTEFATO_PRIMEIRO")
	private Long coArtefatoPrimeiro;

	@Column(name = "CO_ARTEFATO_ULTIMO")
	private Long coArtefatoUltimo;

	@Column(name = "IC_INCLUSAO_MANUAL", columnDefinition = "boolean NOT NULL DEFAULT false")
	private boolean icInclusaoManual = false;

	@Column(name = "IC_INCLUSAO_MALHA", columnDefinition = "boolean NOT NULL DEFAULT false")
	private boolean icInclusaoMalha = false;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CO_ARTEFATO", referencedColumnName = "CO_ARTEFATO", insertable = false, updatable = false)
	private ArtefatoPersistence artefato;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CO_ARTEFATO_PAI", referencedColumnName = "CO_ARTEFATO", insertable = false, updatable = false)
	private ArtefatoPersistence artefatoPai;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CO_ARTEFATO_ANTERIOR", referencedColumnName = "CO_ARTEFATO", insertable = false, updatable = false)
	private ArtefatoPersistence artefatoAnterior;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CO_ARTEFATO_POSTERIOR", referencedColumnName = "CO_ARTEFATO", insertable = false, updatable = false)
	private ArtefatoPersistence artefatoPosterior;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CO_ARTEFATO_PRIMEIRO", referencedColumnName = "CO_ARTEFATO", insertable = false, updatable = false)
	private ArtefatoPersistence artefatoPrimeiro;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CO_ARTEFATO_ULTIMO", referencedColumnName = "CO_ARTEFATO", insertable = false, updatable = false)
	private ArtefatoPersistence artefatoUltimo;

	@OneToMany(mappedBy = "relacionamento", fetch = FetchType.LAZY)
	private List<AtributoRelacionamentoPersistence> listaAtributos;

	@Transient
	private List<AtributoRelacionamentoPersistence> transientListaAtributos = new ArrayList<>();

	public void adicionarAtributoTransient(AtributoRelacionamentoPersistence entry) {
		if (transientListaAtributos == null) {
			transientListaAtributos = new ArrayList<>();
		}

		transientListaAtributos.add(entry);
	}

	public Long getCoRelacionamento() {
		return coRelacionamento;
	}

	public void setCoRelacionamento(Long coRelacionamento) {
		this.coRelacionamento = coRelacionamento;
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

	public Long getCoArtefatoAnterior() {
		return coArtefatoAnterior;
	}

	public void setCoArtefatoAnterior(Long coArtefatoAnterior) {
		this.coArtefatoAnterior = coArtefatoAnterior;
	}

	public Long getCoArtefatoPosterior() {
		return coArtefatoPosterior;
	}

	public void setCoArtefatoPosterior(Long coArtefatoPosterior) {
		this.coArtefatoPosterior = coArtefatoPosterior;
	}

	public Long getCoArtefatoPrimeiro() {
		return coArtefatoPrimeiro;
	}

	public void setCoArtefatoPrimeiro(Long coArtefatoPrimeiro) {
		this.coArtefatoPrimeiro = coArtefatoPrimeiro;
	}

	public Long getCoArtefatoUltimo() {
		return coArtefatoUltimo;
	}

	public void setCoArtefatoUltimo(Long coArtefatoUltimo) {
		this.coArtefatoUltimo = coArtefatoUltimo;
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

	public ArtefatoPersistence getArtefato() {
		return artefato;
	}

	public void setArtefato(ArtefatoPersistence artefato) {
		this.artefato = artefato;
	}

	public ArtefatoPersistence getArtefatoPai() {
		return artefatoPai;
	}

	public void setArtefatoPai(ArtefatoPersistence artefatoPai) {
		this.artefatoPai = artefatoPai;
	}

	public ArtefatoPersistence getArtefatoAnterior() {
		return artefatoAnterior;
	}

	public void setArtefatoAnterior(ArtefatoPersistence artefatoAnterior) {
		this.artefatoAnterior = artefatoAnterior;
	}

	public ArtefatoPersistence getArtefatoPosterior() {
		return artefatoPosterior;
	}

	public void setArtefatoPosterior(ArtefatoPersistence artefatoPosterior) {
		this.artefatoPosterior = artefatoPosterior;
	}

	public ArtefatoPersistence getArtefatoPrimeiro() {
		return artefatoPrimeiro;
	}

	public void setArtefatoPrimeiro(ArtefatoPersistence artefatoPrimeiro) {
		this.artefatoPrimeiro = artefatoPrimeiro;
	}

	public ArtefatoPersistence getArtefatoUltimo() {
		return artefatoUltimo;
	}

	public void setArtefatoUltimo(ArtefatoPersistence artefatoUltimo) {
		this.artefatoUltimo = artefatoUltimo;
	}

	public List<AtributoRelacionamentoPersistence> getTransientListaAtributos() {
		return transientListaAtributos;
	}

	public void setTransientListaAtributos(List<AtributoRelacionamentoPersistence> transientListaAtributos) {
		this.transientListaAtributos = transientListaAtributos;
	}

	public List<AtributoRelacionamentoPersistence> getListaAtributos() {
		return listaAtributos;
	}

	public void setListaAtributos(List<AtributoRelacionamentoPersistence> listaAtributos) {
		this.listaAtributos = listaAtributos;
	}

}
