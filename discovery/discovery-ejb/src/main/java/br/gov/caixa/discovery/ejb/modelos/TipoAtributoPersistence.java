package br.gov.caixa.discovery.ejb.modelos;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity()
@DiscriminatorValue("ATRIBUTO")
public class TipoAtributoPersistence extends TipoPersistence {

	@OneToMany(mappedBy = "tipoAtributo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<AtributoPersistence> listaAtributos;

	public List<AtributoPersistence> getListaAtributos() {
		return listaAtributos;
	}

	public void setListaAtributos(List<AtributoPersistence> listaAtributos) {
		this.listaAtributos = listaAtributos;
	}

}
