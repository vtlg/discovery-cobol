package br.gov.caixa.discovery.ejb.view;

import java.io.Serializable;

public class InterfaceSistemaId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5884740961061541210L;
	private Long coArtefato;
	private Long coArtefatoPai;
	private String caminhoCoArtefato;

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

	public String getCaminhoCoArtefato() {
		return caminhoCoArtefato;
	}

	public void setCaminhoCoArtefato(String caminhoCoArtefato) {
		this.caminhoCoArtefato = caminhoCoArtefato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caminhoCoArtefato == null) ? 0 : caminhoCoArtefato.hashCode());
		result = prime * result + ((coArtefato == null) ? 0 : coArtefato.hashCode());
		result = prime * result + ((coArtefatoPai == null) ? 0 : coArtefatoPai.hashCode());
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
		InterfaceSistemaId other = (InterfaceSistemaId) obj;
		if (caminhoCoArtefato == null) {
			if (other.caminhoCoArtefato != null)
				return false;
		} else if (!caminhoCoArtefato.equals(other.caminhoCoArtefato))
			return false;
		if (coArtefato == null) {
			if (other.coArtefato != null)
				return false;
		} else if (!coArtefato.equals(other.coArtefato))
			return false;
		if (coArtefatoPai == null) {
			if (other.coArtefatoPai != null)
				return false;
		} else if (!coArtefatoPai.equals(other.coArtefatoPai))
			return false;
		return true;
	}

}
