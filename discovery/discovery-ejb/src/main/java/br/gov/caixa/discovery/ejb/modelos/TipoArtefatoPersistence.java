package br.gov.caixa.discovery.ejb.modelos;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity(name="TBL_TIPO_ARTEFATO")
@DiscriminatorValue("ARTEFATO")
public class TipoArtefatoPersistence extends TipoPersistence {

	@OneToMany(mappedBy = "tipoArtefato", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ArtefatoPersistence> listaArtefatos;

	public List<ArtefatoPersistence> getListaArtefatos() {
		return listaArtefatos;
	}

	public void setListaArtefatos(List<ArtefatoPersistence> listaArtefatos) {
		this.listaArtefatos = listaArtefatos;
	}

}
