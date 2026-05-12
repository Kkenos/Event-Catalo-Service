package com.example.Event.Catalog.Service.Service;


import com.example.Event.Catalog.Service.DataBase.Price;
import com.example.Event.Catalog.Service.DataBase.Venue;
import com.example.Event.Catalog.Service.Repositories.PriceRepository;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
    private PriceRepository priceRepository;
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }
    public void CreatePrice(Price price) {
        if (price.getPriceAmount() < 0) {
            throw new IllegalArgumentException("Price amount cannot be negative");
        }
        Long eventId = price.getEvent().getId();
        Venue venue = price.getEvent().getVenue();
        Integer currentSum = priceRepository.sumTotalQuantityByEventId(eventId);

        int totalAllocated = (currentSum != null) ? currentSum : 0;

        if(totalAllocated + price.getTotalQuantity() > venue.getCapacity()) {
            throw new IllegalArgumentException("Ошибка: В зале всего " + venue.getCapacity() +
                    " мест. Вы пытаетесь распределить уже " + (totalAllocated + price.getTotalQuantity()));
        }
        priceRepository.save(price);
    }
}
