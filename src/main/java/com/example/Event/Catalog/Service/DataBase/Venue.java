package com.example.Event.Catalog.Service.DataBase;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Venue")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String city;
    private int capacity;
    private String typeOfPlace;
    private String venueStatus;
}
