package br.gov.caixa.discovery.core.tipos;

public enum TipoAmbiente {
	DES("DES"), TQS("TQS"), PRE("PRE"), REL("REL"), PRD("PRD"), HMP("HMP"), DESCONHECIDO("DESCONHECIDO");

	private String value;

	private TipoAmbiente(String value) {
		this.value = value;
	}

	public String get() {
		return this.value;
	}
}
