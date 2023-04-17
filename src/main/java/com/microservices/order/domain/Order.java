package com.microservices.order.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "order")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private String id;

    private String clientName;

    private Map<String, Integer> OrderedBeerMap;
}

