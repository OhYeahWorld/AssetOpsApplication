package rea.ohyeahworld.laundryserver.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rea.ohyeahworld.laundryserver.entities.WashingMachine;
import rea.ohyeahworld.laundryserver.enums.MachineCondition;

import java.time.LocalDateTime;
import java.util.List;

public interface WashingMachineRepository extends JpaRepository<WashingMachine, String> {
    WashingMachine findByNumber(long number);

    List<WashingMachine> findByCondition(MachineCondition machineCondition);

    // В репозитории добавьте метод:
    @Query("SELECT COALESCE(MAX(w.number), 0) FROM WashingMachine w")
    Long findMaxNumber();

}
