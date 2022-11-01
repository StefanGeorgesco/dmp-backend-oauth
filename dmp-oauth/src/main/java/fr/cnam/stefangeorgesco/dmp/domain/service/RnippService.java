package fr.cnam.stefangeorgesco.dmp.domain.service;

import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import reactor.core.publisher.Mono;

/**
 * Classe de service d'interrogation du service REST externe RNIPP.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
public class RnippService {

	@Autowired
	private WebClient rnippClient;

	/**
	 * Service vérifiant que les données identifiant, nom, prénom et date de
	 * naissance d'un dossier patient correspondent à un enregistrement du RNIPP. La
	 * vérification est positive si aucune exception n'est levée.
	 * 
	 * @param patientFileDTO l'objet
	 *                       {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *                       représentant le dossier patient à vérifier.
	 * @throws CheckException les données du dossier patient ne correspondent pas à
	 *                        un enregistrement du RNIPP.
	 */
	@SuppressWarnings("unchecked")
	public void checkPatientData(@Valid PatientFileDTO patientFileDTO) throws CheckException {

		RnippRecord record = new RnippRecord(patientFileDTO.getId(), patientFileDTO.getFirstname(),
				patientFileDTO.getLastname(), patientFileDTO.getDateOfBirth());

		Mono<Object> responseObject = rnippClient.post().uri("/check").bodyValue(record).exchangeToMono(resp -> {
			if (resp.statusCode().equals(HttpStatus.OK)) {
				return resp.bodyToMono(RnippResponse.class);
			} else if (resp.statusCode().equals(HttpStatus.NOT_ACCEPTABLE)) {
				return resp.bodyToMono(Map.class);
			} else {
				return resp.createException().flatMap(Mono::error);
			}
		});

		Object response = responseObject.block();

		if (response instanceof RnippResponse) {
			if (!((RnippResponse) response).getResult()) {
				throw new CheckException("Les données fournies sont incorrectes. Pas d'enregistrement RNIPP.");
			}
		} else if (response instanceof Map) {
			throw new CheckException("RNIPP - " +
					((Map<String, String>) response).entrySet().stream()
					.map(entry -> entry.getKey() + " : " + entry.getValue())
					.collect(Collectors.joining(", ")));
		}

	}

}
