package rea.ohyeahworld.assetops.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import rea.ohyeahworld.assetops.enums.MaintenancePriority;

public record WorkOrderRequest(
        @NotBlank String equipmentId,
        @NotBlank @Size(max = 160) String title,
        @Size(max = 2000) String description,
        @NotNull MaintenancePriority priority
) {
}
