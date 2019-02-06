package br.gov.caixa.discovery.ejb.view;

import java.io.Serializable;

public class RelacionamentoId implements Serializable {

	private Long coRelacionamentoRel;
	private Long coNuSequencialAtr;
	private Long coArtefatoAsc;
	private Long coArtefatoDesc;

	public Long getCoRelacionamentoRel() {
		return coRelacionamentoRel;
	}

	public void setCoRelacionamentoRel(Long coRelacionamentoRel) {
		this.coRelacionamentoRel = coRelacionamentoRel;
	}

	public Long getCoNuSequencialAtr() {
		return coNuSequencialAtr;
	}

	public void setCoNuSequencialAtr(Long coNuSequencialAtr) {
		this.coNuSequencialAtr = coNuSequencialAtr;
	}

	public Long getCoArtefatoAsc() {
		return coArtefatoAsc;
	}

	public void setCoArtefatoAsc(Long coArtefatoAsc) {
		this.coArtefatoAsc = coArtefatoAsc;
	}

	public Long getCoArtefatoDesc() {
		return coArtefatoDesc;
	}

	public void setCoArtefatoDesc(Long coArtefatoDesc) {
		this.coArtefatoDesc = coArtefatoDesc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coArtefatoAsc == null) ? 0 : coArtefatoAsc.hashCode());
		result = prime * result + ((coArtefatoDesc == null) ? 0 : coArtefatoDesc.hashCode());
		result = prime * result + ((coNuSequencialAtr == null) ? 0 : coNuSequencialAtr.hashCode());
		result = prime * result + ((coRelacionamentoRel == null) ? 0 : coRelacionamentoRel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelacionamentoId other = (RelacionamentoId) obj;
		if (coArtefatoAsc == null) {
			if (other.coArtefatoAsc != null)
				return false;
		} else if (!coArtefatoAsc.equals(other.coArtefatoAsc))
			return false;
		if (coArtefatoDesc == null) {
			if (other.coArtefatoDesc != null)
				return false;
		} else if (!coArtefatoDesc.equals(other.coArtefatoDesc))
			return false;
		if (coNuSequencialAtr == null) {
			if (other.coNuSequencialAtr != null)
				return false;
		} else if (!coNuSequencialAtr.equals(other.coNuSequencialAtr))
			return false;
		if (coRelacionamentoRel == null) {
			if (other.coRelacionamentoRel != null)
				return false;
		} else if (!coRelacionamentoRel.equals(other.coRelacionamentoRel))
			return false;
		return true;
	}
}
