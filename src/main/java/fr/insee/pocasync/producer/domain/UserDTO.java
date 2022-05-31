package fr.insee.pocasync.producer.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;
    private UUID correlationId;
    private String username;
    private boolean registered;
}
