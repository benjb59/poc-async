package fr.insee.pocasync.producer.domain;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;


@Builder
@Table
public record UserDTO(Long userId, @NonNull UUID correlationId, @NonNull String username, boolean registered) {

    public String toPrintableString(){
        return username() + "->" + registered();
    }

    public UserDTO withUserId(@NonNull Long id){
        return new UserDTO(id, this.correlationId, this.username, this.registered);
    }

    public UserDTO withRegistered(boolean registered) {
        return new UserDTO(this.userId, this.correlationId, this.username, registered);
    }

    public UserDTO withCorrelationId(UUID correlationId) {
        return new UserDTO(this.userId, correlationId, this.username, this.registered);
    }


}
