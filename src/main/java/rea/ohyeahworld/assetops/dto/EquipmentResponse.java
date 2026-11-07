package rea.ohyeahworld.assetops.dto;

import rea.ohyeahworld.assetops.entities.Equipment;
import rea.ohyeahworld.assetops.enums.EquipmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EquipmentResponse(
        String id,
        String assetTag,
        String name,
        String category,
        String location,
        String serialNumber,
        LocalDate installationDate,
        EquipmentStatus status,
        EquipmentStatus operationalStatus,
        LocalDateTime createdAt
) {
    public static EquipmentResponse from(Equipment equipment) {
        return new EquipmentResponse(
                equipment.getId(),
                equipment.getAssetTag(),
                equipment.getName(),
                equipment.getCategory(),
                equipment.getLocation(),
                equipment.getSerialNumber(),
                equipment.getInstallationDate(),
                equipment.getStatus(),
                equipment.getOperationalStatus(),
                equipment.getCreatedAt()
        );
    }
}
