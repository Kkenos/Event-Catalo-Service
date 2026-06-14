package com.example.Event.Catalog.Service.DataBase;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "Venue")
@SQLDelete(sql = "UPDATE venues SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String city;
    private int capacity;
    private String typeOfPlace;
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
