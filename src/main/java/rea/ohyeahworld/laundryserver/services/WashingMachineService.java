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

    public WashingMachineService(WashingMachineRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public WashingMachineDto addNewMachine() {
        Long nextNumber = repository.findMaxNumber() + 1;
        WashingMachine machine = new WashingMachine(nextNumber);
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
    public WashingMachineDto reserveMachineById(String id) {
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
    public WashingMachineDto reserveMachineByNum(Long num) {
        WashingMachine machine = repository.findByNumber(num);
        if (machine != null) {
            if (machine.getCondition() != MachineCondition.AVAILABLE) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Ошибка: Стиральная машина с номером " + num + " уже забронирована!"
                );
            }
            machine.setCondition(MachineCondition.BUSY);
            machine.setRemainingTime(15L);

            return new WashingMachineDto(
                    machine.getNumber(),
                    machine.getCondition(),
                    machine.getRemainingTime()
            );
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Стиральная машина с номером " + num + " не найдена!"
            );
        }

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
            if (machine.getRemainingTime() == 0) {
                machine.setCondition(MachineCondition.AVAILABLE);
                machine.setRemainingTime(null);
            }
        }
    }

    @Transactional
    public WashingMachineDto pauseMachineById(String id) {
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
    public WashingMachineDto resumeMachineById(String id) {
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

    @Transactional
    public WashingMachineDto pauseMachineByNum(Long num) {
        WashingMachine machine = repository.findByNumber(num);
        if (machine != null) {
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
        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Стиральная машина с номером " + num + " не найдена!"
            );
        }
    }

    @Transactional
    public WashingMachineDto resumeMachineByNum(Long num) {
        WashingMachine machine = repository.findByNumber(num);
        if (machine != null) {
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
        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Стиральная машина с номером " + num + " не найдена!"
            );
        }
    }
}
