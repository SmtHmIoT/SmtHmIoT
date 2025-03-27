#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <mosquitto.h>
#include <unistd.h>

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
    printf("Message with ID %d has been published!\n", mid);
}

int main()
{
    struct mosquitto *mosq;
    int rc;
    int count = 1;

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
        char message[10];
        snprintf(message, sizeof(message), "%d", count);

        rc = mosquitto_publish(mosq, NULL, TOPIC, strlen(message), message, QOS, RETAIN);
        if (rc != MOSQ_ERR_SUCCESS)
        {
            fprintf(stderr, "Failed to publish message: %s\n", mosquitto_strerror(rc));
        }
        else
        {
            printf("Published: %s\n", message);
        }

        count++; 
        sleep(5);
    }

    mosquitto_destroy(mosq);
    mosquitto_lib_cleanup();

    return 0;
}