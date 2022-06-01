package fr.insee.pocasync.producer.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId=null;
    @NonNull
    private UUID correlationId;
    @NonNull
    private String username;
    private boolean registered;

    public String toPrintableString(){
        return getUsername() + "->" + isRegistered();
    }
}
