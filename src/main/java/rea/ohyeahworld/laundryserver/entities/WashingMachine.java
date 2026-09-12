package rea.ohyeahworld.laundryserver.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import rea.ohyeahworld.laundryserver.enums.MachineCondition;

import java.util.UUID;

@Entity
@Table(name = "washing_machine")
@Getter
@Setter
@EnableJpaAuditing
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WashingMachine {
    @Id
    @Column(name = "id", nullable = false)
    @NotNull
    private String id;

    @Column(name = "machine_condition", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private MachineCondition condition;

    @Column(name = "number", nullable = false)
    @NotNull
    private Long number;

    @Column(name = "remaining_time", nullable = true)
    private Long remainingTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users owner;

    public WashingMachine(Long number) {
        this.id = UUID.randomUUID().toString();
        this.number = number;
        this.condition = MachineCondition.AVAILABLE;
    }
}
