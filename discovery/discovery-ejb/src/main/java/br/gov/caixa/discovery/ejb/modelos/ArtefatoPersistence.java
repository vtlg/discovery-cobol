package br.gov.caixa.discovery.ejb.modelos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity(name = "TBL_ARTEFATO")
@Table(schema = "public", uniqueConstraints = { @UniqueConstraint(columnNames = { "NO_NOME_ARTEFATO", "CO_AMBIENTE",
		"CO_SISTEMA", "CO_TIPO_ARTEFATO", "TS_FIM_VIGENCIA" }) })
public class ArtefatoPersistence {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CO_ARTEFATO")
	private Long coArtefato;

	@Column(name = "NO_NOME_ARTEFATO", length = 300)
	private String noNomeArtefato;

	@Column(name = "NO_NOME_EXIBICAO", length = 300)
	private String noNomeExibicao;

	@Column(name = "NO_NOME_INTERNO", length = 300)
	private String noNomeInterno;

	@Column(name = "CO_AMBIENTE", length = 5)
	private String coAmbiente;

	@Column(name = "CO_SISTEMA", length = 10)
	private String coSistema;

	@Column(name = "CO_TIPO_ARTEFATO", length = 100)
	private String coTipoArtefato;

	@Column(name = "DE_IDENTIFICADOR", length = 500)
	private String deIdentificador;

	@Column(name = "DE_HASH", length = 256, columnDefinition = "NOT NULL DEFAULT ' '::character varying")
	private String deHash = " ";

	@Column(name = "DE_DESCRICAO_USUARIO", columnDefinition = "text DEFAULT ' '::text")
	private String deDescricaoUsuario = " ";

	@Column(name = "DE_DESCRICAO_ARTEFATO", columnDefinition = "text DEFAULT ' '::text")
	private String deDescricaoArtefato = " ";

	@Column(name = "IC_INCLUSAO_MANUAL", columnDefinition = "boolean DEFAULT false")
	private boolean icInclusaoManual = false;

	@Column(name = "IC_PROCESSO_CRITICO", columnDefinition = "boolean DEFAULT false")
	private boolean icProcessoCritico = false;

	@Column(name = "TS_INICIO_VIGENCIA", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar tsInicioVigencia;

	@Column(name = "TS_ULTIMA_MODIFICACAO", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar tsUltimaModificacao;

	@Column(name = "TS_FIM_VIGENCIA", columnDefinition = "TIMESTAMP DEFAULT null")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar tsFimVigencia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_TIPO_ARTEFATO", referencedColumnName = "CO_TIPO", insertable = false, updatable = false)
	private TipoArtefatoPersistence tipoArtefato;

	@OneToMany(mappedBy = "artefatoPai", fetch = FetchType.LAZY)
	private Set<RelacionamentoPersistence> listaArtefato;

	@OneToMany(mappedBy = "artefato", fetch = FetchType.LAZY)
	private Set<RelacionamentoPersistence> listaArtefatoPai;

	@OneToMany(mappedBy = "artefatoPosterior", fetch = FetchType.LAZY)
	private Set<RelacionamentoPersistence> listaArtefatoAnterior;

	@OneToMany(mappedBy = "artefatoAnterior", fetch = FetchType.LAZY)
	private Set<RelacionamentoPersistence> listaArtefatoPosterior;

	@OneToMany(mappedBy = "artefatoUltimo", fetch = FetchType.LAZY)
	private Set<RelacionamentoPersistence> listaArtefatoPrimeiro;

	@OneToMany(mappedBy = "artefatoPrimeiro", fetch = FetchType.LAZY)
	private Set<RelacionamentoPersistence> listaArtefatoUltimo;

	@OneToMany(mappedBy = "artefato", fetch = FetchType.LAZY)
	private Set<AtributoArtefatoPersistence> listaAtributos;

	@Transient
	private boolean transientAtualizarRelacionamentos = true;

	@Transient
	private List<AtributoArtefatoPersistence> transientListaAtributos = new ArrayList<>();

	@Transient
	private List<RelacionamentoPersistence> transientListaRelacionamentos = new ArrayList<>();

	@Transient
	private List<RelacionamentoPersistence> transientRelacionamentosDesativados = new ArrayList<>();

	public void adicionarRelacionamento(RelacionamentoPersistence entry) {
		if (this.listaArtefato == null) {
			this.listaArtefato = new HashSet<>();
		}

		this.listaArtefato.add(entry);
	}

	public void adicionarRelacionamentoPai(RelacionamentoPersistence entry) {
		if (this.listaArtefatoPai == null) {
			this.listaArtefatoPai = new HashSet<>();
		}

		this.listaArtefatoPai.add(entry);
	}

	public void adicionarRelacionamentoDesativadoTransient(RelacionamentoPersistence entry) {
		if (this.transientRelacionamentosDesativados == null) {
			this.transientRelacionamentosDesativados = new ArrayList<>();
		}

		this.transientRelacionamentosDesativados.add(entry);
	}

	public void adicionarRelacionamentoTransient(List<RelacionamentoPersistence> entry) {
		if (this.transientListaRelacionamentos == null) {
			this.transientListaRelacionamentos = new ArrayList<>();
		}

		this.transientListaRelacionamentos.addAll(entry);
	}
	
	public void adicionarRelacionamentoTransient(RelacionamentoPersistence entry) {
		if (this.transientListaRelacionamentos == null) {
			this.transientListaRelacionamentos = new ArrayList<>();
		}

		this.transientListaRelacionamentos.add(entry);
	}

	public void adicionarAtributoTransient(AtributoArtefatoPersistence entry) {
		if (this.transientListaAtributos == null) {
			this.transientListaAtributos = new ArrayList<>();
		}

		this.transientListaAtributos.add(entry);
	}

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

	public boolean isIcInclusaoManual() {
		return icInclusaoManual;
	}

	public void setIcInclusaoManual(boolean icInclusaoManual) {
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

	public Calendar getTsFimVigencia() {
		return tsFimVigencia;
	}

	public void setTsFimVigencia(Calendar tsFimVigencia) {
		this.tsFimVigencia = tsFimVigencia;
	}

	public TipoArtefatoPersistence getTipoArtefato() {
		return tipoArtefato;
	}

	public void setTipoArtefato(TipoArtefatoPersistence tipoArtefato) {
		this.tipoArtefato = tipoArtefato;
	}

	public Set<RelacionamentoPersistence> getListaArtefato() {
		return listaArtefato;
	}

	public void setListaArtefato(Set<RelacionamentoPersistence> listaArtefato) {
		this.listaArtefato = listaArtefato;
	}

	public Set<RelacionamentoPersistence> getListaArtefatoPai() {
		return listaArtefatoPai;
	}

	public void setListaArtefatoPai(Set<RelacionamentoPersistence> listaArtefatoPai) {
		this.listaArtefatoPai = listaArtefatoPai;
	}

	public Set<RelacionamentoPersistence> getListaArtefatoAnterior() {
		return listaArtefatoAnterior;
	}

	public void setListaArtefatoAnterior(Set<RelacionamentoPersistence> listaArtefatoAnterior) {
		this.listaArtefatoAnterior = listaArtefatoAnterior;
	}

	public Set<RelacionamentoPersistence> getListaArtefatoPosterior() {
		return listaArtefatoPosterior;
	}

	public void setListaArtefatoPosterior(Set<RelacionamentoPersistence> listaArtefatoPosterior) {
		this.listaArtefatoPosterior = listaArtefatoPosterior;
	}

	public Set<RelacionamentoPersistence> getListaArtefatoPrimeiro() {
		return listaArtefatoPrimeiro;
	}

	public void setListaArtefatoPrimeiro(Set<RelacionamentoPersistence> listaArtefatoPrimeiro) {
		this.listaArtefatoPrimeiro = listaArtefatoPrimeiro;
	}

	public Set<RelacionamentoPersistence> getListaArtefatoUltimo() {
		return listaArtefatoUltimo;
	}

	public void setListaArtefatoUltimo(Set<RelacionamentoPersistence> listaArtefatoUltimo) {
		this.listaArtefatoUltimo = listaArtefatoUltimo;
	}

	public List<AtributoArtefatoPersistence> getTransientListaAtributos() {
		return transientListaAtributos;
	}

	public void setTransientListaAtributos(List<AtributoArtefatoPersistence> transientListaAtributos) {
		this.transientListaAtributos = transientListaAtributos;
	}

	public List<RelacionamentoPersistence> getTransientListaRelacionamentos() {
		return transientListaRelacionamentos;
	}

	public void setTransientListaRelacionamentos(List<RelacionamentoPersistence> transientListaRelacionamentos) {
		this.transientListaRelacionamentos = transientListaRelacionamentos;
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

	public Set<AtributoArtefatoPersistence> getListaAtributos() {
		return listaAtributos;
	}

	public void setListaAtributos(Set<AtributoArtefatoPersistence> listaAtributos) {
		this.listaAtributos = listaAtributos;
	}

	public boolean isIcProcessoCritico() {
		return icProcessoCritico;
	}

	public void setIcProcessoCritico(boolean icProcessoCritico) {
		this.icProcessoCritico = icProcessoCritico;
	}

	public boolean isTransientAtualizarRelacionamentos() {
		return transientAtualizarRelacionamentos;
	}

	public void setTransientAtualizarRelacionamentos(boolean transientAtualizarRelacionamentos) {
		this.transientAtualizarRelacionamentos = transientAtualizarRelacionamentos;
	}

	public List<RelacionamentoPersistence> getTransientRelacionamentosDesativados() {
		return transientRelacionamentosDesativados;
	}

	public void setTransientRelacionamentosDesativados(
			List<RelacionamentoPersistence> transientRelacionamentosDesativados) {
		this.transientRelacionamentosDesativados = transientRelacionamentosDesativados;
	}

	public ArtefatoPersistence() {
		super();
	}

	public ArtefatoPersistence(Long coArtefato, String noNomeArtefato, String noNomeExibicao, String noNomeInterno,
			String coAmbiente, String coSistema, String coTipoArtefato, String deIdentificador, String deHash,
			String deDescricaoUsuario, String deDescricaoArtefato, boolean icInclusaoManual, Calendar tsInicioVigencia,
			Calendar tsUltimaModificacao, Calendar tsFimVigencia) {
		super();
		this.coArtefato = coArtefato;
		this.noNomeArtefato = noNomeArtefato;
		this.noNomeExibicao = noNomeExibicao;
		this.noNomeInterno = noNomeInterno;
		this.coAmbiente = coAmbiente;
		this.coSistema = coSistema;
		this.coTipoArtefato = coTipoArtefato;
		this.deIdentificador = deIdentificador;
		this.deHash = deHash;
		this.deDescricaoUsuario = deDescricaoUsuario;
		this.deDescricaoArtefato = deDescricaoArtefato;
		this.icInclusaoManual = icInclusaoManual;
		this.tsInicioVigencia = tsInicioVigencia;
		this.tsUltimaModificacao = tsUltimaModificacao;
		this.tsFimVigencia = tsFimVigencia;
	}

	public ArtefatoPersistence(Long coArtefato, String noNomeArtefato, String noNomeExibicao, String noNomeInterno,
			String coAmbiente, String coSistema, String coTipoArtefato, String deIdentificador, String deHash,
			String deDescricaoUsuario, String deDescricaoArtefato, boolean icInclusaoManual, Calendar tsInicioVigencia,
			Calendar tsUltimaModificacao, Calendar tsFimVigencia, boolean icProcessoCritico) {
		super();
		this.coArtefato = coArtefato;
		this.noNomeArtefato = noNomeArtefato;
		this.noNomeExibicao = noNomeExibicao;
		this.noNomeInterno = noNomeInterno;
		this.coAmbiente = coAmbiente;
		this.coSistema = coSistema;
		this.coTipoArtefato = coTipoArtefato;
		this.deIdentificador = deIdentificador;
		this.deHash = deHash;
		this.deDescricaoUsuario = deDescricaoUsuario;
		this.deDescricaoArtefato = deDescricaoArtefato;
		this.icInclusaoManual = icInclusaoManual;
		this.tsInicioVigencia = tsInicioVigencia;
		this.tsUltimaModificacao = tsUltimaModificacao;
		this.tsFimVigencia = tsFimVigencia;
		this.icProcessoCritico = icProcessoCritico;
	}

	public ArtefatoPersistence(Long coArtefato, String noNomeArtefato, String noNomeExibicao, String noNomeInterno,
			String coAmbiente, String coSistema, String coTipoArtefato, String deIdentificador, String deHash,
			String deDescricaoUsuario, String deDescricaoArtefato, boolean icInclusaoManual, Calendar tsInicioVigencia,
			Calendar tsUltimaModificacao, Calendar tsFimVigencia, TipoArtefatoPersistence tipoArtefato,
			Set<RelacionamentoPersistence> listaArtefato, Set<RelacionamentoPersistence> listaArtefatoPai,
			Set<RelacionamentoPersistence> listaArtefatoAnterior, Set<RelacionamentoPersistence> listaArtefatoPosterior,
			Set<RelacionamentoPersistence> listaArtefatoPrimeiro, Set<RelacionamentoPersistence> listaArtefatoUltimo,
			boolean transientAtualizarRelacionamentos, List<AtributoArtefatoPersistence> transientListaAtributos,
			List<RelacionamentoPersistence> transientListaRelacionamentos) {
		super();
		this.coArtefato = coArtefato;
		this.noNomeArtefato = noNomeArtefato;
		this.noNomeExibicao = noNomeExibicao;
		this.noNomeInterno = noNomeInterno;
		this.coAmbiente = coAmbiente;
		this.coSistema = coSistema;
		this.coTipoArtefato = coTipoArtefato;
		this.deIdentificador = deIdentificador;
		this.deHash = deHash;
		this.deDescricaoUsuario = deDescricaoUsuario;
		this.deDescricaoArtefato = deDescricaoArtefato;
		this.icInclusaoManual = icInclusaoManual;
		this.tsInicioVigencia = tsInicioVigencia;
		this.tsUltimaModificacao = tsUltimaModificacao;
		this.tsFimVigencia = tsFimVigencia;
		this.tipoArtefato = tipoArtefato;
		this.listaArtefato = listaArtefato;
		this.listaArtefatoPai = listaArtefatoPai;
		this.listaArtefatoAnterior = listaArtefatoAnterior;
		this.listaArtefatoPosterior = listaArtefatoPosterior;
		this.listaArtefatoPrimeiro = listaArtefatoPrimeiro;
		this.listaArtefatoUltimo = listaArtefatoUltimo;
		this.transientAtualizarRelacionamentos = transientAtualizarRelacionamentos;
		this.transientListaAtributos = transientListaAtributos;
		this.transientListaRelacionamentos = transientListaRelacionamentos;
	}

	public ArtefatoPersistence(String noNomeArtefato) {
		super();
		this.noNomeArtefato = noNomeArtefato;
	}

	public ArtefatoPersistence(String noNomeArtefato, String noNomeExibicao) {
		super();
		this.noNomeArtefato = noNomeArtefato;
		this.noNomeExibicao = noNomeExibicao;
	}

	public ArtefatoPersistence(Long coArtefato, String noNomeArtefato, String noNomeExibicao, String noNomeInterno,
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

	public ArtefatoPersistence(Long coArtefato, String noNomeArtefato, String noNomeExibicao, String noNomeInterno,
			String coTipoArtefato, String coAmbiente, String coSistema, boolean icProcessoCritico) {
		super();
		this.coArtefato = coArtefato;
		this.noNomeArtefato = noNomeArtefato;
		this.noNomeExibicao = noNomeExibicao;
		this.noNomeInterno = noNomeInterno;
		this.coTipoArtefato = coTipoArtefato;
		this.coAmbiente = coAmbiente;
		this.coSistema = coSistema;
		this.icProcessoCritico = icProcessoCritico;
	}

	public ArtefatoPersistence(Object[] copyOfRange) {

		this.coArtefato = (Long) copyOfRange[0];
		this.noNomeArtefato = (String) copyOfRange[1];
		this.noNomeExibicao = (String) copyOfRange[2];
		this.noNomeInterno = (String) copyOfRange[3];
		this.coTipoArtefato = (String) copyOfRange[4];
		this.icInclusaoManual = (Boolean) copyOfRange[5];
		this.icProcessoCritico = (Boolean) copyOfRange[6];

//		artefatoRoot.get("coArtefato"), 
//		artefatoRoot.get("noNomeArtefato"), 
//		artefatoRoot.get("noNomeExibicao"),
//		artefatoRoot.get("noNomeInterno"),
//		artefatoRoot.get("coTipoArtefato"),
//		artefatoRoot.get("icInclusaoManual"),
//		artefatoRoot.get("icProcessoCritico"),

	}

	// ****

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Código : (" + this.coArtefato + ") ");
		sb.append("Nome Artefato : (" + this.noNomeArtefato + ") ");
		sb.append("Nome Interno : (" + this.noNomeInterno + ") ");
		sb.append("Nome Exibição  : (" + this.noNomeExibicao + ") ");
		sb.append("Tipo de Artefato : (" + this.coTipoArtefato + ") ");
		sb.append("Ambiente : (" + this.coAmbiente + ") ");
		sb.append("Sistema : (" + this.coSistema + ") ");

		return sb.toString();
	}

}
