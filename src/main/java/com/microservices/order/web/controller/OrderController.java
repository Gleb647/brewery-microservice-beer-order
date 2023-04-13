package com.microservices.order.web.controller;

import com.microservices.order.domain.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    KafkaTemplate<String, Order> kafkaTemplate;

    public OrderController(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/make-order")
    public void makeOrder(
            @RequestParam("beerName") String beerName,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("name") String name){
        Order order = Order.builder()
                .beerName(beerName)
                .name(name)
                .quantity(quantity)
                .build();
        kafkaTemplate.send("Orders", order);
    }
}
