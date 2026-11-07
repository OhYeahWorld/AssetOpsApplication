package rea.ohyeahworld.assetops.dto;

import rea.ohyeahworld.assetops.entities.WorkOrder;
import rea.ohyeahworld.assetops.enums.MaintenancePriority;
import rea.ohyeahworld.assetops.enums.WorkOrderStatus;

import java.time.LocalDateTime;

public record WorkOrderResponse(
        String id,
        String equipmentId,
        String equipmentAssetTag,
        String title,
        String description,
        MaintenancePriority priority,
        WorkOrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime startedAt,
        LocalDateTime completedAt
) {
    public static WorkOrderResponse from(WorkOrder workOrder) {
        return new WorkOrderResponse(
                workOrder.getId(),
                workOrder.getEquipment().getId(),
                workOrder.getEquipment().getAssetTag(),
                workOrder.getTitle(),
                workOrder.getDescription(),
                workOrder.getPriority(),
                workOrder.getStatus(),
                workOrder.getCreatedAt(),
                workOrder.getStartedAt(),
                workOrder.getCompletedAt()
        );
    }
}
