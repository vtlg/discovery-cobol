package br.gov.caixa.discovery.ejb.view;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "VW_ARTEFATO")
public class ArtefatoView {

	@Id
	@Column(name = "CO_ARTEFATO")
	private Long coArtefato;

	@Column(name = "NO_NOME_ARTEFATO")
	private String noNomeArtefato;

	@Column(name = "NO_NOME_EXIBICAO")
	private String noNomeExibicao;

	@Column(name = "NO_NOME_INTERNO")
	private String noNomeInterno;

	@Column(name = "CO_AMBIENTE")
	private String coAmbiente;

	@Column(name = "CO_SISTEMA")
	private String coSistema;

	@Column(name = "CO_TIPO_ARTEFATO")
	private String coTipoArtefato;

	@Column(name = "DE_IDENTIFICADOR")
	private String deIdentificador;

	@Column(name = "DE_HASH")
	private String deHash;

	@Column(name = "DE_DESCRICAO_USUARIO")
	private String deDescricaoUsuario;

	@Column(name = "DE_DESCRICAO_ARTEFATO")
	private String deDescricaoArtefato;

	@Column(name = "IC_PROCESSO_CRITICO")
	private Boolean icProcessoCritico;

	@Column(name = "IC_INCLUSAO_MANUAL")
	private Boolean icInclusaoManual;

	@Column(name = "TS_INICIO_VIGENCIA")
	private Calendar tsInicioVigencia;

	@Column(name = "TS_ULTIMA_MODIFICACAO")
	private Calendar tsUltimaModificacao;

	@Column(name = "COUNT_RELACIONAMENTO")
	private Long countRelacionamento;

	@Column(name = "COUNT_RELACIONAMENTO_INTERFACE")
	private Long countRelacionamentoInterface;

	@Column(name = "COUNT_RELACIONAMENTO_NORMAL")
	private Long countRelacionamentoNormal;

	@Column(name = "COUNT_RELACIONAMENTO_CONTROL_M")
	private Long countRelacionamentoControlM;

//	@Column(name = "CO_TIPO_RELACIONAMENTO")
//	private String coTipoRelacionamento;
//
//	@Transient
//	private Integer transientCountRelacionamentoInterface = 0;
//
//	@Transient
//	private Integer transientCountRelacionamentoControlM = 0;
//
//	@Transient
//	private Integer transientCountRelacionamentoNormal = 0;
//
//	@Transient
//	private Integer transientCountRelacionamento = 0;
//
//	@Column(name = "REL_IC_INCLUSAO_MANUAL")
//	private Boolean icInclusaoManualRelacionamento;
//
//	@Column(name = "REL_INCLUSAO_MALHA")
//	private Boolean icInclusaoMalhaRelacionamento;
//
//	public void adicionarTransientCountRelacionamentoInterface(Integer valor) {
//		this.transientCountRelacionamentoInterface += valor;
//	}
//
//	public void adicionarTransientCountRelacionamentoControlM(Integer valor) {
//		this.transientCountRelacionamentoControlM += valor;
//	}
//
//	public void adicionarTransientCountRelacionamentoNormal(Integer valor) {
//		this.transientCountRelacionamentoNormal += valor;
//	}
//
//	public void adicionarTransientCountRelacionamento(Integer valor) {
//		this.transientCountRelacionamento += valor;
//	}

	public Long getCoArtefato() {
		return coArtefato;
	}

	public void setCoArtefato(Long coArtefato) {
		this.coArtefato = coArtefato;
	}

	public String getNoNomeArtefato() {
		return noNomeArtefato;
	}

	public void setNoNomeArtefato(String noNomeArtefato) {
		this.noNomeArtefato = noNomeArtefato;
	}

	public String getNoNomeExibicao() {
		return noNomeExibicao;
	}

	public void setNoNomeExibicao(String noNomeExibicao) {
		this.noNomeExibicao = noNomeExibicao;
	}

	public String getNoNomeInterno() {
		return noNomeInterno;
	}

	public void setNoNomeInterno(String noNomeInterno) {
		this.noNomeInterno = noNomeInterno;
	}

	public String getCoAmbiente() {
		return coAmbiente;
	}

	public void setCoAmbiente(String coAmbiente) {
		this.coAmbiente = coAmbiente;
	}

	public String getCoSistema() {
		return coSistema;
	}

	public void setCoSistema(String coSistema) {
		this.coSistema = coSistema;
	}

	public String getCoTipoArtefato() {
		return coTipoArtefato;
	}

	public void setCoTipoArtefato(String coTipoArtefato) {
		this.coTipoArtefato = coTipoArtefato;
	}

	public String getDeIdentificador() {
		return deIdentificador;
	}

	public void setDeIdentificador(String deIdentificador) {
		this.deIdentificador = deIdentificador;
	}

	public String getDeHash() {
		return deHash;
	}

	public void setDeHash(String deHash) {
		this.deHash = deHash;
	}

	public String getDeDescricaoUsuario() {
		return deDescricaoUsuario;
	}

	public void setDeDescricaoUsuario(String deDescricaoUsuario) {
		this.deDescricaoUsuario = deDescricaoUsuario;
	}

	public String getDeDescricaoArtefato() {
		return deDescricaoArtefato;
	}

	public void setDeDescricaoArtefato(String deDescricaoArtefato) {
		this.deDescricaoArtefato = deDescricaoArtefato;
	}

	public Boolean getIcProcessoCritico() {
		return icProcessoCritico;
	}

	public void setIcProcessoCritico(Boolean icProcessoCritico) {
		this.icProcessoCritico = icProcessoCritico;
	}

	public Boolean getIcInclusaoManual() {
		return icInclusaoManual;
	}

	public void setIcInclusaoManual(Boolean icInclusaoManual) {
		this.icInclusaoManual = icInclusaoManual;
	}

	public Calendar getTsInicioVigencia() {
		return tsInicioVigencia;
	}

	public void setTsInicioVigencia(Calendar tsInicioVigencia) {
		this.tsInicioVigencia = tsInicioVigencia;
	}

	public Calendar getTsUltimaModificacao() {
		return tsUltimaModificacao;
	}

	public void setTsUltimaModificacao(Calendar tsUltimaModificacao) {
		this.tsUltimaModificacao = tsUltimaModificacao;
	}

	public Long getCountRelacionamento() {
		return countRelacionamento;
	}

	public void setCountRelacionamento(Long countRelacionamento) {
		this.countRelacionamento = countRelacionamento;
	}

	public Long getCountRelacionamentoInterface() {
		return countRelacionamentoInterface;
	}

	public void setCountRelacionamentoInterface(Long countRelacionamentoInterface) {
		this.countRelacionamentoInterface = countRelacionamentoInterface;
	}

	public Long getCountRelacionamentoNormal() {
		return countRelacionamentoNormal;
	}

	public void setCountRelacionamentoNormal(Long countRelacionamentoNormal) {
		this.countRelacionamentoNormal = countRelacionamentoNormal;
	}

	public Long getCountRelacionamentoControlM() {
		return countRelacionamentoControlM;
	}

	public void setCountRelacionamentoControlM(Long countRelacionamentoControlM) {
		this.countRelacionamentoControlM = countRelacionamentoControlM;
	}

	public ArtefatoView() {
		super();
	}

	public ArtefatoView(Long coArtefato, String noNomeArtefato, String noNomeExibicao, String noNomeInterno,
			String coTipoArtefato, String coAmbiente, String coSistema) {
		super();
		this.coArtefato = coArtefato;
		this.noNomeArtefato = noNomeArtefato;
		this.noNomeExibicao = noNomeExibicao;
		this.noNomeInterno = noNomeInterno;
		this.coTipoArtefato = coTipoArtefato;
		this.coAmbiente = coAmbiente;
		this.coSistema = coSistema;
	}

}
