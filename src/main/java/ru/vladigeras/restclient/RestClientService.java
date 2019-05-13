package ru.vladigeras.restclient;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * @author vladi_geras on 13.05.2019
 */
public interface RestClientService {
	<T> RestClientResponse<T> send(String url, HttpMethod method, HttpHeaders httpHeaders, Map<String, String> params, JSONObject body, Class<T> responseType);

	<T> RestClientResponse<T> send(String url, HttpMethod method, HttpHeaders httpHeaders, Map<String, String> params, Class<T> responseType);

	<T> RestClientResponse<T> send(String url, HttpMethod method, HttpHeaders httpHeaders, JSONObject body, Class<T> responseType);
}
