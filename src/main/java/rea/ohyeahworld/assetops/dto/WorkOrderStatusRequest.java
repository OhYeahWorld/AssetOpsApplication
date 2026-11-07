package rea.ohyeahworld.assetops.dto;

import jakarta.validation.constraints.NotNull;
import rea.ohyeahworld.assetops.enums.WorkOrderStatus;

public record WorkOrderStatusRequest(@NotNull WorkOrderStatus status) {
}
