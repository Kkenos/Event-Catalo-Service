package com.example.Event.Catalog.Service.Controller;

import com.example.Event.Catalog.Service.DataBase.Price;
import com.example.Event.Catalog.Service.Service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/prices")
public class PriceController {
    @Autowired
    private final PriceService priceService;
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @PostMapping("/createPrice")
    public ResponseEntity<Price> addPrice(@RequestBody Price price) {
        Price newPrice = priceService.createPrice(price);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPrice.getId())
                .toUri();
        return ResponseEntity.created(location).body(newPrice);
    }
}
