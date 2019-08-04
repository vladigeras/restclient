package ru.vladigeras.restclient;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * @author vladi_geras on 13.05.2019
 */
@Service
public class RestTemplateService implements RestClientService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateService.class);

	private final RestTemplate restTemplate;

	public RestTemplateService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public <T> RestClientResponse<T> send(String url, HttpMethod method, HttpHeaders headers, Map<String, String> params, Object body, Class<T> responseType) {
		HttpEntity entity = this.buildHttpEntity(headers, body);

		ResponseEntity<T> responseEntity;
		try {
			responseEntity = this.restTemplate.exchange(this.buildUri(url, params).toUriString(), method, entity, responseType);
			LOGGER.info(method + " запрос на " + url + " был отправлен. Response: " + responseEntity.getStatusCode());

			return new RestClientResponse<>(responseEntity.getBody(), responseEntity.getStatusCode(), null);
		} catch (HttpStatusCodeException e) {
			LOGGER.error("Произошла ошибка при выполнении " + method + " " + url + " запроса: " + e.getMessage()
					+ " " + e.getResponseBodyAsString());
			return new RestClientResponse<>(null, e.getStatusCode(), e.getResponseBodyAsString());
		}
	}

	private HttpEntity buildHttpEntity(HttpHeaders headers, Object body) {
		HttpEntity entity;
		if (body == null) {
			entity = this.buildEmptyEntity();
		}
		if (body instanceof JSONObject) {
			// JSONObject
			headers = headers == null ? this.buildHeadersJson() : headers;
			entity = new HttpEntity<>(body.toString(), headers);
		} else if (body instanceof MultiValueMap) {
			// Multipart Form data
			headers = headers == null ? this.buildHeadersForm() : headers;
			entity = new HttpEntity<>(body, headers);
		} else {
			// Object as a json in request body
			headers = headers == null ? this.buildHeadersJson() : headers;
			entity = new HttpEntity<>(body, headers);
		}
		return entity;
	}

	private UriComponentsBuilder buildUri(String url, Map<String, String> params) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(url);
		if (params != null) {
			params.forEach(builder::queryParam);
		}
		return builder;
	}

	private HttpHeaders buildHeadersJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	private HttpHeaders buildHeadersForm() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		return headers;
	}

	private HttpEntity buildEmptyEntity() {
		return new HttpEntity(new HttpHeaders());
	}
}
