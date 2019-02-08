package br.gov.caixa.discovery.ws.resources;

import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.discovery.ejb.dao.ArtefatoDao;
import br.gov.caixa.discovery.ejb.dao.RelacionamentoDao;
import br.gov.caixa.discovery.ejb.dao.ViewDao;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ws.modelos.ArtefatoDomain;
import br.gov.caixa.discovery.ws.utils.Conversores;

@Stateless
public class ArtefatoResource implements ArtefatoResourceI {

	@EJB
	ArtefatoDao artefatoDao;

	@EJB
	RelacionamentoDao relacionamentoDao;

	@EJB
	ViewDao viewDao;

	@Override
	public Response getArtefato(Long coArtefato) throws EJBException {
		ResponseBuilder response = Response.status(Status.OK);
		ArtefatoPersistence persistence = artefatoDao.getArtefato(coArtefato);

		ArtefatoDomain domain = Conversores.converter(persistence, true, false);

		if (domain == null) {
			response.status(Status.NOT_FOUND);
		} else {
			response.entity(domain);
		}

		return response.build();
	}

	@Override
	public Response getRelacionamentos(Long coArtefato) throws EJBException {
		ResponseBuilder response = Response.status(Status.OK);
		ArtefatoPersistence persistence = viewDao.getArtefatoRelacionamento(coArtefato);

		ArtefatoDomain domain = Conversores.converter(persistence, true, true);

		response.entity(domain);

		return response.build();
	}

	@Override
	public Response atualizarArtefato(Long coArtefato, ArtefatoDomain domain) {
		ResponseBuilder response = Response.status(Status.OK);
		ArtefatoPersistence persistence = artefatoDao.getArtefato(coArtefato);

		if (persistence == null) {
			// ERRO : ARTEFATO NÃO ENCONTRADO
		}

		persistence.setCoSistema(domain.getCoSistema());
		persistence.setCoAmbiente(domain.getCoAmbiente());
		persistence.setDeDescricaoUsuario(domain.getDeDescricaoUsuario());
		persistence.setNoNomeExibicao(domain.getNoNomeExibicao());
		persistence.setIcProcessoCritico(domain.isIcProcessoCritico());
		persistence.setTsUltimaModificacao(Calendar.getInstance());

		artefatoDao.atualizar(persistence);
		relacionamentoDao.atualizarRelacionamentoOnAlterarCoSistema(persistence);

		domain = Conversores.converter(persistence, false, false);

		response.entity(domain);

		return response.build();
	}

	@Override
	public Response incluirArtefato(ArtefatoDomain artefato) {
		ResponseBuilder response = Response.status(Status.OK);

		ArtefatoPersistence persistence = new ArtefatoPersistence();
		Calendar TS_ATUAL = Calendar.getInstance();

		persistence.setNoNomeArtefato(artefato.getNoNomeArtefato());
		persistence.setNoNomeInterno(artefato.getNoNomeInterno());
		persistence.setNoNomeExibicao(artefato.getNoNomeExibicao());
		persistence.setCoAmbiente("DESCONHECIDO");
		persistence.setCoSistema(artefato.getCoSistema());
		persistence.setCoTipoArtefato(artefato.getTipoArtefato().getCoTipo());
		persistence.setDeDescricaoUsuario(artefato.getDeDescricaoUsuario());
		persistence.setIcInclusaoManual(true);
		persistence.setIcProcessoCritico(artefato.isIcProcessoCritico());
		persistence.setTsInicioVigencia(TS_ATUAL);
		persistence.setTsUltimaModificacao(TS_ATUAL);

		artefatoDao.incluir(persistence);

		if (persistence.getCoArtefato() != null) {
			artefato = Conversores.converter(persistence, false, false);
			response.entity(artefato);
		} else {
			response.status(Status.INTERNAL_SERVER_ERROR);
		}

		return response.build();
	}

}
