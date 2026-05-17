package com.example.Event.Catalog.Service.DataBase;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tier;
    private int priceAmount;
    private final String currency = "RUB";
    private int availableQuantity;
    private int totalQuantity;
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;
}
