package fr.insee.pocasync.producer.service;

import fr.insee.pocasync.producer.broker.in.ResponseFromConsumer;
import fr.insee.pocasync.producer.broker.out.RequestToConsumer;
import fr.insee.pocasync.producer.domain.UserEntity;
import fr.insee.pocasync.producer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static fr.insee.Configurations.MESSAGE_QUEUE_RESPONSE;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RequestToConsumer requestToConsumer;
  private final ResponseFromConsumer responseReceiverFromConsumer;

  @Override
  public String createUser(String username) {

    String correlationId=UUID.randomUUID().toString();
    requestToConsumer.publish(username, correlationId);
    responseReceiverFromConsumer.receiveResponse(MESSAGE_QUEUE_RESPONSE, correlationId);

    /*UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    return userRepository.save(userEntity).getUserId().toString();*/
    return null;
  }

  @Override
  public Stream<UserEntity> queryUser() {
    return StreamSupport.stream(userRepository.findAll().spliterator(), false);
  }
}
