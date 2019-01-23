package br.gov.caixa.discovery.ejb.tipos;

public enum Tabelas {

	TBL_ARTEFATO("TBL_ARTEFATO"), TBL_RELACIONAMENTO_ARTEFATO("TBL_RELACIONAMENTO_ARTEFATO"),
	TBL_ATRIBUTO("TBL_ATRIBUTO"), TBL_TIPO_ARTEFATO("TBL_TIPO_ARTEFATO");

	private String value;

	private Tabelas(String value) {
		this.value = value;
	}

	public String get() {
		return this.value;
	}
}
