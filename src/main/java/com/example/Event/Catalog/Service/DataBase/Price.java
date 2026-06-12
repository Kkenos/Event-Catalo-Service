package com.example.Event.Catalog.Service.DataBase;


import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tier;
    private int priceAmount;
    @Column(columnDefinition = "varchar(255) default 'RUB'")
    private final String currency = "RUB";
    private int availableQuantity;
    private int totalQuantity;
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "eventId")
    @JsonBackReference
    private Event event;
}
