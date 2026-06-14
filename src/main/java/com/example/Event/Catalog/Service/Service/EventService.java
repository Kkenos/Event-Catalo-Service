package com.example.Event.Catalog.Service.Service;


import com.example.Event.Catalog.Service.DataBase.Event;

import com.example.Event.Catalog.Service.DataBase.Venue;
import com.example.Event.Catalog.Service.Repositories.EventRepository;
import com.example.Event.Catalog.Service.Repositories.VenueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    public EventService(EventRepository eventRepository,VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    public Event createEvent(Event event) {
        if(event.getStartTime() == null || event.getEndTime() == null){
            throw new IllegalArgumentException("Ошибка, не указано начало или конец мероприятия");
        }
        if(event.getStartTime().isAfter(event.getEndTime())){
            throw new IllegalArgumentException("Ошибка, время окончания не может быть раньше начала");
        }
        if(event.getVenue() == null || event.getVenue().getId() == null){
            throw new IllegalArgumentException("Указанная площадка не найдена");
        }
        Long venueId = event.getVenue().getId();
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Площадки нет"));
        event.setVenue(venue);
        event.setStatus("ACTIVE");
        return eventRepository.save(event);
    }

    public void updateInformation(Event event) {
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

    public void deleteInformation(long id) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Событие не найдено"));
        existingEvent.setStatus("DELETED");
        eventRepository.save(existingEvent);
    }

    public Event getEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Событие не найдено!"));
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAllByStatus("ACTIVE");
    }

    public Page<Event> getAllEventsByStartTimeBetween(LocalDateTime start, LocalDateTime end, int page, int size) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Начало не может быть после конца");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        return eventRepository.findAllByStartTimeGreaterThanEqualAndEndTimeLessThanEqual(start, end, pageable);
    }

    public Page<Event> getEventByName(String eventName, Pageable pageable) {
        return eventRepository.findByNameEventContainingIgnoreCase(eventName, pageable);
        //Раньше использовал обычный findByEventName, но так пользователю нужно было вводить полное имя
        // и еще в точь-в-точь! Благодаря Containing(который в SQL превратиться LIKE) можно искать по совпадению
        //IgnoreCase для того чтобы разницы в написании, к примеру, РОК,рок,Рок,рОк и т.п не было важно
    }
    public String converterNameFile(String photoURL){
        int lastToken = photoURL.lastIndexOf(".");
        if(lastToken == -1){
            throw new IllegalArgumentException("У вашего файла нет расширения");
        }
        String photoName = photoURL.substring(lastToken);
        String uuid = UUID.randomUUID().toString();
        String result = uuid + photoName;
        return result;
    }
}
