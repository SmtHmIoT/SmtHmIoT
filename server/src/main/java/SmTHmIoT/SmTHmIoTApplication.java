package SmTHmIoT;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import SmTHmIoT.entity.SensorType;
import SmTHmIoT.repository.SensorTypeRepository;

@SpringBootApplication
public class SmTHmIoTApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmTHmIoTApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(SensorTypeRepository sensorTypeRepository) {
		return args -> {
			// 초기 데이터 삽입
			SensorType sensorType1 = new SensorType();
			sensorType1.setTypeId(1); // 필요 시 다른 필드 추가
			sensorType1.setTypeName("T");
			sensorTypeRepository.save(sensorType1);

			SensorType sensorType2 = new SensorType();
			sensorType2.setTypeId(2); // 필요 시 다른 필드 추가
			sensorType2.setTypeName("H");
			sensorTypeRepository.save(sensorType2);
		};
	}
}
