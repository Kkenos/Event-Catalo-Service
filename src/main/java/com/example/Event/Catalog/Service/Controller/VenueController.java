package com.example.Event.Catalog.Service.Controller;

import com.example.Event.Catalog.Service.DataBase.Venue;
import com.example.Event.Catalog.Service.Service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/venues")
public class VenueController {
    private final VenueService venueService;


    public VenueController(VenueService venueService){
        this.venueService = venueService;
    }
    @PostMapping("/newVenue")
    public ResponseEntity<Venue> addVenue(@RequestBody Venue venue){
        Venue newVenue = venueService.CreateVenue(venue);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newVenue.getId())
                .toUri();
        return ResponseEntity.created(location).body(newVenue);
    }
    @GetMapping
    public ResponseEntity<List<Venue>> getAllVenue(){
        List<Venue> venues =  venueService.getAllVenues();
        return ResponseEntity.ok(venues);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Venue> deleteVenue(@PathVariable Long id){
        venueService.DeleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
