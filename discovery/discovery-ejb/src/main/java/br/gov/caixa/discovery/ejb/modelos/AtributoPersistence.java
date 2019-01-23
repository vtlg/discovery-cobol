package br.gov.caixa.discovery.ejb.modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "TBL_ATRIBUTO")
public class AtributoPersistence {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NU_SEQUENCIAL")
	private Long nuSequencial;

	@Column(name = "CO_ATRIBUTO", length = 100)
	private String coAtributo;

	@Column(name = "CO_EXTERNO")
	private Long coExterno;

	@Column(name = "CO_TABELA", length = 100)
	private String coTabela;

	@Column(name = "DE_VALOR", columnDefinition = "text DEFAULT text::' '")
	private String deValor;

	@Column(name = "IC_EDITAVEL", columnDefinition = "boolean NOT NULL DEFAULT false")
	private Boolean icEditavel = false;

	@Column(name = "IC_OPCIONAL", columnDefinition = "boolean NOT NULL DEFAULT false")
	private Boolean icOpcional = false;

	public String getCoAtributo() {
		return coAtributo;
	}

	public void setCoAtributo(String coAtributo) {
		this.coAtributo = coAtributo;
	}

	public Long getCoExterno() {
		return coExterno;
	}

	public void setCoExterno(Long coExterno) {
		this.coExterno = coExterno;
	}

	public String getCoTabela() {
		return coTabela;
	}

	public void setCoTabela(String coTabela) {
		this.coTabela = coTabela;
	}

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

}
