package ru.vladigeras.restclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author vladi_geras on 04.08.2019
 */
@Configuration
public class RestTemplateClientConfig {
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		// you can add your own interceptors
		// interceptors.add(new RestTemplateTokenInterceptor());
		restTemplate.setInterceptors(interceptors);
		return restTemplate;
	}
}
