package br.gov.caixa.discovery.ws.resources;

import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.discovery.ejb.dao.ArtefatoDao;
import br.gov.caixa.discovery.ejb.modelos.ArtefatoPersistence;
import br.gov.caixa.discovery.ws.modelos.ArtefatoDomain;
import br.gov.caixa.discovery.ws.utils.Conversores;

@Stateless
public class ArtefatoResource implements ArtefatoResourceI {

	@EJB
	ArtefatoDao artefatoDao;

	@Override
	public Response getArtefato(Long coArtefato) {
		ResponseBuilder response = Response.status(Status.OK);
		ArtefatoPersistence persistence = artefatoDao.pesquisarArtefato(coArtefato);

		ArtefatoDomain domain = Conversores.converter(persistence, true);

		response.entity(domain);

		return response.build();
	}

	@Override
	public Response getRelacionamentos(Long coArtefato) {
		ResponseBuilder response = Response.status(Status.OK);
		ArtefatoPersistence persistence = artefatoDao.pesquisarArtefatoRelacionamento(coArtefato);

		ArtefatoDomain domain = Conversores.converter(persistence, true);

		response.entity(domain);

		return response.build();
	}

	@Override
	public Response atualizarArtefato(Long coArtefato, ArtefatoDomain domain) {
		ResponseBuilder response = Response.status(Status.OK);
		ArtefatoPersistence persistence = artefatoDao.pesquisarArtefato(coArtefato);

		if (persistence == null) {
			// ERRO : ARTEFATO NÃO ENCONTRADO
		}

		persistence.setCoSistema(domain.getCoSistema());
		persistence.setCoAmbiente(domain.getCoAmbiente());
		persistence.setDeDescricaoUsuario(domain.getDeDescricaoUsuario());
		persistence.setNoNomeExibicao(domain.getNoNomeExibicao());
		persistence.setTsUltimaModificacao(Calendar.getInstance());

		artefatoDao.atualizar(persistence);

		domain = Conversores.converter(persistence, false);

		response.entity(domain);

		return response.build();
	}

}
