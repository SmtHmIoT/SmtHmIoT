package SmTHmIoT.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sensors")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id")
    private Integer sensorId;

    @Column(name = "sensor_name", nullable = false)
    private String sensorName;

    @Column(name = "location")
    private String location;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private SensorType sensorType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SensorStatus status = SensorStatus.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum SensorStatus {
        ACTIVE, INACTIVE, MAINTENANCE
    }
}