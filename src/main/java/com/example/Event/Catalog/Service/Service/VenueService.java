package com.example.Event.Catalog.Service.Service;

import com.example.Event.Catalog.Service.DataBase.Event;
import com.example.Event.Catalog.Service.DataBase.Venue;
import com.example.Event.Catalog.Service.Repositories.EventRepository;
import com.example.Event.Catalog.Service.Repositories.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueService {
    private VenueRepository venueRepository;
    private EventRepository eventRepository;

    public VenueService(VenueRepository venueRepository, EventRepository eventRepository) {
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
    }

    public Venue CreateVenue(Venue venue) {
        if (!venue.getCity().matches("^[a-zA-Zа-яА-ЯёЁ\\\\s-]+$")) {
            throw new IllegalArgumentException("Содержит недопустимые символы!");
        }
        if (venue.getCity().length() < 3) {
            throw new IllegalArgumentException("Название города не может быть меньше 3 букв!");
        }
        if (venue.getCapacity() < 0) {
            throw new IllegalArgumentException("Вместимость не может быть меньше или равно 0!");
        } else {
            venue.setVenueStatus("ACTIVE");
            return venueRepository.save(venue);
        }
    }

    public void DeleteVenue(long venueId) {
        Venue currentPlace = venueRepository.findById(venueId).orElseThrow(() -> new IllegalArgumentException("такого мета нет!"));
        currentPlace.setVenueStatus("DELETED");
        venueRepository.save(currentPlace);
    }

    public void UpdateVenue(Venue venue) {
        Venue currentPlace = venueRepository.findById(venue.getId()).orElseThrow(() -> new IllegalArgumentException("такого мета нет!"));
        if (currentPlace.getCapacity() > 0) {
            currentPlace.setCapacity(currentPlace.getCapacity());
        }
        if (currentPlace.getName() != null) {
            currentPlace.setName(venue.getName());
        }
        venueRepository.save(currentPlace);
    }

    public Venue GetVenuesByEventId(long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Событие не найдено"));
        if (event.getVenue() == null) {
            throw new IllegalArgumentException("у данного события не назначена площадка");
        }
        return event.getVenue();
    }
    public List<Venue> getAllVenues(){
        List<Venue> venues = venueRepository.findAll();
        return venues;
    }
}
