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
    public WashingMachineDto getMachineById(@PathVariable long number) {
        return service.getMachineByNumber(number);
    }
}
