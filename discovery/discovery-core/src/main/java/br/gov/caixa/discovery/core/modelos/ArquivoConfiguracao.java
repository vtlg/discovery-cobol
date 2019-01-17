package br.gov.caixa.discovery.core.modelos;

public class ArquivoConfiguracao {
	String ambiente;
	String sistema;
	String tipo;
	String caminhoPasta;
	String plataforma;

	String caminhoPastaAcessoria;
	String[] filtroInclusaoArquivoLiteral;
	String[] filtroExclusaoArquivoLiteral;
	String[] filtroInclusaoArquivoRegex;
	String[] filtroExclusaoArquivoRegex;
	String[] tiposNativosLiteral;
	String[] tiposNativosRegex;

	String[] metodosChamadaEaseLiteral;
	String[] metodosChamadaEaseRegex;

	String usuario;
	String senha;
	String ipServidor;
	String bancoDados;
	String urlConexao;
	String porta;

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCaminhoPasta() {
		return caminhoPasta;
	}

	public void setCaminhoPasta(String caminhoPasta) {
		this.caminhoPasta = caminhoPasta;
	}

	public String getCaminhoPastaAcessoria() {
		return caminhoPastaAcessoria;
	}

	public void setCaminhoPastaAcessoria(String caminhoPastaAcessoria) {
		this.caminhoPastaAcessoria = caminhoPastaAcessoria;
	}

	public String[] getFiltroInclusaoArquivoLiteral() {
		return filtroInclusaoArquivoLiteral;
	}

	public void setFiltroInclusaoArquivoLiteral(String[] filtroInclusaoArquivoLiteral) {
		this.filtroInclusaoArquivoLiteral = filtroInclusaoArquivoLiteral;
	}

	public String[] getFiltroExclusaoArquivoLiteral() {
		return filtroExclusaoArquivoLiteral;
	}

	public void setFiltroExclusaoArquivoLiteral(String[] filtroExclusaoArquivoLiteral) {
		this.filtroExclusaoArquivoLiteral = filtroExclusaoArquivoLiteral;
	}

	public String[] getFiltroInclusaoArquivoRegex() {
		return filtroInclusaoArquivoRegex;
	}

	public void setFiltroInclusaoArquivoRegex(String[] filtroInclusaoArquivoRegex) {
		this.filtroInclusaoArquivoRegex = filtroInclusaoArquivoRegex;
	}

	public String[] getFiltroExclusaoArquivoRegex() {
		return filtroExclusaoArquivoRegex;
	}

	public void setFiltroExclusaoArquivoRegex(String[] filtroExclusaoArquivoRegex) {
		this.filtroExclusaoArquivoRegex = filtroExclusaoArquivoRegex;
	}

	public String[] getTiposNativosLiteral() {
		return tiposNativosLiteral;
	}

	public void setTiposNativosLiteral(String[] tiposNativosLiteral) {
		this.tiposNativosLiteral = tiposNativosLiteral;
	}

	public String[] getTiposNativosRegex() {
		return tiposNativosRegex;
	}

	public void setTiposNativosRegex(String[] tiposNativosRegex) {
		this.tiposNativosRegex = tiposNativosRegex;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getIpServidor() {
		return ipServidor;
	}

	public void setIpServidor(String ipServidor) {
		this.ipServidor = ipServidor;
	}

	public String getBancoDados() {
		return bancoDados;
	}

	public void setBancoDados(String bancoDados) {
		this.bancoDados = bancoDados;
	}

	public String getUrlConexao() {
		return urlConexao;
	}

	public void setUrlConexao(String urlConexao) {
		this.urlConexao = urlConexao;
	}

	public String getPlataforma() {
		return plataforma;
	}

	public void setPlataforma(String plataforma) {
		this.plataforma = plataforma;
	}

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public String[] getMetodosChamadaEaseLiteral() {
		return metodosChamadaEaseLiteral;
	}

	public void setMetodosChamadaEaseLiteral(String[] metodosChamadaEaseLiteral) {
		this.metodosChamadaEaseLiteral = metodosChamadaEaseLiteral;
	}

	public String[] getMetodosChamadaEaseRegex() {
		return metodosChamadaEaseRegex;
	}

	public void setMetodosChamadaEaseRegex(String[] metodosChamadaEaseRegex) {
		this.metodosChamadaEaseRegex = metodosChamadaEaseRegex;
	}
}
