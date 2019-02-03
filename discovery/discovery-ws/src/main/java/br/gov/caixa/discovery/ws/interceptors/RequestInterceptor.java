package br.gov.caixa.discovery.ws.interceptors;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.logging.Logger;
import org.jboss.resteasy.logging.impl.Log4jLogger;

@Provider
public class RequestInterceptor implements ContainerRequestFilter {

	private static final Logger LOGGER = Log4jLogger.getLogger(RequestInterceptor.class);

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		LOGGER.trace("Request Path : " + requestContext.getUriInfo().getAbsolutePath());
		LOGGER.trace("Request Http methods : " + requestContext.getMethod());

		if (requestContext.getHeaders() != null) {
			LOGGER.trace("Request headers");
			requestContext.getHeaders().forEach((headerProperty, valor) -> {
				LOGGER.trace(headerProperty + " : " + valor);
			});
		}

	}

}
