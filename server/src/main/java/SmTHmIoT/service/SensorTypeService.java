package SmTHmIoT.service;

import SmTHmIoT.repository.SensorTypeRepository;
import SmTHmIoT.entity.SensorType;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorTypeService {

    @Autowired
    private SensorTypeRepository sensorTypeRepository;

    // 센서 타입 생성
    public SensorType createSensorType() {
        SensorType sensorType = new SensorType();
        // 필요한 초기화 작업을 수행합니다.
        return sensorTypeRepository.save(sensorType);
    }

    // 모든 센서 타입 조회
    public List<SensorType> getAllSensorTypes() {
        return sensorTypeRepository.findAll();
    }

    // 특정 센서 타입 조회
    public SensorType getSensorTypeById(Integer typeId) {
        return sensorTypeRepository.findById(typeId).orElse(null);
    }
}