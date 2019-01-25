package br.gov.caixa.discovery.ejb.modelos;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "TBL_ATRIBUTO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CO_TABELA")
public abstract class AtributoPersistence {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NU_SEQUENCIAL")
	private Long nuSequencial;

	@Column(name = "CO_TIPO_ATRIBUTO", length = 100)
	private String coTipoAtributo;

	@Column(name = "CO_EXTERNO")
	private Long coExterno;

	@Column(name = "DE_VALOR", columnDefinition = "text DEFAULT text::' '")
	private String deValor;

	@Column(name = "IC_EDITAVEL", columnDefinition = "boolean NOT NULL DEFAULT false")
	private Boolean icEditavel = false;

	@Column(name = "IC_OPCIONAL", columnDefinition = "boolean NOT NULL DEFAULT false")
	private Boolean icOpcional = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_TIPO_ATRIBUTO", referencedColumnName = "CO_TIPO", insertable = false, updatable = false)
	private TipoAtributoPersistence tipoAtributo;

	public String getCoTipoAtributo() {
		return coTipoAtributo;
	}

	public void setCoTipoAtributo(String coTipoAtributo) {
		this.coTipoAtributo = coTipoAtributo;
	}

	public Long getCoExterno() {
		return coExterno;
	}

	public void setCoExterno(Long coExterno) {
		this.coExterno = coExterno;
	}

	/*
	 * public String getCoTabela() { return coTabela; }
	 * 
	 * public void setCoTabela(String coTabela) { this.coTabela = coTabela; }
	 */

	public String getDeValor() {
		return deValor;
	}

	public void setDeValor(String deValor) {
		this.deValor = deValor;
	}

	public Boolean getIcOpcional() {
		return icOpcional;
	}

	public void setIcOpcional(Boolean icOpcional) {
		this.icOpcional = icOpcional;
	}

	public Boolean getIcEditavel() {
		return icEditavel;
	}

	public void setIcEditavel(Boolean icEditavel) {
		this.icEditavel = icEditavel;
	}

	public Long getNuSequencial() {
		return nuSequencial;
	}

	public void setNuSequencial(Long nuSequencial) {
		this.nuSequencial = nuSequencial;
	}

	public TipoAtributoPersistence getTipoAtributo() {
		return tipoAtributo;
	}

	public void setTipoAtributo(TipoAtributoPersistence tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}

}
