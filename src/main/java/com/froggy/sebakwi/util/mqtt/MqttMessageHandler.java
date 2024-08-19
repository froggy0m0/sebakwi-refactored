package com.froggy.sebakwi.util.mqtt;

import static com.froggy.sebakwi.util.JsonToObjectConverter.convert;

import com.froggy.sebakwi.checkupList.dto.CheckupRequest;
import com.froggy.sebakwi.checkupList.service.CheckupListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class MqttMessageHandler {

    private final CheckupListService checkupListService;

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {

        checkupListService.processCheckupData(
            convert((String) message.getPayload(), CheckupRequest.class));
    }

}
