package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import reactor.core.publisher.Mono;

@Service
public class KeycloakService {

	@Value("${IAM.admin.client.username}")
	private String username;

	@Value("${IAM.admin.client.password}")
	private String password;

	@Value("${IAM.admin.client.grant_type}")
	private String grant_type;

	@Value("${IAM.admin.client.client_id}")
	private String client_id;

	@Value("${keycloak.realm}")
	private String realm;

	@Autowired
	WebClient keyCloakClient;
	
	public HttpStatus createKeycloakUser(UserDTO userDTO) throws WebClientResponseException {
		
		String token = getAdminToken();
		
		CredentialRepresentation credentials = new CredentialRepresentation();
		credentials.setType("password");
		credentials.setTemporary(false);
		credentials.setValue(userDTO.getPassword());
		
		UserRepresentation user = new UserRepresentation();
		user.setEnabled(true);
		user.setUsername(userDTO.getUsername());
		user.setCredentials(List.of(credentials));
//		user.setRealmRoles(List.of(userDTO.getRole()));
		user.setGroups(List.of(userDTO.getRole() + "S"));
		
		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put("id", List.of(userDTO.getId()));
		
		user.setAttributes(attributes);

		ResponseEntity<Void> resp = keyCloakClient
										.post()
										.uri("/admin/realms/" + realm + "/users")
										.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
										.header("Authorization", "Bearer " + token).body(Mono.just(user), UserRepresentation.class)
										.retrieve()
										.toBodilessEntity()
										.block();
		return resp.getStatusCode();
	}

	public HttpStatus deleteKeycloakUser(String username) {
		String token = getAdminToken();
		String userId = getKeycloakUSerId(token, username);
		ResponseEntity<Void> resp = keyCloakClient
										.delete()
										.uri("/admin/realms/" + realm + "/users/" + userId)
										.header("Authorization", "Bearer " + token)
										.retrieve()
										.toBodilessEntity()
										.block();
		return resp.getStatusCode();
	}

	public String getAdminToken() {
		String admin = "username=" + username + "&password=" + password + "&grant_type=" + grant_type + "&client_id="
				+ client_id;
		byte[] postData = admin.getBytes(StandardCharsets.UTF_8);
		Map<String, String> result = keyCloakClient
										.post()
										.uri("/realms/master/protocol/openid-connect/token")
										.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
										.body(Mono.just(postData), byte[].class)
										.retrieve()
										.bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
										.block();
		String token = result.get("access_token");
		return token;
	}

	private String getKeycloakUSerId(String token, String username) {
		UserRepresentation[] ur = keyCloakClient
									.get()
									.uri("/admin/realms/" + realm + "/users?username=" + username)
									.header("Authorization", "Bearer " + token)
									.retrieve()
									.bodyToMono(UserRepresentation[].class)
									.block();
		try {
			return ur[0].getId();
		} catch (ArrayIndexOutOfBoundsException e) {}
		return "unknown";
	}
}
