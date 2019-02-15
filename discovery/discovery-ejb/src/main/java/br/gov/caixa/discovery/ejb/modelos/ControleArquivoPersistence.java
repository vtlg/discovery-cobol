package br.gov.caixa.discovery.ejb.modelos;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "TBL_CONTROLE_ARQUIVO")
@Table()
public class ControleArquivoPersistence {

	@Id
	@Column(name = "NO_NOME_ARQUIVO", length = 300)
	private String noNomeArquivo;

	@Column(name = "DE_HASH", length = 256, columnDefinition = "NOT NULL DEFAULT ' '::character varying")
	private String deHash = " ";

	@Column(name = "DE_RETORNO_IMPORTACAO", columnDefinition = "text DEFAULT ' '::text")
	private String deRetornoImportacao = " ";

	@Column(name = "IC_FORCAR_ATUALIZACAO", columnDefinition = "boolean DEFAULT false")
	private boolean icForcarAtualizacao = false;

	@Column(name = "TS_ULTIMA_LEITURA", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar tsUltimaLeitura;

	public String getNoNomeArquivo() {
		return noNomeArquivo;
	}

	public void setNoNomeArquivo(String noNomeArquivo) {
		this.noNomeArquivo = noNomeArquivo;
	}

	public String getDeHash() {
		return deHash;
	}

	public void setDeHash(String deHash) {
		this.deHash = deHash;
	}

	public String getDeRetornoImportacao() {
		return deRetornoImportacao;
	}

	public void setDeRetornoImportacao(String deRetornoImportacao) {
		this.deRetornoImportacao = deRetornoImportacao;
	}

	public boolean isIcForcarAtualizacao() {
		return icForcarAtualizacao;
	}

	public void setIcForcarAtualizacao(boolean icForcarAtualizacao) {
		this.icForcarAtualizacao = icForcarAtualizacao;
	}

	public Calendar getTsUltimaLeitura() {
		return tsUltimaLeitura;
	}

	public void setTsUltimaLeitura(Calendar tsUltimaLeitura) {
		this.tsUltimaLeitura = tsUltimaLeitura;
	}

}
