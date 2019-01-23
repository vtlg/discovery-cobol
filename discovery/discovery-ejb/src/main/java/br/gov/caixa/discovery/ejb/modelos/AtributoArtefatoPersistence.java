package br.gov.caixa.discovery.ejb.modelos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "TBL_ATRIBUTO_ARTEFATO")
@DiscriminatorValue("TBL_ARTEFATO")
public class AtributoArtefatoPersistence extends AtributoPersistence {
}
