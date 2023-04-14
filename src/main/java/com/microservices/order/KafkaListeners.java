package com.microservices.order;

import com.jayway.jsonpath.JsonPath;
import com.microservices.order.domain.Order;
import com.microservices.order.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private final OrderRepository orderRepository;

    public KafkaListeners(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "Approve", groupId = "groupId")
    void listener(String data){
        System.out.println("Listener receive: " + data);
        String beerName = JsonPath.read(data, "$.beerName");
        int quantity = JsonPath.read(data, "$.quantity");
        String clientName = JsonPath.read(data, "$.name");
        orderRepository.save(Order.builder()
                .beerName(beerName)
                .quantity(quantity)
                .clientName(clientName)
                .build());
    }
}
