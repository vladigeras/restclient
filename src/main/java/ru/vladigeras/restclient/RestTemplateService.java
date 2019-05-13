package ru.vladigeras.restclient;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * @author vladi_geras on 13.05.2019
 */
@Service
public class RestTemplateService implements RestClientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateService.class);

	@Override
	public <T> RestClientResponse<T> send(String url, HttpMethod method, HttpHeaders headers, Map<String, String> params, JSONObject body, Class<T> responseType) {
		if (headers == null) headers = buildDefaultHeaders();
		return sendRequest(url, method, headers, params, body, responseType);
	}

	@Override
	public <T> RestClientResponse<T> send(String url, HttpMethod method, HttpHeaders headers, Map<String, String> params, Class<T> responseType) {
		return send(url, method, headers, params, null, responseType);
	}

	@Override
	public <T> RestClientResponse<T> send(String url, HttpMethod method, HttpHeaders headers, JSONObject body, Class<T> responseType) {
		return send(url, method, headers, new HashMap<>(), body, responseType);
	}

	private <T> RestClientResponse<T> sendRequest(String url, HttpMethod method, HttpHeaders headers, Map<String, String> params, JSONObject body, Class<T> responseType) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = body == null ? new HttpEntity<>(headers) : new HttpEntity<>(body.toString(), headers);
		ResponseEntity<T> responseEntity;
		try {
			responseEntity = restTemplate.exchange(buildUri(url, params).toUriString(), method, entity, responseType);
			LOGGER.info(method + " запрос на " + url + " был отправлен. Response: " + responseEntity.getStatusCode());

			return new RestClientResponse<>(responseEntity.getBody(), responseEntity.getStatusCode(), null);
		} catch (HttpStatusCodeException e) {
			LOGGER.error("Произошла ошибка при выполнении " + method + " " + url + " запроса: " + e.getMessage()
					+ " " + e.getResponseBodyAsString());
			return new RestClientResponse<>(null, e.getStatusCode(), e.getResponseBodyAsString());
		}
	}

	private UriComponentsBuilder buildUri(String url, Map<String, String> params) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(url);
		if (params != null) {
			params.forEach(builder::queryParam);
		}
		return builder;
	}

	private HttpHeaders buildDefaultHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	private List<HttpMessageConverter<?>> getMessageConverters() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		return messageConverters;
	}
}
