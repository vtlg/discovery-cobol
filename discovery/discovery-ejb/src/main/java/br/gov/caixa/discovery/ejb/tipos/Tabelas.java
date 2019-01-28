package br.gov.caixa.discovery.ejb.tipos;

public enum Tabelas {

	TBL_ARTEFATO("ARTEFATO"), TBL_RELACIONAMENTO_ARTEFATO("RELACIONAMENTO"),
	TBL_ATRIBUTO("ATRIBUTO"), TBL_TIPO("TIPO");

	private String value;

	private Tabelas(String value) {
		this.value = value;
	}

	public String get() {
		return this.value;
	}
}
