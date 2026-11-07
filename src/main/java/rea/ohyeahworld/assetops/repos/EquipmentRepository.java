package rea.ohyeahworld.assetops.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rea.ohyeahworld.assetops.entities.Equipment;
import rea.ohyeahworld.assetops.enums.EquipmentStatus;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, String> {
    boolean existsByAssetTagIgnoreCase(String assetTag);

    List<Equipment> findByStatus(EquipmentStatus status);

    List<Equipment> findByCategoryIgnoreCase(String category);

    List<Equipment> findByStatusAndCategoryIgnoreCase(EquipmentStatus status, String category);

}
