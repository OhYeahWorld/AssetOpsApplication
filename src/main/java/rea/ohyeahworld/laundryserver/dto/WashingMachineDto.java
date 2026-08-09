package rea.ohyeahworld.laundryserver.dto;

import lombok.NoArgsConstructor;
import rea.ohyeahworld.laundryserver.enums.MachineCondition;

import java.time.LocalDateTime;

@NoArgsConstructor
public class WashingMachineDto {
    private Long number;
    private MachineCondition condition;
    private Long remainingTime;

    public WashingMachineDto(Long number, MachineCondition condition) {
        this.condition = condition;
        this.number = number;
    }

    public WashingMachineDto(Long number, MachineCondition condition, Long remainingTime) {
        this.condition = condition;
        this.number = number;
        this.remainingTime = remainingTime;
    }
}
