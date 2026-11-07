package rea.ohyeahworld.assetops.dto;

import jakarta.validation.constraints.NotNull;
import rea.ohyeahworld.assetops.enums.EquipmentStatus;

public record EquipmentStatusRequest(@NotNull EquipmentStatus status) {
}
