package rea.ohyeahworld.assetops.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rea.ohyeahworld.assetops.dto.WorkOrderRequest;
import rea.ohyeahworld.assetops.dto.WorkOrderResponse;
import rea.ohyeahworld.assetops.entities.Equipment;
import rea.ohyeahworld.assetops.entities.WorkOrder;
import rea.ohyeahworld.assetops.enums.EquipmentStatus;
import rea.ohyeahworld.assetops.enums.WorkOrderStatus;
import rea.ohyeahworld.assetops.repos.WorkOrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class WorkOrderService {
    private static final Set<WorkOrderStatus> ACTIVE_STATUSES =
            Set.of(WorkOrderStatus.OPEN, WorkOrderStatus.IN_PROGRESS);

    private final EquipmentService equipmentService;
    private final WorkOrderRepository repository;

    public WorkOrderService(EquipmentService equipmentService, WorkOrderRepository repository) {
        this.equipmentService = equipmentService;
        this.repository = repository;
    }

    @Transactional
    public WorkOrderResponse create(WorkOrderRequest request) {
        Equipment equipment = equipmentService.getEquipment(request.equipmentId());
        if (equipment.getStatus() == EquipmentStatus.DECOMMISSIONED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot create a work order for decommissioned equipment"
            );
        }

        WorkOrder workOrder = new WorkOrder(
                equipment,
                request.title().trim(),
                request.description() == null ? null : request.description().trim(),
                request.priority()
        );
        equipment.setStatus(EquipmentStatus.UNDER_MAINTENANCE);
        return WorkOrderResponse.from(repository.save(workOrder));
    }

    @Transactional(readOnly = true)
    public List<WorkOrderResponse> findAll(String equipmentId) {
        List<WorkOrder> workOrders = equipmentId == null
                ? repository.findAll()
                : repository.findByEquipment_Id(equipmentId);
        return workOrders.stream().map(WorkOrderResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public WorkOrderResponse findById(String id) {
        return WorkOrderResponse.from(getWorkOrder(id));
    }

    @Transactional
    public WorkOrderResponse updateStatus(String id, WorkOrderStatus nextStatus) {
        WorkOrder workOrder = getWorkOrder(id);
        WorkOrderStatus currentStatus = workOrder.getStatus();

        boolean validTransition = switch (currentStatus) {
            case OPEN -> nextStatus == WorkOrderStatus.IN_PROGRESS || nextStatus == WorkOrderStatus.CANCELLED;
            case IN_PROGRESS -> nextStatus == WorkOrderStatus.COMPLETED || nextStatus == WorkOrderStatus.CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
        if (!validTransition) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot change work order status from " + currentStatus + " to " + nextStatus
            );
        }

        workOrder.setStatus(nextStatus);
        if (nextStatus == WorkOrderStatus.IN_PROGRESS) {
            workOrder.setStartedAt(LocalDateTime.now());
        }
        if (nextStatus == WorkOrderStatus.COMPLETED) {
            workOrder.setCompletedAt(LocalDateTime.now());
        }

        if (!ACTIVE_STATUSES.contains(nextStatus)
                && !repository.existsByEquipment_IdAndStatusIn(
                        workOrder.getEquipment().getId(),
                        ACTIVE_STATUSES
                )) {
            workOrder.getEquipment().setStatus(workOrder.getEquipment().getOperationalStatus());
        }
        return WorkOrderResponse.from(workOrder);
    }

    private WorkOrder getWorkOrder(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Work order not found: " + id));
    }
}
