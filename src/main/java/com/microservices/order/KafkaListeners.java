package com.microservices.order;

import com.jayway.jsonpath.JsonPath;
import com.microservices.order.domain.Order;
import com.microservices.order.repository.OrderRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KafkaListeners {

    private final OrderRepository orderRepository;

    public KafkaListeners(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = {"Approve", "BeerProduce"}, groupId = "groupId")
    void approveFromBeerStorage(String data){
        String clientName = JsonPath.read(data, "$.clientName");
        Map<String, Integer> map = new HashMap<>();
        map = JsonPath.read(data, "$.orderedBeerMap");
        List<Order> order = orderRepository.findByClientName(clientName);
        if (order.size() > 0){
            map.forEach((client, beer) ->{
                order.get(0).getOrderedBeerMap().put(client, beer);
                orderRepository.save(order.get(0));
            });
        }else{
            orderRepository.save(Order.builder()
                    .clientName(clientName)
                    .OrderedBeerMap(map)
                    .build());
        }
    }
}
