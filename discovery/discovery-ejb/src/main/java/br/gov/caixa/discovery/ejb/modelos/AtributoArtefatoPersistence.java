package br.gov.caixa.discovery.ejb.modelos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "TBL_ATRIBUTO_ARTEFATO")
@DiscriminatorValue("TBL_ARTEFATO")
public class AtributoArtefatoPersistence extends AtributoPersistence {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CO_EXTERNO", referencedColumnName = "CO_ARTEFATO", insertable = false, updatable = false)
	private ArtefatoPersistence artefato;
}
