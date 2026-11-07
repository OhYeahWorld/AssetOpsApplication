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
import rea.ohyeahworld.assetops.dto.WorkOrderRequest;
import rea.ohyeahworld.assetops.dto.WorkOrderResponse;
import rea.ohyeahworld.assetops.dto.WorkOrderStatusRequest;
import rea.ohyeahworld.assetops.services.WorkOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {
    private final WorkOrderService service;

    public WorkOrderController(WorkOrderService service) {
        this.service = service;
    }

    @PostMapping
    public WorkOrderResponse create(@Valid @RequestBody WorkOrderRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<WorkOrderResponse> findAll(
            @RequestParam(required = false) String equipmentId
    ) {
        return service.findAll(equipmentId);
    }

    @GetMapping("/{id}")
    public WorkOrderResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PatchMapping("/{id}/status")
    public WorkOrderResponse updateStatus(
            @PathVariable String id,
            @Valid @RequestBody WorkOrderStatusRequest request
    ) {
        return service.updateStatus(id, request.status());
    }
}
