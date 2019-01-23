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

@Entity(name = "TBL_TIPO_ARTEFATO")
@Table(schema = "public", uniqueConstraints = { @UniqueConstraint(columnNames = { "CO_TIPO_ARTEFATO" }) })
public class TipoArtefatoPersistence {

	@Id
	@Column(name = "CO_TIPO_ARTEFATO", length = 100)
	private String coTipoArtefato;

	@Column(name = "DE_TIPO_ARTEFATO", length = 500)
	private String deTipoArtefato;

	@Column(name = "IC_PESQUISAVEL", columnDefinition = "boolean DEFAULT true")
	private Boolean icPesquisavel;

	@Column(name = "IC_ATRIBUTO", columnDefinition = "boolean NOT NULL DEFAULT false")
	private Boolean icAtributo;

	@Column(name = "IC_GRAFO", columnDefinition = "boolean NOT NULL DEFAULT true")
	private Boolean icGrafo;

	@Column(name = "CO_COR", columnDefinition = "character varying NOT NULL DEFAULT '#000000'::character varying")
	private String coCor;

	@OneToMany(mappedBy = "tipoArtefato", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ArtefatoPersistence> listaArtefatos;

	public String getCoTipoArtefato() {
		return coTipoArtefato;
	}

	public void setCoTipoArtefato(String coTipoArtefato) {
		this.coTipoArtefato = coTipoArtefato;
	}

	public String getDeTipoArtefato() {
		return deTipoArtefato;
	}

	public void setDeTipoArtefato(String deTipoArtefato) {
		this.deTipoArtefato = deTipoArtefato;
	}

	public Boolean getIcPesquisavel() {
		return icPesquisavel;
	}

	public void setIcPesquisavel(Boolean icPesquisavel) {
		this.icPesquisavel = icPesquisavel;
	}

	public List<ArtefatoPersistence> getListaArtefatos() {
		return listaArtefatos;
	}

	public void setListaArtefatos(List<ArtefatoPersistence> listaArtefatos) {
		this.listaArtefatos = listaArtefatos;
	}

	public Boolean getIcAtributo() {
		return icAtributo;
	}

	public void setIcAtributo(Boolean icAtributo) {
		this.icAtributo = icAtributo;
	}

	public Boolean getIcGrafo() {
		return icGrafo;
	}

	public void setIcGrafo(Boolean icGrafo) {
		this.icGrafo = icGrafo;
	}

	public String getCoCor() {
		return coCor;
	}

	public void setCoCor(String coCor) {
		this.coCor = coCor;
	}

	public TipoArtefatoPersistence() {
		super();
	}

	public TipoArtefatoPersistence(String coTipoArtefato, String deTipoArtefato, Boolean icPesquisavel,
			Boolean icAtributo, Boolean icGrafo, String coCor, List<ArtefatoPersistence> listaArtefatos) {
		super();
		this.coTipoArtefato = coTipoArtefato;
		this.deTipoArtefato = deTipoArtefato;
		this.icPesquisavel = icPesquisavel;
		this.icAtributo = icAtributo;
		this.icGrafo = icGrafo;
		this.coCor = coCor;
		this.listaArtefatos = listaArtefatos;
	}

	public TipoArtefatoPersistence(String coTipoArtefato, String deTipoArtefato, Boolean icPesquisavel,
			Boolean icAtributo, Boolean icGrafo, String coCor) {
		super();
		this.coTipoArtefato = coTipoArtefato;
		this.deTipoArtefato = deTipoArtefato;
		this.icPesquisavel = icPesquisavel;
		this.icAtributo = icAtributo;
		this.icGrafo = icGrafo;
		this.coCor = coCor;
	}

}
