package SmTHmIoT.mqtt;

import SmTHmIoT.dto.SensorDTO;
import SmTHmIoT.entity.Sensor;
import SmTHmIoT.repository.SensorTypeRepository;
import SmTHmIoT.repository.SensorRepository;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttListener {

    private static final String BROKER_URL = "tcp://localhost:1883"; // Mosquitto 브로커 URL
    private static final String TOPIC = "sensor/data";

    private final SensorTypeRepository sensorTypeRepository;
    private final SensorRepository sensorRepository;
    private final Gson gson = new Gson();

    public MqttListener(SensorTypeRepository sensorTypeRepository, SensorRepository sensorRepository) {
        this.sensorTypeRepository = sensorTypeRepository;
        this.sensorRepository = sensorRepository;
        connectAndSubscribe();
    }

    private void connectAndSubscribe() {
        try {
            MqttClient client = new MqttClient(BROKER_URL, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            client.connect(options);
            client.subscribe(TOPIC, (topic, message) -> handleMessage(new String(message.getPayload())));
            System.out.println("Connected to MQTT broker and subscribed to topic: " + TOPIC);
            
        } catch (MqttException e) {
            System.err.println("Failed to connect to MQTT broker. Retrying...");
            // // 재시도 로직 추가
            // try {
            //     Thread.sleep(5000); // 5초 대기 후 재시도
            //     connectAndSubscribe();
            // } catch (InterruptedException ex) {
            //     Thread.currentThread().interrupt();
            // }
        }
    }

    private void handleMessage(String payload) {
        try {
            // JSON 데이터를 SensorDTO 객체로 변환
            SensorDTO sensorDTO = gson.fromJson(payload, SensorDTO.class);

            // Sensor 엔티티로 변환
            Sensor sensor = new Sensor();
            sensor.setSensorName(sensorDTO.getSensorName());
            sensor.setLocation(sensorDTO.getLocation());
            sensor.setCreatedAt(java.time.LocalDateTime.now());
            sensor.setUpdatedAt(java.time.LocalDateTime.now());

            // Sensor 엔티티를 SensorRepository를 통해 저장
            sensorRepository.save(sensor);
            System.out.println("Sensor data saved: " + sensor);
        } catch (com.google.gson.JsonSyntaxException e) {
            System.err.println("Invalid JSON format: " + payload);
        } catch (Exception e) {
            System.err.println("Failed to process message: " + payload);
        }
    }
}
