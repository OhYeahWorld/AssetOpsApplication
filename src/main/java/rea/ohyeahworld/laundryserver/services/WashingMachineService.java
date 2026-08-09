package rea.ohyeahworld.laundryserver.services;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rea.ohyeahworld.laundryserver.dto.WashingMachineDto;
import rea.ohyeahworld.laundryserver.entities.WashingMachine;
import rea.ohyeahworld.laundryserver.enums.MachineCondition;
import rea.ohyeahworld.laundryserver.repos.WashingMachineRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WashingMachineService {
    private final WashingMachineRepository repository;
    private long machinesAmount = 0L;

    public WashingMachineService(WashingMachineRepository repository) {
        this.repository = repository;
    }

    public WashingMachineDto addNewMachine() {
        WashingMachine machine = new WashingMachine(++machinesAmount);
        repository.save(machine);
        return new WashingMachineDto(machine.getNumber(), machine.getCondition());
    }

    public List<WashingMachineDto> getAllMachines() {
        return repository.findAll().stream()
                .map(machine -> new WashingMachineDto(
                        machine.getNumber(),
                        machine.getCondition(),
                        machine.getRemainingTime()
                )).toList();
    }

    public WashingMachineDto getMachineByNumber(long number) {
        WashingMachine machine = repository.findByNumber(number);
        return new WashingMachineDto(machine.getNumber(),
                machine.getCondition(),
                machine.getRemainingTime());
    }

    public WashingMachineDto getMachineById(String id) {
        WashingMachine machine = repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Стиральная машина с ID " + id + " не найдена!"
        ));
        return new WashingMachineDto(
                machine.getNumber(),
                machine.getCondition(),
                machine.getRemainingTime());
    }

    @Transactional
    public WashingMachineDto reserveMachine(String id) {
        WashingMachine machine = repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Стиральная машина с ID " + id + " не найдена!"
        ));
        if (machine.getCondition() != MachineCondition.AVAILABLE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ошибка: Стиральная машина с ID " + id + " уже забронирована!"
            );
        }
        machine.setCondition(MachineCondition.BUSY);
        machine.setRemainingTime(120000L);

        return new WashingMachineDto(
                machine.getNumber(),
                machine.getCondition(),
                machine.getRemainingTime()
        );
    }

    @Transactional
    @Scheduled(fixedRate = 1000)
    public void releaseExpiredReservations() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(2);

        List<WashingMachine> machines = repository.findByCondition(
                MachineCondition.BUSY
        );

        for (var machine : machines) {
            machine.setRemainingTime(machine.getRemainingTime() - 1);
        }
    }

    @Transactional
    public WashingMachineDto pause(String id) {
        WashingMachine machine = repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Стиральная машина с ID " + id + " не найдена!"
        ));

        if (machine.getCondition() != MachineCondition.BUSY) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ошибка: Нельзя поставить на паузу машину в состоянии " + machine.getCondition()
            );
        }

        machine.setCondition(MachineCondition.PAUSED);

        return new WashingMachineDto(
                machine.getNumber(),
                machine.getCondition(),
                machine.getRemainingTime()
        );
    }

    @Transactional
    public WashingMachineDto resumeMachine(String id) {
        WashingMachine machine = repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Стиральная машина с ID " + id + " не найдена!"
        ));

        if (machine.getCondition() != MachineCondition.PAUSED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ошибка: Машина не находится на паузе!"
            );
        }

        machine.setCondition(MachineCondition.BUSY);

        return new WashingMachineDto(
                machine.getNumber(),
                machine.getCondition(),
                machine.getRemainingTime()
        );
    }
}
