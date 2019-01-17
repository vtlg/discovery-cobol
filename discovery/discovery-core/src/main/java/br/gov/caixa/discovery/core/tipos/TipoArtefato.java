package br.gov.caixa.discovery.core.tipos;

public enum TipoArtefato {
	COPYBOOK("COPYBOOK"),
	COPYBOOK_VARIAVEL("COPYBOOK-VARIAVEL"),
	PROGRAMA_COBOL("PROGRAMA-COBOL"),
	PROGRAMA_COBOL_PARAGRAFO("PROGRAMA-COBOL-PARAGRAFO"),
	CICS_TRANSACTION("CICS-TRANSACTION"),
	FILE_DESCRIPTION("FILE-DESCRIPTION"),
	TABELA("TABELA"),
	TABELA_CAMPO("TABELA-CAMPO"),
	UTILITARIO("UTILITARIO"),
	DECLARACAO_SQL("DECLARACAO-SQL"),
	DESCONHECIDO("DESCONHECIDO"),
	JCL("JCL"),
	JCL_STEP("JCL-STEP"),
	DSN("DSN"),
	;

	private String value;

	private TipoArtefato(String value) {
		this.value = value;
	}

	public String get() {
		return this.value;
	}
}
