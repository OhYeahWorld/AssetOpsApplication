package rea.ohyeahworld.assetops.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EquipmentRequest(
        @NotBlank @Size(max = 50) String assetTag,
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 80) String category,
        @NotBlank @Size(max = 120) String location,
        @Size(max = 100) String serialNumber,
        @PastOrPresent LocalDate installationDate
) {
}
