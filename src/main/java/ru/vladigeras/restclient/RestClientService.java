package ru.vladigeras.restclient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * @author vladi_geras on 13.05.2019
 */
public interface RestClientService {
	<T> RestClientResponse<T> send(String url, HttpMethod method, HttpHeaders httpHeaders, Map<String, String> params, Object body, Class<T> responseType);
}
