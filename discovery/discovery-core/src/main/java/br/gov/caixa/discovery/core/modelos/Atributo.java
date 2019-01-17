package br.gov.caixa.discovery.core.modelos;

import br.gov.caixa.discovery.core.tipos.TipoAtributo;

public class Atributo {
	private TipoAtributo tipoAtributo;
	private String valor;
	private String descricao;
	private String tipo;

	public Atributo() {
		super();
	}

	public Atributo(TipoAtributo tipoAtributo, String valor, String descricao, String tipo) {
		super();
		this.tipoAtributo = tipoAtributo;
		this.valor = valor;
		this.descricao = descricao;
		this.tipo = tipo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		Atributo entry = (Atributo) obj;
		StringBuilder sb1 = new StringBuilder();
		sb1.append(entry.tipoAtributo);
		sb1.append(entry.valor);
		sb1.append(entry.descricao);
		sb1.append(entry.tipo);

		StringBuilder sb2 = new StringBuilder();
		sb2.append(this.tipoAtributo);
		sb2.append(this.valor);
		sb2.append(this.descricao);
		sb2.append(this.tipo);

		if (!sb2.toString().equals(sb1.toString())) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Tipo Atributo : (" + this.tipoAtributo + ") ");
		sb.append("Valor : (" + this.valor + ") ");
		sb.append("Tipo : (" + this.tipo + ") ");
		sb.append("Descrição : (" + this.descricao + ") ");

		return sb.toString();
	}

	public TipoAtributo getTipoAtributo() {
		return tipoAtributo;
	}

	public void setTipoAtributo(TipoAtributo tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
