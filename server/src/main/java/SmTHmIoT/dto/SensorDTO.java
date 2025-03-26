package SmTHmIoT.dto;

import lombok.Data;

@Data
public class SensorDTO {
    private Integer sensorId;
    private String sensorName;
    private String location;
    private String type;
    private String value;
    private String timestamp;
}
