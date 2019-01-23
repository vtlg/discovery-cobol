package br.gov.caixa.discovery.ejb.modelos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "TBL_ATRIBUTO_RELACIONAMENTO")
@DiscriminatorValue("TBL_RELACIONAMENTO_ARTEFATO")
public class AtributoRelacionamentoPersistence extends AtributoPersistence {
}
