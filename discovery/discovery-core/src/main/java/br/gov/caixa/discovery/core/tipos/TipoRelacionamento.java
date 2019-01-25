package br.gov.caixa.discovery.core.tipos;

public enum TipoRelacionamento {
	CONTROL_M("CONTROL-M"), INTERFACE("INTERFACE"), NORMAL("NORMAL");

	private String value;

	private TipoRelacionamento(String value) {
		this.value = value;
	}

	public String get() {
		return this.value;
	}
}
