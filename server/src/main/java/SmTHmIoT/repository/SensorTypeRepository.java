package SmTHmIoT.repository;

import SmTHmIoT.entity.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorTypeRepository extends JpaRepository<SensorType, Integer> {
}