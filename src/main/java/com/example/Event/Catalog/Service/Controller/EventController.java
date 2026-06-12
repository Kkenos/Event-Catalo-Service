package com.example.Event.Catalog.Service.Controller;


import com.example.Event.Catalog.Service.DataBase.Event;
import com.example.Event.Catalog.Service.Service.EventService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }



    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        Event event = eventService.getEventById(id);

        return ResponseEntity.ok(event);
    }

    @GetMapping("/allEvents")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(@RequestParam String eventName,@PageableDefault(size = 10) Pageable pageable) {

        Page<Event> eventPage = eventService.getEventByName(eventName, pageable);
        return ResponseEntity.ok(eventPage.getContent());
    }
    @PostMapping("/createEvent")
    public ResponseEntity<Event> createEvent(@RequestBody Event event){
        Event newEvent = eventService.createEvent(event);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newEvent.getId())
                .toUri();
        return ResponseEntity.created(location).body(newEvent);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id){
        eventService.deleteInformation(id);
        return ResponseEntity.noContent().build();
    }
}
