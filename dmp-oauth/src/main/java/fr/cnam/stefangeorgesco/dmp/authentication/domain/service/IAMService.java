package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public interface IAMService {
    boolean userExistsByUsername(String username);

    boolean userExistsById(String id);

    HttpStatus createUser(UserDTO userDTO) throws WebClientResponseException;

    HttpStatus updateUser(UserDTO userDTO) throws WebClientResponseException;

    HttpStatus deleteUser(String id);
}
