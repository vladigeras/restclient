package ru.vladigeras.restclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vladi_geras on 13.05.2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestTemplateClientConfig.class, RestTemplateService.class})
public class RestTemplateServiceTest {

	private static final String TEST_API_DOMAIN = "https://reqres.in/api";

	@Autowired
	private RestTemplateService restTemplateService;

	@Test
	public void sendSuccessfulPostRequest() throws JSONException {
		String url = TEST_API_DOMAIN + "/register";
		JSONObject body = new JSONObject()
				.put("email", "eve.holt@reqres.in")
				.put("password", "pistol");

		RestClientResponse<TestRegistrationResponse> response = restTemplateService.send(url, HttpMethod.POST, null, Collections.emptyMap(), body, TestRegistrationResponse.class);
		Assert.assertNotNull(response.getBody());
		Assert.assertEquals(HttpStatus.OK, response.getStatus());
		Assert.assertNull(response.getError());
	}

	@Test
	public void sendErrorPostRequest() throws JSONException {
		String url = TEST_API_DOMAIN + "/login";
		JSONObject body = new JSONObject()
				.put("email", "peter@klaven");

		RestClientResponse<TestRegistrationResponse> response = restTemplateService.send(url, HttpMethod.POST, null, Collections.emptyMap(), body, TestRegistrationResponse.class);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
		Assert.assertNotNull(response.getError());
	}

	@Test
	public void sendSuccessfulGetRequest() {
		String url = TEST_API_DOMAIN + "/users/2";
		RestClientResponse<TestUserReponse> response = restTemplateService.send(url, HttpMethod.GET, null, Collections.emptyMap(), null, TestUserReponse.class);
		Assert.assertNotNull(response.getBody());
		Assert.assertEquals(HttpStatus.OK, response.getStatus());
		Assert.assertNull(response.getError());
	}

	@Test
	public void sendErrorGetRequest() {
		String url = TEST_API_DOMAIN + "/users/23";
		RestClientResponse<TestUserReponse> response = restTemplateService.send(url, HttpMethod.GET, null, Collections.emptyMap(), null, TestUserReponse.class);
		Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		Assert.assertNotNull(response.getError());
	}

	@Test
	public void sendSuccessfullRequestWithParams() {
		String url = TEST_API_DOMAIN + "/users";
		Map<String, String> params = new HashMap<>();
		params.put("page", "2");

		RestClientResponse<String> response = restTemplateService.send(url, HttpMethod.GET, null, params, null, String.class);
		Assert.assertNotNull(response.getBody());
		Assert.assertEquals(HttpStatus.OK, response.getStatus());
		Assert.assertNull(response.getError());
	}

	@Test
	public void sendSuccessfulDeleteRequest() {
		String url = TEST_API_DOMAIN + "/users/2";
		RestClientResponse<String> response = restTemplateService.send(url, HttpMethod.DELETE, null, Collections.emptyMap(), null, String.class);
		Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
		Assert.assertNull(response.getError());
	}

	@Test
	public void sendSuccessfulPutRequest() throws JSONException {
		String url = TEST_API_DOMAIN + "/users/2";
		JSONObject body = new JSONObject()
				.put("name", "morpheus")
				.put("job", "zion resident");

		RestClientResponse<String> response = restTemplateService.send(url, HttpMethod.PUT, null, Collections.emptyMap(), body, String.class);
		Assert.assertNotNull(response.getBody());
		Assert.assertEquals(HttpStatus.OK, response.getStatus());
		Assert.assertNull(response.getError());
	}

	static class TestRegistrationResponse {
		public Long id;
		public String token;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	static class TestUserReponse {
		public Object data;
	}
}
