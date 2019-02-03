package br.gov.caixa.discovery.ws.interceptors;

import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.logging.Logger;
import org.jboss.resteasy.logging.impl.Log4jLogger;

@Provider
public class ResponseInterceptor implements ContainerResponseFilter {

	private static final Logger LOGGER = Log4jLogger.getLogger(ResponseInterceptor.class);

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {

		LOGGER.trace("Response Status: " + responseContext.getStatus());

		if (responseContext.getStringHeaders() != null) {
			LOGGER.trace("Response headers");
			responseContext.getStringHeaders().forEach((headerProperty, valor) -> {
				LOGGER.trace(headerProperty + " : " + valor);
			});
		}

		if (responseContext.getEntity() != null) {
			LOGGER.trace("Response body");
			Arrays.asList(responseContext.getEntity()).forEach(obj -> {
				LOGGER.trace(obj.getClass().getSimpleName() + " : " + obj.toString());
			});
		}
	}

}
