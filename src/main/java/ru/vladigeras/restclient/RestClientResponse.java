package ru.vladigeras.restclient;

import org.springframework.http.HttpStatus;

/**
 * @author vladi_geras on 13.05.2019
 */
public class RestClientResponse<T> {
	private T body;
	private HttpStatus status;
	private Object error;

	public RestClientResponse(T body, HttpStatus status, Object error) {
		this.body = body;
		this.status = status;
		this.error = error;
	}

	public T getBody() {
		return body;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public Object getError() {
		return error;
	}
}
