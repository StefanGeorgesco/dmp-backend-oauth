package fr.cnam.stefangeorgesco.dmp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientsConfig {

	@Value("${rnipp.url}")
	private String rnippUrl;
	
	@Value("${keycloak.auth-server-url}")
	private String keycloakUri;
	
	@Bean
	public WebClient rnippClient() {
		return WebClient.builder()
				.baseUrl(rnippUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	@Bean
    public WebClient keyCloakClient() {
    	return  WebClient.builder()
    			.baseUrl(keycloakUri)
    			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    			.build();
    }

}
