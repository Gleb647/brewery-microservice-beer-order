package com.microservices.order.web.controller;

import com.microservices.order.domain.Order;
import com.microservices.order.model.OrderDto;
import com.microservices.order.web.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/orders")
@RestController
public class OrderController {

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    private final OrderService orderService;

    public OrderController(KafkaTemplate<String, OrderDto> kafkaTemplate, OrderService orderService) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderService = orderService;
    }

    @PostMapping("/make-order")
    public void makeOrder(@RequestBody OrderDto orderDto){
        OrderDto new_order = OrderDto.builder()
                .clientName(orderDto.getClientName())
                .orderedBeerMap(orderDto.getOrderedBeerMap())
                .build();
        kafkaTemplate.send("Orders", new_order);
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getOrders(){
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }
}
