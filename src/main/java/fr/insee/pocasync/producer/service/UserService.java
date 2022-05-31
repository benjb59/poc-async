package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.domain.UserDTO;

import java.util.stream.Stream;

public interface UserService {
    void createUser(String username);

    Stream<UserDTO> queryUser();
}
