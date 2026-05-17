package com.example.Event.Catalog.Service.Repositories;

import com.example.Event.Catalog.Service.DataBase.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByStatus(String status);

    Page<Event> findAllByEventDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Event> findByEventName(String eventName, Pageable pageable);
}
