package br.gov.caixa.discovery.ejb.modelos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "TBL_ATRIBUTO_RELACIONAMENTO")
@DiscriminatorValue("RELACIONAMENTO_ARTEFATO")
public class AtributoRelacionamentoPersistence extends AtributoPersistence {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CO_EXTERNO", referencedColumnName = "CO_RELACIONAMENTO", insertable = false, updatable = false)
	private RelacionamentoPersistence relacionamento;

}
