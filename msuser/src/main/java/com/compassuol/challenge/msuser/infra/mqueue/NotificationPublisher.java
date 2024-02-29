package com.compassuol.challenge.msuser.infra.mqueue;

import com.compassuol.challenge.msuser.application.dto.DataNotification;
import com.compassuol.challenge.msuser.application.dto.mapper.UserMapper;
import com.compassuol.challenge.msuser.application.enums.Event;
import com.compassuol.challenge.msuser.domain.Usuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Data
@Component

public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueNotifications;
    private final ObjectMapper objectMapper;

    public NotificationPublisher(RabbitTemplate rabbitTemplate, Queue queueNotifications, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueNotifications = queueNotifications;
        this.objectMapper = objectMapper;
    }

    public void publishNotification(DataNotification data) throws JsonProcessingException {
        var json = convertIntoJson(data);
        rabbitTemplate.convertAndSend(queueNotifications.getName(), json);
    }

    private String convertIntoJson(DataNotification data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }

    public DataNotification createDataNotification(Usuario user, Event event) {
        DataNotification data = UserMapper.toDataNotification(user);
        data.setEvent(event);
        data.setDate(LocalDateTime.now());
        return data;
    }
}
