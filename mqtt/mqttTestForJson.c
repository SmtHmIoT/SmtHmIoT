#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <mosquitto.h>
#include <unistd.h>
#include <time.h>

#define CLIENT_ID "mqtt_publisher"
#define BROKER "localhost"
#define PORT 1883
#define TOPIC "test/topic"
#define QOS 0
#define RETAIN false

void on_connect(struct mosquitto *mosq, void *userdata, int result)
{
    if (result == 0)
    {
        printf("Connected to MQTT Broker!\n");
    }
    else
    {
        printf("Failed to connect, result code: %d\n", result);
        exit(1);
    }
}

void on_publish(struct mosquitto *mosq, void *userdata, int mid)
{
    printf("Message with ID %d has been published.\n", mid);
}

int main(void)
{
    struct mosquitto *mosq;
    int rc;
    double temperature = 25.0; // 시작 온도
    const double temp_min = 25.0;
    const double temp_max = 25.5;
    const double temp_step = 0.1;

    // 고정 값들
    int humidity = 40;
    int light = 100;
    double sound = 40.0;

    mosquitto_lib_init();

    mosq = mosquitto_new(CLIENT_ID, true, NULL);
    if (!mosq)
    {
        fprintf(stderr, "Failed to create mosquitto instance\n");
        return 1;
    }

    mosquitto_connect_callback_set(mosq, on_connect);
    mosquitto_publish_callback_set(mosq, on_publish);

    rc = mosquitto_connect(mosq, BROKER, PORT, 60);
    if (rc != MOSQ_ERR_SUCCESS)
    {
        fprintf(stderr, "Failed to connect to broker: %s\n", mosquitto_strerror(rc));
        mosquitto_destroy(mosq);
        return 1;
    }

    while (1)
    {
        char time_str[30];
        char message[256];
        time_t now = time(NULL);
        struct tm *tm_info = localtime(&now);
        // 현재 시간을 "YYYY-MM-DD HH:MM:SS" 형식의 문자열로 변환
        strftime(time_str, sizeof(time_str), "%Y-%m-%d %H:%M:%S", tm_info);

        // JSON 형식의 문자열 생성
        snprintf(message, sizeof(message),
                 "{\"Time\": \"%s\", \"T\": %.2f, \"H\": %d, \"L\": %d, \"S\": %.2f}",
                 time_str, temperature, humidity, light, sound);

        // MQTT 브로커에 JSON 메시지 발행
        rc = mosquitto_publish(mosq, NULL, TOPIC, strlen(message), message, QOS, RETAIN);
        if (rc != MOSQ_ERR_SUCCESS)
        {
            fprintf(stderr, "Failed to publish message: %s\n", mosquitto_strerror(rc));
        }
        else
        {
            printf("Published: %s\n", message);
        }

        // 온도를 0.1씩 증가, 25.5를 초과하면 25.0으로 리셋
        temperature += temp_step;
        if (temperature > temp_max)
        {
            temperature = temp_min;
        }

        sleep(5); // 5초 대기
    }

    mosquitto_destroy(mosq);
    mosquitto_lib_cleanup();

    return 0;
}
