package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.*;

public interface UserService {
    void createUser(UserDTO userDTO)
            throws FinderException, CheckException, CreateException;

    void updateUser(UserDTO userDTO);

    void deleteUser(String id) throws DeleteException;
}
