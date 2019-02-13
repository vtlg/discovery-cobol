package br.gov.caixa.discovery.core.modelos;

import java.util.ArrayList;
import java.util.List;

import br.gov.caixa.discovery.core.tipos.TipoAmbiente;
import br.gov.caixa.discovery.core.tipos.TipoArtefato;
import br.gov.caixa.discovery.core.tipos.TipoAtributo;
import br.gov.caixa.discovery.core.tipos.TipoRelacionamento;
import br.gov.caixa.discovery.core.utils.UtilsHandler;

public class Artefato {
	private String nome;
	private String nomeInterno;
	private String identificador;
	private TipoArtefato tipoArtefato;
	private TipoAmbiente ambiente;
	private TipoRelacionamento tipoRelacionamento = TipoRelacionamento.NORMAL;
	private String sistema;
	private StringBuilder representacaoTextual = new StringBuilder();
	private List<String> roteiroCodigo = new ArrayList<>();
	private List<String> roteiroVariavel = new ArrayList<>();
	private List<String> codigoCompleto = new ArrayList<>();
	private List<String> codigoFonte = new ArrayList<>();
	private List<String> codigoFonteTratado = new ArrayList<>();
	private List<String> comentarios = new ArrayList<>();
	private String hash;
	private String descricao;
	private String observacao;
	private byte[] arquivo;
	private String caminhoArquivo;
	private String nomeArquivo;
	private int posicao;
	private List<Artefato> artefatosRelacionados = new ArrayList<>();
	private List<Atributo> atributos = new ArrayList<>();
	private boolean excluir = false;
	private boolean isMalhaControlm = false;

	public Artefato() {
		super();
	}

	public Artefato(String nome, String nomeInterno, String identificador, TipoArtefato tipoArtefato,
			TipoAmbiente ambiente, String sistema, String caminhoArquivo, String nomeArquivo) {
		super();
		this.nome = nome;
		this.nomeInterno = nomeInterno;
		this.identificador = identificador;
		this.tipoArtefato = tipoArtefato;
		this.ambiente = ambiente;
		this.sistema = sistema;
		this.caminhoArquivo = caminhoArquivo;
		this.nomeArquivo = nomeArquivo;
	}

	public Artefato(String nome, String nomeInterno, String identificador, TipoArtefato tipoArtefato,
			TipoAmbiente ambiente, String sistema, String caminhoArquivo, String nomeArquivo,
			TipoRelacionamento tipoRelacionamento) {
		super();
		this.nome = nome;
		this.nomeInterno = nomeInterno;
		this.identificador = identificador;
		this.tipoArtefato = tipoArtefato;
		this.ambiente = ambiente;
		this.sistema = sistema;
		this.caminhoArquivo = caminhoArquivo;
		this.nomeArquivo = nomeArquivo;
		this.tipoRelacionamento = tipoRelacionamento;
	}

	public void adicionarRepresentacaoTextual(String entry) {
		if (this.representacaoTextual == null) {
			this.representacaoTextual = new StringBuilder();
		}

		this.representacaoTextual.append(entry);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		Artefato entry = (Artefato) obj;
		StringBuilder sb1 = new StringBuilder();
		sb1.append(entry.nome);
		sb1.append(entry.nomeInterno);
		sb1.append(entry.identificador);
		sb1.append(entry.tipoArtefato);
		sb1.append(entry.ambiente);
		sb1.append(entry.sistema);
		sb1.append(entry.tipoRelacionamento.get());

		StringBuilder sb2 = new StringBuilder();
		sb2.append(this.nome);
		sb2.append(this.nomeInterno);
		sb2.append(this.identificador);
		sb2.append(this.tipoArtefato);
		sb2.append(this.ambiente);
		sb2.append(this.sistema);
		sb2.append(this.tipoRelacionamento.get());
		
		System.out.println(" entry (" +  sb1.toString() +  ") == this (" + sb2.toString() + ")" );

		if (!sb2.toString().equals(sb1.toString())) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Nome : (" + this.nome + ") ");
		sb.append("Nome Interno : (" + this.nomeInterno + ") ");
		sb.append("Tipo Artefato : (" + this.tipoArtefato + ") ");
		sb.append("Ambiente : (" + this.ambiente + ") ");
		sb.append("Sistema : (" + this.sistema + ") ");
		sb.append("Tipo Relacionamento : (" + this.tipoRelacionamento.get() + ") ");

		return sb.toString();
	}

	public Artefato copiar() {
		Artefato novoArtefato = new Artefato();
		novoArtefato.setAmbiente(this.getAmbiente());
		novoArtefato.setArquivo(this.getArquivo());
		novoArtefato.setArtefatosRelacionados(this.getArtefatosRelacionados());
		novoArtefato.setAtributos(this.getAtributos());
		novoArtefato.setCaminhoArquivo(this.getCaminhoArquivo());
		novoArtefato.setCodigoCompleto(this.getCodigoCompleto());
		novoArtefato.setCodigoFonte(this.getCodigoFonte());
		novoArtefato.setCodigoFonteTratado(this.getCodigoFonteTratado());
		novoArtefato.setComentarios(this.getComentarios());
		novoArtefato.setDescricao(this.getDescricao());
		novoArtefato.setIdentificador(this.getIdentificador());
		novoArtefato.setMalhaControlm(this.isMalhaControlm());
		novoArtefato.setNome(this.getNome());
		novoArtefato.setNomeArquivo(this.getNomeArquivo());
		novoArtefato.setNomeInterno(this.getNomeInterno());
		novoArtefato.setObservacao(this.getObservacao());
		novoArtefato.setPosicao(this.getPosicao());
		novoArtefato.setRepresentacaoTextual(this.getRepresentacaoTextual());
		novoArtefato.setRoteiroCodigo(this.getRoteiroCodigo());
		novoArtefato.setRoteiroVariavel(this.getRoteiroVariavel());
		novoArtefato.setSistema(this.getSistema());
		novoArtefato.setTipoArtefato(this.getTipoArtefato());
		novoArtefato.setTipoRelacionamento(this.getTipoRelacionamento());

		return novoArtefato;
	}

	public Atributo buscaAtributo(TipoAtributo tipo) {
		for (Atributo entry : this.atributos) {
			if (tipo.equals(entry.getTipoAtributo())) {
				return entry;
			}
		}

		return null;
	}

	public void adicionarArtefatosRelacionados(Artefato entry) {
		this.artefatosRelacionados.add(entry);
	}

	public void adicionarArtefatosRelacionados(List<Artefato> entries) {
		for (Artefato entry : entries) {
			this.artefatosRelacionados.add(entry);
		}
	}

	public void adicionarAtributo(Atributo entry) {
		this.atributos.add(entry);
	}

	public void adicionarRoteiroCodigo(String entry) {
		this.roteiroCodigo.add(entry);
	}

	public void adicionarRoteiroVariavel(String entry) {
		this.roteiroVariavel.add(entry);
	}

	public void adicionarCodigoCompleto(String entry) {
		this.codigoCompleto.add(entry);
	}

	public void adicionarCodigoFonte(String entry) {
		this.codigoFonte.add(entry);
	}

	public void adicionarCodigoFonteTratado(String entry) {
		this.codigoFonteTratado.add(entry);
	}

	public void adicionarComentario(String entry) {
		this.comentarios.add(entry);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeInterno() {
		return nomeInterno;
	}

	public void setNomeInterno(String nomeInterno) {
		this.nomeInterno = nomeInterno;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public TipoArtefato getTipoArtefato() {
		return tipoArtefato;
	}

	public void setTipoArtefato(TipoArtefato tipoArtefato) {
		this.tipoArtefato = tipoArtefato;
	}

	public TipoAmbiente getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(TipoAmbiente ambiente) {
		this.ambiente = ambiente;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public StringBuilder getRepresentacaoTextual() {
		return representacaoTextual;
	}

	public void setRepresentacaoTextual(StringBuilder representacaoTextual) {
		this.representacaoTextual = representacaoTextual;
	}

	public List<String> getRoteiroCodigo() {
		return roteiroCodigo;
	}

	public void setRoteiroCodigo(List<String> roteiroCodigo) {
		this.roteiroCodigo = roteiroCodigo;
	}

	public List<String> getRoteiroVariavel() {
		return roteiroVariavel;
	}

	public void setRoteiroVariavel(List<String> roteiroVariavel) {
		this.roteiroVariavel = roteiroVariavel;
	}

	public List<String> getCodigoCompleto() {
		return codigoCompleto;
	}

	public void setCodigoCompleto(List<String> codigoCompleto) {
		this.codigoCompleto = codigoCompleto;
	}

	public List<String> getCodigoFonte() {
		return codigoFonte;
	}

	public void setCodigoFonte(List<String> codigoFonte) {
		this.codigoFonte = codigoFonte;
	}

	public List<String> getCodigoFonteTratado() {
		return codigoFonteTratado;
	}

	public void setCodigoFonteTratado(List<String> codigoFonteTratado) {
		this.codigoFonteTratado = codigoFonteTratado;
	}

	public List<String> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<String> comentarios) {
		this.comentarios = comentarios;
	}

	public String getHash() {

		if (TipoArtefato.DSN.equals(this.getTipoArtefato())) {
			this.hash = UtilsHandler.calcularHash(this.getNome(), UtilsHandler.SHA256, "UTF-8");
			if (this.hash.length() < 64) {
				this.hash = "0" + hash;
			}

		} else if (TipoArtefato.DECLARACAO_SQL.equals(this.getTipoArtefato())) {
			this.representacaoTextual.append(this.getNome());
			for (Atributo entry : this.getAtributos()) {
				this.representacaoTextual.append(entry.getValor());
			}

			this.hash = UtilsHandler.calcularHash(this.representacaoTextual.toString(), UtilsHandler.SHA256, "UTF-8");
			if (this.hash.length() < 64) {
				this.hash = "0" + hash;
			}
		}

		else if (TipoArtefato.UTILITARIO.equals(this.getTipoArtefato())) {
			this.hash = null;
		} else if (this.representacaoTextual.toString() != null && !this.representacaoTextual.toString().isEmpty()) {
			this.representacaoTextual.append(this.nome);
			this.representacaoTextual.append(this.descricao);
			this.representacaoTextual.append(this.identificador);
			this.representacaoTextual.append(this.sistema);
			this.representacaoTextual.append(this.observacao);
			this.representacaoTextual.append(this.nomeInterno);
			this.representacaoTextual.append(this.nomeArquivo);

			this.hash = UtilsHandler.calcularHash(this.representacaoTextual.toString(), UtilsHandler.SHA256, "UTF-8");
			if (this.hash.length() < 64) {
				this.hash = "0" + hash;
			}
		} else {
			this.hash = "";
		}

		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public List<Artefato> getArtefatosRelacionados() {
		return artefatosRelacionados;
	}

	public void setArtefatosRelacionados(List<Artefato> artefatosRelacionados) {
		this.artefatosRelacionados = artefatosRelacionados;
	}

	public List<Atributo> getAtributos() {
		return atributos;
	}

	public void setAtributos(List<Atributo> atributos) {
		this.atributos = atributos;
	}

	public boolean isExcluir() {
		return excluir;
	}

	public void setExcluir(boolean excluir) {
		this.excluir = excluir;
	}

	public boolean isMalhaControlm() {
		return isMalhaControlm;
	}

	public void setMalhaControlm(boolean isMalhaControlm) {
		this.isMalhaControlm = isMalhaControlm;
	}

	public TipoRelacionamento getTipoRelacionamento() {
		return tipoRelacionamento;
	}

	public void setTipoRelacionamento(TipoRelacionamento tipoRelacionamento) {
		this.tipoRelacionamento = tipoRelacionamento;
	}

}
