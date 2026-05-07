package com.example.Event.Catalog.Service.DataBase;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nameEvent;
    @Column(nullable = false)
    private LocalDateTime date;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String createAt;

    @ManyToOne
    @JoinColumn(name = "idVenue")
    private Venue venue;

}
