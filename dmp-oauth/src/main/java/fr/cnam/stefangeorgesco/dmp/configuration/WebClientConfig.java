package fr.cnam.stefangeorgesco.dmp.configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

	@Value("${rnipp.url}")
	private String rnippUrl;

	@Bean
	public HttpClient getHttpClient() {
		return HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
				.responseTimeout(Duration.ofMillis(1000))
				.doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(1000, TimeUnit.MILLISECONDS))
						.addHandlerLast(new WriteTimeoutHandler(1000, TimeUnit.MILLISECONDS)));
	}

	@Bean
	public WebClient rnippClient() {
		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(getHttpClient())).baseUrl(rnippUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

}
