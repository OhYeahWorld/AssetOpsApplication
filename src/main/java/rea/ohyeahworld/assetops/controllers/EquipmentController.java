package rea.ohyeahworld.assetops.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rea.ohyeahworld.assetops.dto.EquipmentRequest;
import rea.ohyeahworld.assetops.dto.EquipmentResponse;
import rea.ohyeahworld.assetops.dto.EquipmentStatusRequest;
import rea.ohyeahworld.assetops.enums.EquipmentStatus;
import rea.ohyeahworld.assetops.services.EquipmentService;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    private final EquipmentService service;

    public EquipmentController(EquipmentService service) {
        this.service = service;
    }

    @PostMapping
    public EquipmentResponse create(@Valid @RequestBody EquipmentRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<EquipmentResponse> findAll(
            @RequestParam(required = false) EquipmentStatus status,
            @RequestParam(required = false) String category
    ) {
        return service.findAll(status, category);
    }

    @GetMapping("/{id}")
    public EquipmentResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PatchMapping("/{id}/status")
    public EquipmentResponse updateStatus(
            @PathVariable String id,
            @Valid @RequestBody EquipmentStatusRequest request
    ) {
        return service.updateStatus(id, request.status());
    }
}
