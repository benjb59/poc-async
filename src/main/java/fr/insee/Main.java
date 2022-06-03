package fr.insee;

import fr.insee.pocasync.producer.domain.UserDTO;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Bean
    public BeforeConvertCallback<UserDTO> idGeneratingCallback(DatabaseClient databaseClient) {

        return (userDTO, sqlIdentifier) -> userDTO.userId() == null ?
                databaseClient.sql("SELECT NEXT VALUE FOR primary_key")
                        .map(row -> row.get(0, Long.class))
                        .first()
                        .map(userDTO::withUserId):
                Mono.just(userDTO);
    }

        @Bean
    public ConnectionFactoryInitializer databaseSchemaInitializer(ConnectionFactory connectionFactory) {

        var initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(
                new ResourceDatabasePopulator(
                        new ByteArrayResource("""
                create sequence if not exists primary_key;
                DROP TABLE IF EXISTS user_dto;
                CREATE TABLE user_dto (user_id bigint PRIMARY KEY, username VARCHAR(100) NOT NULL, correlation_id UUID NOT NULL, registered BOOLEAN);
                """.getBytes()
                        )
                ));

        return initializer;
    }

}

