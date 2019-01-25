package br.gov.caixa.discovery.ejb.modelos;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity(name = "TBL_TIPO_RELACIONAMENTO_ARTEFATO")
@DiscriminatorValue("RELACIONAMENTO_ARTEFATO")
public class TipoRelacionamentoPersistence extends TipoPersistence {

	@OneToMany(mappedBy = "tipoRelacionamento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RelacionamentoPersistence> listaRelacionamentos;

	public List<RelacionamentoPersistence> getListaRelacionamentos() {
		return listaRelacionamentos;
	}

	public void setListaRelacionamentos(List<RelacionamentoPersistence> listaRelacionamentos) {
		this.listaRelacionamentos = listaRelacionamentos;
	}

}
