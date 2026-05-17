package com.example.Event.Catalog.Service.Service;


import com.example.Event.Catalog.Service.DataBase.Event;

import com.example.Event.Catalog.Service.Repositories.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void TakeInfo(Event event) {

        if (event.getStartTime() == null || event.getEndTime() == null) {
            throw new IllegalArgumentException("Ошибка, не все даты указаны!");
        } else if ((event.getStartTime().compareTo(event.getEndTime()) < 0)) {
            event.setStatus("ACTIVE");
            eventRepository.save(event);
        } else {
            throw new IllegalArgumentException("Ошибка, время окончания не может быть раньше начала!");
        }
    }

    public void UpdateInformation(Event event) {
        Event existingEvent = eventRepository.findById(event.getId())
                .orElseThrow(() -> new IllegalArgumentException("Событие не найдено"));
        if (event.getStatus() != null) {
            existingEvent.setStatus(event.getStatus());
        }
        if (event.getDescription() != null && !event.getDescription().isEmpty()) {
            existingEvent.setDescription(event.getDescription());
        }
        if (event.getStartTime() != null) {
            existingEvent.setStartTime(event.getStartTime());
        }
        if (event.getEndTime() != null) {
            existingEvent.setEndTime(event.getEndTime());
        }
        if ((existingEvent.getStartTime().compareTo(existingEvent.getEndTime()) < 0)) {
            eventRepository.save(existingEvent);
        } else if (existingEvent.getStartTime() == null || existingEvent.getEndTime() == null) {
            throw new IllegalArgumentException("Не введены начало и/или конец события!");
        } else {
            throw new IllegalArgumentException("Ошибка, время окончания не может быть раньше начала!");
        }
    }

    public void DeleteInformation(long id) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Событие не найдено"));
        existingEvent.setStatus("DELETED");
        eventRepository.save(existingEvent);
    }

    public Event getEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Событие не найдено"));
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAllByStatus("ACTIVE");
    }

    public Page<Event> getAllEventsByEventDateBetween(LocalDateTime start, LocalDateTime end, int page, int size) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Начало не может быть после конца");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        return eventRepository.findAllByEventDateBetween(start, end, pageable);
    }

    public Page<Event> getEventByName(String eventName, Pageable pageable) {
        return eventRepository.findByEventName(eventName, pageable);
    }
}
