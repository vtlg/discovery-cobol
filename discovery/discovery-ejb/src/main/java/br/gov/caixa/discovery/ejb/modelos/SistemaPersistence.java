package br.gov.caixa.discovery.ejb.modelos;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "TBL_SISTEMA")
@Table(schema = "public", uniqueConstraints = { @UniqueConstraint(columnNames = { "CO_SISTEMA" }) })
public class SistemaPersistence {

	@Id
	@Column(name = "CO_SISTEMA", length = 100)
	private String coSistema;

	@Column(name = "DE_SISTEMA", length = 500)
	private String deSistema;

	@OneToMany(mappedBy = "sistema", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ArtefatoPersistence> listaArtefatos;

	public List<ArtefatoPersistence> getListaArtefatos() {
		return listaArtefatos;
	}

	public void setListaArtefatos(List<ArtefatoPersistence> listaArtefatos) {
		this.listaArtefatos = listaArtefatos;
	}

	public String getCoSistema() {
		return coSistema;
	}

	public String getDeSistema() {
		return deSistema;
	}

	public void setDeSistema(String deSistema) {
		this.deSistema = deSistema;
	}

	public void setCoSistema(String coSistema) {
		this.coSistema = coSistema;
	}

	public SistemaPersistence(String coSistema, String deSistema) {
		super();
		this.coSistema = coSistema;
		this.deSistema = deSistema;
	}

}
