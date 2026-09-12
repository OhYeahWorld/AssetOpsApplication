package rea.ohyeahworld.laundryserver.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@EnableJpaAuditing
public class Users {
    @Id
    @Column(name = "id", nullable = false)
    @NotNull
    private String id;


    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private WashingMachine washingMachine;

    public Users() {
        this.id = UUID.randomUUID().toString();
    }
}
