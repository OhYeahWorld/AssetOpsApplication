package rea.ohyeahworld.laundryserver.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import rea.ohyeahworld.laundryserver.enums.MachineCondition;

import java.time.LocalDateTime;

@Entity
@Table(name = "washing_machine")
@Getter
@Setter
@NoArgsConstructor
@EnableJpaAuditing
public class WashingMachine {
    @Id
    @Column(name = "id", nullable = false)
    @NotNull
    private String id;

    @Column(name = "machine_condition", nullable = false)
    @NotNull
    private MachineCondition condition;

    @Column(name = "number", nullable = false)
    @NotNull
    private Long number;

    @Column(name = "remaining_time", nullable = true)
    private Long remainingTime;

    public WashingMachine(Long number) {
        this.number = number;
    }
}
