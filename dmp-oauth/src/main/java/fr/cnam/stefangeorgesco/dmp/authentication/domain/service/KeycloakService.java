package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

	private final WebClient keyCloakClient;

	public KeycloakService(WebClient keyCloakClient) {
		this.keyCloakClient = keyCloakClient;
	}

	/**
	 * Service indiquant si un utilisateur existe, recherche par nom d'utilisateur.
	 * 
	 * @param username le nom d'utilisateur.
	 * @return égal à true si l'utilisateur existe, false sinon.
	 */
	public boolean userExistsByUsername(String username) {
		String token = getAdminToken();

		UserRepresentation[] ur = keyCloakClient.get()
				.uri("/admin/realms/" + realm + "/users?username=" + username + "&exact=true")
				.header("Authorization", "Bearer " + token).retrieve().bodyToMono(UserRepresentation[].class).block();

		return ur != null && ur.length > 0;
	}

	/**
	 * Service indiquant si un utilisateur existe, recherche par identifiant.
	 * 
	 * @param id l'identifiant.
	 * @return égal à true si l'utilisateur existe, false sinon.
	 */
	public boolean userExistsById(String id) {
		String token = getAdminToken();

		UserRepresentation[] ur = keyCloakClient.get().uri("/admin/realms/" + realm + "/users?q=id:" + id)
				.header("Authorization", "Bearer " + token).retrieve().bodyToMono(UserRepresentation[].class).block();

		return ur != null && ur.length > 0;
	}

	/**
	 * Service de création d'un utilisateur (credentials, username, firstname,
	 * lastname, email, group, id ttribute)
	 * 
	 * @param userDTO objet UserDTO comportant les données de l'utilisateur.
	 * @return statut HttpStatus (valeur) retourné par la requête WebClient.
	 * @throws WebClientResponseException erreur de communication avec Keycloak
	 */
	public HttpStatus createUser(UserDTO userDTO) throws WebClientResponseException {
		String token = getAdminToken();

		CredentialRepresentation credentials = new CredentialRepresentation();
		credentials.setType("password");
		credentials.setTemporary(false);
		credentials.setValue(userDTO.getPassword());

		UserRepresentation user = new UserRepresentation();
		user.setEnabled(true);
		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setFirstName(userDTO.getFirstname());
		user.setLastName(userDTO.getLastname());
		user.setCredentials(List.of(credentials));
		user.setGroups(List.of(userDTO.getRole() + "S"));

		Map<String, List<String>> attributes = new HashMap<>();
		attributes.put("id", List.of(userDTO.getId()));

		user.setAttributes(attributes);

		ResponseEntity<Void> resp = keyCloakClient.post().uri("/admin/realms/" + realm + "/users")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header("Authorization", "Bearer " + token).body(Mono.just(user), UserRepresentation.class).retrieve()
				.toBodilessEntity().block();

		return Objects.requireNonNull(resp).getStatusCode();
	}

	/**
	 * Service de mise à jour des données de l'utilisateur (firstname, lastname,
	 * email).
	 * 
	 * @param userDTO les données à mettre à jour.
	 * @return statut HttpStatus (valeur) retourné par la requête WebClient.
	 * @throws WebClientResponseException erreur de communication avec Keycloak
	 */
	public HttpStatus updateUser(UserDTO userDTO) throws WebClientResponseException {
		String token = getAdminToken();
		String userId = getUserIdById(token, userDTO.getId());

		UserRepresentation user = new UserRepresentation();
		user.setFirstName(userDTO.getFirstname());
		user.setLastName(userDTO.getLastname());
		user.setEmail(userDTO.getEmail());

		ResponseEntity<Void> resp = keyCloakClient.put().uri("/admin/realms/" + realm + "/users/" + userId)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header("Authorization", "Bearer " + token).body(Mono.just(user), UserRepresentation.class).retrieve()
				.toBodilessEntity().block();

		return Objects.requireNonNull(resp).getStatusCode();
	}

	/**
	 * Service de suppression d'un utilisateur désigné par son identifiant.
	 * 
	 * @param id l'identifiant de l'utilisateur à supprimer.
	 * @return statut HttpStatus (valeur) retourné par la requête WebClient.
	 */
	public HttpStatus deleteUser(String id) {
		String token = getAdminToken();

		String userId = getUserIdById(token, id);

		ResponseEntity<Void> resp = keyCloakClient.delete().uri("/admin/realms/" + realm + "/users/" + userId)
				.header("Authorization", "Bearer " + token).retrieve().toBodilessEntity().block();

		return Objects.requireNonNull(resp).getStatusCode();
	}

	private String getUserIdById(String token, String id) {
		UserRepresentation[] ur = keyCloakClient.get().uri("/admin/realms/" + realm + "/users?q=id:" + id)
				.header("Authorization", "Bearer " + token).retrieve().bodyToMono(UserRepresentation[].class).block();
		try {
			return Objects.requireNonNull(ur)[0].getId();
		} catch (ArrayIndexOutOfBoundsException e) {
			return "unknown";
		}
	}

	private String getAdminToken() {
		String admin = "username=" + username + "&password=" + password + "&grant_type=" + grant_type + "&client_id="
				+ client_id;
		byte[] postData = admin.getBytes(StandardCharsets.UTF_8);
		Map<String, String> result = keyCloakClient.post().uri("/realms/master/protocol/openid-connect/token")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.body(Mono.just(postData), byte[].class).retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
				}).block();

		return Objects.requireNonNull(result).get("access_token");
	}

}
