package br.gov.caixa.discovery.ejb.modelos;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "TBL_TIPO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CO_TABELA")
@Table(schema = "public", uniqueConstraints = { @UniqueConstraint(columnNames = { "CO_TIPO" }) })
public class TipoPersistence {

	@Id
	@Column(name = "CO_TIPO", length = 100)
	private String coTipo;

	@Column(name = "CO_TABELA", length = 100, insertable = false, updatable = false)
	private String coTabela;

	@Column(name = "DE_TIPO", length = 500)
	private String deTipo;

	@Column(name = "IC_PESQUISAVEL", columnDefinition = "boolean NOT NULL DEFAULT true")
	private Boolean icPesquisavel;

	@Column(name = "IC_EXIBIR_ATRIBUTO", columnDefinition = "boolean NOT NULL DEFAULT true")
	private Boolean icExibirAtributo;

	@Column(name = "IC_EXIBIR_GRAFO", columnDefinition = "boolean NOT NULL DEFAULT true")
	private Boolean icExibirGrafo;

	@Column(name = "CO_COR", columnDefinition = "character varying NOT NULL DEFAULT '#000000'::character varying")
	private String coCor;

	@Column(name = "CO_COR_BORDA", columnDefinition = "character varying NOT NULL DEFAULT '#000000'::character varying")
	private String coCorBorda;

	@Column(name = "NU_LARGURA_BORDA", columnDefinition = "integer NOT NULL DEFAULT 2")
	private int nuLarguraBorda;

	public String getCoTipo() {
		return coTipo;
	}

	public void setCoTipo(String coTipo) {
		this.coTipo = coTipo;
	}

	public String getDeTipo() {
		return deTipo;
	}

	public void setDeTipo(String deTipo) {
		this.deTipo = deTipo;
	}

	public Boolean getIcPesquisavel() {
		return icPesquisavel;
	}

	public void setIcPesquisavel(Boolean icPesquisavel) {
		this.icPesquisavel = icPesquisavel;
	}

	public Boolean getIcExibirAtributo() {
		return icExibirAtributo;
	}

	public void setIcExibirAtributo(Boolean icExibirAtributo) {
		this.icExibirAtributo = icExibirAtributo;
	}

	public Boolean getIcExibirGrafo() {
		return icExibirGrafo;
	}

	public void setIcExibirGrafo(Boolean icExibirGrafo) {
		this.icExibirGrafo = icExibirGrafo;
	}

	public String getCoCor() {
		return coCor;
	}

	public void setCoCor(String coCor) {
		this.coCor = coCor;
	}

	public int getNuLarguraBorda() {
		return nuLarguraBorda;
	}

	public void setNuLarguraBorda(int nuLarguraBorda) {
		this.nuLarguraBorda = nuLarguraBorda;
	}

	public String getCoCorBorda() {
		return coCorBorda;
	}

	public void setCoCorBorda(String coCorBorda) {
		this.coCorBorda = coCorBorda;
	}

	public TipoPersistence() {
		super();
	}

	public String getCoTabela() {
		return coTabela;
	}

	public void setCoTabela(String coTabela) {
		this.coTabela = coTabela;
	}

	public TipoPersistence(String coTipo, String deTipo, Boolean icPesquisavel, Boolean icExibirAtributo,
			Boolean icExibirGrafo, String coCor, String coCorBorda) {
		super();
		this.coTipo = coTipo;
		this.deTipo = deTipo;
		this.icPesquisavel = icPesquisavel;
		this.icExibirAtributo = icExibirAtributo;
		this.icExibirGrafo = icExibirGrafo;
		this.coCor = coCor;
		this.coCorBorda = coCorBorda;
	}

	public TipoPersistence(String coTipo, String coTabela, String deTipo, Boolean icPesquisavel,
			Boolean icExibirAtributo, Boolean icExibirGrafo, String coCor, String coCorBorda) {
		super();
		this.coTipo = coTipo;
		this.coTabela = coTabela;
		this.deTipo = deTipo;
		this.icPesquisavel = icPesquisavel;
		this.icExibirAtributo = icExibirAtributo;
		this.icExibirGrafo = icExibirGrafo;
		this.coCor = coCor;
		this.coCorBorda = coCorBorda;
	}

	public TipoPersistence(String coTipo, String coTabela, String deTipo, Boolean icPesquisavel,
			Boolean icExibirAtributo, Boolean icExibirGrafo, String coCor, String coCorBorda, int nuLarguraBorda) {
		super();
		this.coTipo = coTipo;
		this.coTabela = coTabela;
		this.deTipo = deTipo;
		this.icPesquisavel = icPesquisavel;
		this.icExibirAtributo = icExibirAtributo;
		this.icExibirGrafo = icExibirGrafo;
		this.coCor = coCor;
		this.coCorBorda = coCorBorda;
		this.nuLarguraBorda = nuLarguraBorda;
	}

}
