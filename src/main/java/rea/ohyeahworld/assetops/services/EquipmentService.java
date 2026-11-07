package rea.ohyeahworld.assetops.services;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rea.ohyeahworld.assetops.dto.EquipmentRequest;
import rea.ohyeahworld.assetops.dto.EquipmentResponse;
import rea.ohyeahworld.assetops.entities.Equipment;
import rea.ohyeahworld.assetops.enums.EquipmentStatus;
import rea.ohyeahworld.assetops.repos.EquipmentRepository;

import java.util.List;
import java.util.Locale;

@Service
public class EquipmentService {
    private final EquipmentRepository repository;

    public EquipmentService(EquipmentRepository repository) {
        this.repository = repository;
    }

    public EquipmentResponse create(EquipmentRequest request) {
        String assetTag = request.assetTag().trim().toUpperCase(Locale.ROOT);
        if (repository.existsByAssetTagIgnoreCase(assetTag)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Asset tag already exists");
        }

        Equipment equipment = new Equipment(
                assetTag,
                request.name().trim(),
                request.category().trim(),
                request.location().trim(),
                request.serialNumber() == null ? null : request.serialNumber().trim(),
                request.installationDate()
        );
        return EquipmentResponse.from(repository.save(equipment));
    }

    public List<EquipmentResponse> findAll(EquipmentStatus status, String category) {
        String normalizedCategory = category == null || category.isBlank() ? null : category.trim();
        List<Equipment> equipment;
        if (status != null && normalizedCategory != null) {
            equipment = repository.findByStatusAndCategoryIgnoreCase(status, normalizedCategory);
        } else if (status != null) {
            equipment = repository.findByStatus(status);
        } else if (normalizedCategory != null) {
            equipment = repository.findByCategoryIgnoreCase(normalizedCategory);
        } else {
            equipment = repository.findAll();
        }
        return equipment.stream().map(EquipmentResponse::from).toList();
    }

    public EquipmentResponse findById(String id) {
        return EquipmentResponse.from(getEquipment(id));
    }

    @Transactional
    public EquipmentResponse updateStatus(String id, EquipmentStatus nextStatus) {
        Equipment equipment = getEquipment(id);
        if (nextStatus == EquipmentStatus.UNDER_MAINTENANCE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Equipment enters maintenance through a work order"
            );
        }
        if (equipment.getStatus() == EquipmentStatus.UNDER_MAINTENANCE
                || equipment.getStatus() == EquipmentStatus.DECOMMISSIONED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Equipment status is managed by its maintenance lifecycle"
            );
        }

        equipment.setStatus(nextStatus);
        equipment.setOperationalStatus(nextStatus);
        return EquipmentResponse.from(repository.save(equipment));
    }

    public Equipment getEquipment(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not found: " + id));
    }
}
