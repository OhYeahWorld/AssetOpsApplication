package rea.ohyeahworld.assetops.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import rea.ohyeahworld.assetops.dto.WorkOrderRequest;
import rea.ohyeahworld.assetops.dto.WorkOrderResponse;
import rea.ohyeahworld.assetops.entities.Equipment;
import rea.ohyeahworld.assetops.entities.WorkOrder;
import rea.ohyeahworld.assetops.enums.EquipmentStatus;
import rea.ohyeahworld.assetops.enums.MaintenancePriority;
import rea.ohyeahworld.assetops.enums.WorkOrderStatus;
import rea.ohyeahworld.assetops.repos.WorkOrderRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WorkOrderServiceTest {
    @Mock
    private EquipmentService equipmentService;
    @Mock
    private WorkOrderRepository repository;

    private WorkOrderService service;
    private Equipment equipment;
    private WorkOrder workOrder;

    @BeforeEach
    void setUp() {
        service = new WorkOrderService(equipmentService, repository);
        equipment = new Equipment("PUMP-042", "Cooling pump", "PUMP", "Plant A", null, null);
        workOrder = new WorkOrder(equipment, "Replace bearing", "Excessive vibration", MaintenancePriority.HIGH);
        equipment.setStatus(EquipmentStatus.UNDER_MAINTENANCE);
        equipment.setOperationalStatus(EquipmentStatus.IN_USE);
    }

    @Test
    void completingWorkOrderRestoresPreviousOperationalStatus() {
        when(repository.findById(workOrder.getId())).thenReturn(Optional.of(workOrder));
        when(repository.existsByEquipment_IdAndStatusIn(workOrder.getEquipment().getId(),
                java.util.Set.of(WorkOrderStatus.OPEN, WorkOrderStatus.IN_PROGRESS))).thenReturn(false);

        service.updateStatus(workOrder.getId(), WorkOrderStatus.IN_PROGRESS);
        var result = service.updateStatus(workOrder.getId(), WorkOrderStatus.COMPLETED);

        assertEquals(WorkOrderStatus.COMPLETED, result.status());
        assertNotNull(result.startedAt());
        assertNotNull(result.completedAt());
        assertEquals(EquipmentStatus.IN_USE, equipment.getStatus());
    }

    @Test
    void terminalWorkOrderCannotBeReopened() {
        workOrder.setStatus(WorkOrderStatus.COMPLETED);
        when(repository.findById(workOrder.getId())).thenReturn(Optional.of(workOrder));

        assertThrows(ResponseStatusException.class,
                () -> service.updateStatus(workOrder.getId(), WorkOrderStatus.IN_PROGRESS));
    }

    @Test
    void completionKeepsEquipmentUnderMaintenanceWhenAnotherOrderIsActive() {
        when(repository.findById(workOrder.getId())).thenReturn(Optional.of(workOrder));
        when(repository.existsByEquipment_IdAndStatusIn(workOrder.getEquipment().getId(),
                java.util.Set.of(WorkOrderStatus.OPEN, WorkOrderStatus.IN_PROGRESS))).thenReturn(true);
        workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);

        service.updateStatus(workOrder.getId(), WorkOrderStatus.COMPLETED);

        assertEquals(EquipmentStatus.UNDER_MAINTENANCE, equipment.getStatus());
    }

    @Test
    void creatingWorkOrderMarksEquipmentForMaintenance() {
        equipment.setStatus(EquipmentStatus.AVAILABLE);
        equipment.setOperationalStatus(EquipmentStatus.AVAILABLE);
        when(equipmentService.getEquipment(equipment.getId())).thenReturn(equipment);
        when(repository.save(any(WorkOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));
        WorkOrderRequest request = new WorkOrderRequest(
                equipment.getId(),
                "Replace bearing",
                "Excessive vibration",
                MaintenancePriority.HIGH
        );

        WorkOrderResponse result = service.create(request);

        assertEquals(WorkOrderStatus.OPEN, result.status());
        assertEquals(EquipmentStatus.UNDER_MAINTENANCE, equipment.getStatus());
    }

    @Test
    void decommissionedEquipmentCannotReceiveWorkOrders() {
        equipment.setStatus(EquipmentStatus.DECOMMISSIONED);
        when(equipmentService.getEquipment(equipment.getId())).thenReturn(equipment);
        WorkOrderRequest request = new WorkOrderRequest(
                equipment.getId(),
                "Replace bearing",
                null,
                MaintenancePriority.HIGH
        );

        assertThrows(ResponseStatusException.class, () -> service.create(request));
        verify(repository, never()).save(any(WorkOrder.class));
    }
}
