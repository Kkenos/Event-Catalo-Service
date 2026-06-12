package com.example.Event.Catalog.Service.DataBase;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import javax.xml.catalog.Catalog;

@Entity
@Data
@Table(name = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nameEvent;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String createAt;

    @ManyToOne
    @JoinColumn(name = "idVenue")
    private Venue venue;
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Price> prices;
}
