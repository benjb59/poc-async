package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.domain.UserEntity;

import java.util.stream.Stream;

public interface UserService {
    String createUser(String username);

    Stream<UserEntity> queryUser();
}
