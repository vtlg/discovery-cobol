package br.gov.caixa.discovery.ejb.view;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Transient;

import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ejb.modelos.AtributoRelacionamentoPersistence;
import br.gov.caixa.discovery.ejb.modelos.RelacionamentoPersistence;

@Entity(name = "VW_RELACIONAMENTO")
@IdClass(value= RelacionamentoId.class )
public class RelacionamentoView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3899073995401404585L;

	@Id
	@Column(name = "REL_CO_RELACIONAMENTO")
	private Long coRelacionamentoRel;

	@Column(name = "REL_CO_TIPO_RELACIONAMENTO")
	private String coTipoRelacionamentoRel;

	@Column(name = "REL_IC_INCLUSAO_MANUAL")
	private Boolean icInclusaoManualRel;

	@Column(name = "REL_IC_INCLUSAO_MALHA")
	private Boolean icInclusaoMalhaRel;

	@Id
	@Column(name = "atr_nu_sequencial")
	private Long coNuSequencialAtr;

	@Column(name = "atr_co_tipo_atributo")
	private String coTipoAtributoAtr;

	@Column(name = "atr_co_tabela")
	private String coTabelaAtr;

	@Column(name = "atr_co_externo")
	private Long coExternoAtr;

	@Column(name = "atr_de_valor")
	private String deValorAtr;

	@Column(name = "atr_ic_editavel")
	private Boolean icEditavelAtr;

	@Column(name = "atr_ic_opcional")
	private Boolean icOpcionalAtr;

	@Id
	@Column(name = "ASC_CO_ARTEFATO")
	private Long coArtefatoAsc;

	@Column(name = "ASC_NO_NOME_ARTEFATO")
	private String noNomeArtefatoAsc;

	@Column(name = "ASC_NO_NOME_EXIBICAO")
	private String noNomeExibicaoAsc;

	@Column(name = "ASC_NO_NOME_INTERNO")
	private String noNomeInternoAsc;

	@Column(name = "ASC_CO_AMBIENTE")
	private String coAmbienteAsc;

	@Column(name = "ASC_CO_SISTEMA")
	private String coSistemaAsc;

	@Column(name = "ASC_CO_TIPO_ARTEFATO")
	private String coTipoArtefatoAsc;

	@Column(name = "ASC_DE_IDENTIFICADOR")
	private String deIdentificadorAsc;

	@Column(name = "ASC_IC_INCLUSAO_MANUAL")
	private Boolean icInclusaoManualAsc;

	@Column(name = "ASC_IC_PROCESSO_CRITICO")
	private Boolean icProcessoCriticoAsc;

	@Id
	@Column(name = "DESC_CO_ARTEFATO")
	private Long coArtefatoDesc;

	@Column(name = "DESC_NO_NOME_ARTEFATO")
	private String noNomeArtefatoDesc;

	@Column(name = "DESC_NO_NOME_EXIBICAO")
	private String noNomeExibicaoDesc;

	@Column(name = "DESC_NO_NOME_INTERNO")
	private String noNomeInternoDesc;

	@Column(name = "DESC_CO_AMBIENTE")
	private String coAmbienteDesc;

	@Column(name = "DESC_CO_SISTEMA")
	private String coSistemaDesc;

	@Column(name = "DESC_CO_TIPO_ARTEFATO")
	private String coTipoArtefatoDesc;

	@Column(name = "DESC_DE_IDENTIFICADOR")
	private String deIdentificadorDesc;

	@Column(name = "DESC_IC_INCLUSAO_MANUAL")
	private Boolean icInclusaoManualDesc;

	@Column(name = "DESC_IC_PROCESSO_CRITICO")
	private Boolean icProcessoCriticoDesc;

	@Transient
	private RelacionamentoPersistence transientRelacionamento;

	@Transient
	private AtributoRelacionamentoPersistence transientAtributo;

	@Transient
	private Set<AtributoRelacionamentoPersistence> transientListaAtributos;

	@Transient
	private ArtefatoPersistence transientArtefatoAsc;

	@Transient
	private ArtefatoPersistence transientArtefatoDesc;

	public void adicionarAtributo(AtributoRelacionamentoPersistence atributo) {
		if (transientListaAtributos == null) {
			this.transientListaAtributos = new HashSet<>();
		}

		this.transientListaAtributos.add(atributo);
	}

	public RelacionamentoPersistence getTransientRelacionamento() {
		if (this.transientRelacionamento != null || this.coRelacionamentoRel == null) {
			return this.transientRelacionamento;
		}

		this.transientRelacionamento = new RelacionamentoPersistence();

		this.transientRelacionamento.setCoRelacionamento(coRelacionamentoRel);
		this.transientRelacionamento.setCoTipoRelacionamento(coTipoRelacionamentoRel);
		this.transientRelacionamento.setIcInclusaoMalha(icInclusaoMalhaRel);
		this.transientRelacionamento.setIcInclusaoManual(icInclusaoManualRel);

		this.transientRelacionamento.setArtefato(this.getTransientArtefatoDesc());
		this.transientRelacionamento.setArtefatoPai(this.getTransientArtefatoAsc());

		return this.transientRelacionamento;
	}

	public void setTransientRelacionamento(RelacionamentoPersistence transientRelacionamento) {

	}

	public AtributoRelacionamentoPersistence getTransientAtributo() {
		if (this.coNuSequencialAtr == null) {
			return null;
		}

		transientAtributo = new AtributoRelacionamentoPersistence();
		transientAtributo.setCoExterno(this.coExternoAtr);
		transientAtributo.setCoTipoAtributo(this.coTipoAtributoAtr);
		transientAtributo.setDeValor(this.deValorAtr);
		transientAtributo.setIcEditavel(this.icEditavelAtr);
		transientAtributo.setIcOpcional(this.icOpcionalAtr);
		transientAtributo.setNuSequencial(this.coNuSequencialAtr);

		return transientAtributo;
	}

	public void setTransientAtributo(AtributoRelacionamentoPersistence transientAtributo) {
		this.transientAtributo = transientAtributo;
	}

	public ArtefatoPersistence getTransientArtefatoAsc() {

		if (this.transientArtefatoAsc != null || this.coArtefatoAsc == null) {
			return this.transientArtefatoAsc;
		}

		this.transientArtefatoAsc = new ArtefatoPersistence();

		this.transientArtefatoAsc.setCoAmbiente(coAmbienteAsc);
		this.transientArtefatoAsc.setCoArtefato(coArtefatoAsc);
		this.transientArtefatoAsc.setCoSistema(coSistemaAsc);
		this.transientArtefatoAsc.setCoTipoArtefato(coTipoArtefatoAsc);
		this.transientArtefatoAsc.setDeIdentificador(deIdentificadorAsc);
		this.transientArtefatoAsc.setIcInclusaoManual(icInclusaoManualAsc);
		this.transientArtefatoAsc.setIcProcessoCritico(icProcessoCriticoAsc);
		this.transientArtefatoAsc.setNoNomeArtefato(noNomeArtefatoAsc);
		this.transientArtefatoAsc.setNoNomeExibicao(noNomeExibicaoAsc);
		this.transientArtefatoAsc.setNoNomeInterno(noNomeInternoAsc);

		return transientArtefatoAsc;
	}

	public void setTransientArtefatoAsc(ArtefatoPersistence transientArtefatoAsc) {
		this.transientArtefatoAsc = transientArtefatoAsc;
	}

	public ArtefatoPersistence getTransientArtefatoDesc() {
		if (this.transientArtefatoDesc != null || this.coArtefatoDesc == null) {
			return this.transientArtefatoDesc;
		}

		this.transientArtefatoDesc = new ArtefatoPersistence();

		this.transientArtefatoDesc.setCoAmbiente(coAmbienteDesc);
		this.transientArtefatoDesc.setCoArtefato(coArtefatoDesc);
		this.transientArtefatoDesc.setCoSistema(coSistemaDesc);
		this.transientArtefatoDesc.setCoTipoArtefato(coTipoArtefatoDesc);
		this.transientArtefatoDesc.setDeIdentificador(deIdentificadorDesc);
		this.transientArtefatoDesc.setIcInclusaoManual(icInclusaoManualDesc);
		this.transientArtefatoDesc.setIcProcessoCritico(icProcessoCriticoDesc);
		this.transientArtefatoDesc.setNoNomeArtefato(noNomeArtefatoDesc);
		this.transientArtefatoDesc.setNoNomeExibicao(noNomeExibicaoDesc);
		this.transientArtefatoDesc.setNoNomeInterno(noNomeInternoDesc);

		return transientArtefatoDesc;
	}

	public void setTransientArtefatoDesc(ArtefatoPersistence transientArtefatoDesc) {
		this.transientArtefatoDesc = transientArtefatoDesc;
	}

	public Long getCoRelacionamentoRel() {
		return coRelacionamentoRel;
	}

	public void setCoRelacionamentoRel(Long coRelacionamentoRel) {
		this.coRelacionamentoRel = coRelacionamentoRel;
	}

	public String getCoTipoRelacionamentoRel() {
		return coTipoRelacionamentoRel;
	}

	public void setCoTipoRelacionamentoRel(String coTipoRelacionamentoRel) {
		this.coTipoRelacionamentoRel = coTipoRelacionamentoRel;
	}

	public Boolean getIcInclusaoManualRel() {
		return icInclusaoManualRel;
	}

	public void setIcInclusaoManualRel(Boolean icInclusaoManualRel) {
		this.icInclusaoManualRel = icInclusaoManualRel;
	}

	public Boolean getIcInclusaoMalhaRel() {
		return icInclusaoMalhaRel;
	}

	public void setIcInclusaoMalhaRel(Boolean icInclusaoMalhaRel) {
		this.icInclusaoMalhaRel = icInclusaoMalhaRel;
	}

	public Long getCoNuSequencialAtr() {
		return coNuSequencialAtr;
	}

	public void setCoNuSequencialAtr(Long coNuSequencialAtr) {
		this.coNuSequencialAtr = coNuSequencialAtr;
	}

	public String getCoTipoAtributoAtr() {
		return coTipoAtributoAtr;
	}

	public void setCoTipoAtributoAtr(String coTipoAtributoAtr) {
		this.coTipoAtributoAtr = coTipoAtributoAtr;
	}

	public String getCoTabelaAtr() {
		return coTabelaAtr;
	}

	public void setCoTabelaAtr(String coTabelaAtr) {
		this.coTabelaAtr = coTabelaAtr;
	}

	public Long getCoExternoAtr() {
		return coExternoAtr;
	}

	public void setCoExternoAtr(Long coExternoAtr) {
		this.coExternoAtr = coExternoAtr;
	}

	public String getDeValorAtr() {
		return deValorAtr;
	}

	public void setDeValorAtr(String deValorAtr) {
		this.deValorAtr = deValorAtr;
	}

	public Boolean getIcEditavelAtr() {
		return icEditavelAtr;
	}

	public void setIcEditavelAtr(Boolean icEditavelAtr) {
		this.icEditavelAtr = icEditavelAtr;
	}

	public Boolean getIcOpcionalAtr() {
		return icOpcionalAtr;
	}

	public void setIcOpcionalAtr(Boolean icOpcionalAtr) {
		this.icOpcionalAtr = icOpcionalAtr;
	}

	public Long getCoArtefatoAsc() {
		return coArtefatoAsc;
	}

	public void setCoArtefatoAsc(Long coArtefatoAsc) {
		this.coArtefatoAsc = coArtefatoAsc;
	}

	public String getNoNomeArtefatoAsc() {
		return noNomeArtefatoAsc;
	}

	public void setNoNomeArtefatoAsc(String noNomeArtefatoAsc) {
		this.noNomeArtefatoAsc = noNomeArtefatoAsc;
	}

	public String getNoNomeExibicaoAsc() {
		return noNomeExibicaoAsc;
	}

	public void setNoNomeExibicaoAsc(String noNomeExibicaoAsc) {
		this.noNomeExibicaoAsc = noNomeExibicaoAsc;
	}

	public String getNoNomeInternoAsc() {
		return noNomeInternoAsc;
	}

	public void setNoNomeInternoAsc(String noNomeInternoAsc) {
		this.noNomeInternoAsc = noNomeInternoAsc;
	}

	public String getCoAmbienteAsc() {
		return coAmbienteAsc;
	}

	public void setCoAmbienteAsc(String coAmbienteAsc) {
		this.coAmbienteAsc = coAmbienteAsc;
	}

	public String getCoSistemaAsc() {
		return coSistemaAsc;
	}

	public void setCoSistemaAsc(String coSistemaAsc) {
		this.coSistemaAsc = coSistemaAsc;
	}

	public String getCoTipoArtefatoAsc() {
		return coTipoArtefatoAsc;
	}

	public void setCoTipoArtefatoAsc(String coTipoArtefatoAsc) {
		this.coTipoArtefatoAsc = coTipoArtefatoAsc;
	}

	public String getDeIdentificadorAsc() {
		return deIdentificadorAsc;
	}

	public void setDeIdentificadorAsc(String deIdentificadorAsc) {
		this.deIdentificadorAsc = deIdentificadorAsc;
	}

	public Boolean getIcInclusaoManualAsc() {
		return icInclusaoManualAsc;
	}

	public void setIcInclusaoManualAsc(Boolean icInclusaoManualAsc) {
		this.icInclusaoManualAsc = icInclusaoManualAsc;
	}

	public Boolean getIcProcessoCriticoAsc() {
		return icProcessoCriticoAsc;
	}

	public void setIcProcessoCriticoAsc(Boolean icProcessoCriticoAsc) {
		this.icProcessoCriticoAsc = icProcessoCriticoAsc;
	}

	public Long getCoArtefatoDesc() {
		return coArtefatoDesc;
	}

	public void setCoArtefatoDesc(Long coArtefatoDesc) {
		this.coArtefatoDesc = coArtefatoDesc;
	}

	public String getNoNomeArtefatoDesc() {
		return noNomeArtefatoDesc;
	}

	public void setNoNomeArtefatoDesc(String noNomeArtefatoDesc) {
		this.noNomeArtefatoDesc = noNomeArtefatoDesc;
	}

	public String getNoNomeExibicaoDesc() {
		return noNomeExibicaoDesc;
	}

	public void setNoNomeExibicaoDesc(String noNomeExibicaoDesc) {
		this.noNomeExibicaoDesc = noNomeExibicaoDesc;
	}

	public String getNoNomeInternoDesc() {
		return noNomeInternoDesc;
	}

	public void setNoNomeInternoDesc(String noNomeInternoDesc) {
		this.noNomeInternoDesc = noNomeInternoDesc;
	}

	public String getCoAmbienteDesc() {
		return coAmbienteDesc;
	}

	public void setCoAmbienteDesc(String coAmbienteDesc) {
		this.coAmbienteDesc = coAmbienteDesc;
	}

	public String getCoSistemaDesc() {
		return coSistemaDesc;
	}

	public void setCoSistemaDesc(String coSistemaDesc) {
		this.coSistemaDesc = coSistemaDesc;
	}

	public String getCoTipoArtefatoDesc() {
		return coTipoArtefatoDesc;
	}

	public void setCoTipoArtefatoDesc(String coTipoArtefatoDesc) {
		this.coTipoArtefatoDesc = coTipoArtefatoDesc;
	}

	public String getDeIdentificadorDesc() {
		return deIdentificadorDesc;
	}

	public void setDeIdentificadorDesc(String deIdentificadorDesc) {
		this.deIdentificadorDesc = deIdentificadorDesc;
	}

	public Boolean getIcInclusaoManualDesc() {
		return icInclusaoManualDesc;
	}

	public void setIcInclusaoManualDesc(Boolean icInclusaoManualDesc) {
		this.icInclusaoManualDesc = icInclusaoManualDesc;
	}

	public Boolean getIcProcessoCriticoDesc() {
		return icProcessoCriticoDesc;
	}

	public void setIcProcessoCriticoDesc(Boolean icProcessoCriticoDesc) {
		this.icProcessoCriticoDesc = icProcessoCriticoDesc;
	}

	public Set<AtributoRelacionamentoPersistence> getTransientListaAtributos() {
		return transientListaAtributos;
	}

	public void setTransientListaAtributos(Set<AtributoRelacionamentoPersistence> transientListaAtributos) {
		this.transientListaAtributos = transientListaAtributos;
	}

	public RelacionamentoView() {
		super();
	}

}
