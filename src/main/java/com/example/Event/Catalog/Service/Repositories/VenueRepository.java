package com.example.Event.Catalog.Service.Repositories;

import com.example.Event.Catalog.Service.DataBase.Event;
import com.example.Event.Catalog.Service.DataBase.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface VenueRepository extends JpaRepository<Venue, Long> {

    List<Event> findByIsDeleted(String venueStatus);
}
