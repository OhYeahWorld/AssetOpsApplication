package rea.ohyeahworld.assetops.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import rea.ohyeahworld.assetops.enums.EquipmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Equipment {
    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(name = "asset_tag", nullable = false, unique = true, length = 50)
    private String assetTag;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(nullable = false, length = 120)
    private String location;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "installation_date")
    private LocalDate installationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private EquipmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "operational_status", nullable = false, length = 24)
    private EquipmentStatus operationalStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Equipment(
            String assetTag,
            String name,
            String category,
            String location,
            String serialNumber,
            LocalDate installationDate
    ) {
        this.id = UUID.randomUUID().toString();
        this.assetTag = assetTag;
        this.name = name;
        this.category = category;
        this.location = location;
        this.serialNumber = serialNumber;
        this.installationDate = installationDate;
        this.status = EquipmentStatus.AVAILABLE;
        this.operationalStatus = EquipmentStatus.AVAILABLE;
    }
}
