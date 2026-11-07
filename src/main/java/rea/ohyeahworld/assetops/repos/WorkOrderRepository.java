package rea.ohyeahworld.assetops.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rea.ohyeahworld.assetops.entities.WorkOrder;
import rea.ohyeahworld.assetops.enums.WorkOrderStatus;

import java.util.Collection;
import java.util.List;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, String> {
    List<WorkOrder> findByEquipment_Id(String equipmentId);

    boolean existsByEquipment_IdAndStatusIn(String equipmentId, Collection<WorkOrderStatus> statuses);
}
