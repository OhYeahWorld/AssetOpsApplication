package rea.ohyeahworld.laundryserver.controllers;

import org.springframework.web.bind.annotation.*;
import rea.ohyeahworld.laundryserver.dto.WashingMachineDto;
import rea.ohyeahworld.laundryserver.services.WashingMachineService;

import java.util.List;

@RestController("/laundry")
public class WashingMachineController {
    private WashingMachineService service;

    WashingMachineController(WashingMachineService service) {
        this.service = service;
    }

    @PostMapping("/generateMachines")
    public void generateMachines(@RequestBody int howMuch) {
        for (int i = 0; i < howMuch; i++) {
            service.addNewMachine();
        }
    }

    @PostMapping("/addMachine")
    public void addMachine() {
        service.addNewMachine();
    }

    @GetMapping("/")
    public List<WashingMachineDto> getAllMachines() {
        return service.getAllMachines();
    }

    @GetMapping("/byId/{id}")
    public WashingMachineDto getMachineById(@PathVariable String id) {
        return service.getMachineById(id);
    }

    @GetMapping("/byNum/{number}")
    public WashingMachineDto getMachineByNumber(@PathVariable long number) {
        return service.getMachineByNumber(number);
    }

    @PutMapping("/pauseMachineByNum/{num}")
    public WashingMachineDto pauseMachine(@PathVariable long num) {
        return service.pauseMachineByNum(num);
    }

    @PutMapping("/resumeMachineByNum/{num}")
    public WashingMachineDto resumeMachine(@PathVariable long num) {
        return service.resumeMachineByNum(num);
    }

    @PutMapping("/pauseMachineById/{id}")
    public WashingMachineDto pauseMachineById(@PathVariable String id) {
        return service.pauseMachineById(id);
    }

    @PutMapping("/resumeMachineById/{id}")
    public WashingMachineDto resumeMachineById(@PathVariable String id) {
        return service.resumeMachineById(id);
    }

    @PutMapping("/reserveMachineById/{id}")
    public WashingMachineDto reserveMachineById(@PathVariable String id) {
        return service.reserveMachineById(id);
    }

    @PutMapping("/reserveMachineByNum/{num}")
    public WashingMachineDto reserveMachineByNum(@PathVariable Long num) {
        return service.reserveMachineByNum(num);
    }
}
