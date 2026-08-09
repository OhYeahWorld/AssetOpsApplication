package rea.ohyeahworld.laundryserver.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rea.ohyeahworld.laundryserver.entities.WashingMachine;
import rea.ohyeahworld.laundryserver.enums.MachineCondition;

import java.time.LocalDateTime;
import java.util.List;

public interface WashingMachineRepository extends JpaRepository<WashingMachine, String> {
    WashingMachine findByNumber(long number);

    List<WashingMachine> findByCondition(MachineCondition machineCondition);
}
