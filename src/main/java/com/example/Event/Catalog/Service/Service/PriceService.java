package com.example.Event.Catalog.Service.Service;

import com.example.Event.Catalog.Service.DataBase.Event;
import com.example.Event.Catalog.Service.DataBase.Price;
import com.example.Event.Catalog.Service.DataBase.Venue;
import com.example.Event.Catalog.Service.Repositories.EventRepository;
import com.example.Event.Catalog.Service.Repositories.PriceRepository;
import org.springframework.stereotype.Service;

@Service
public class PriceService {

    private final PriceRepository priceRepository;
    private final EventRepository eventRepository;
    public PriceService(PriceRepository priceRepository, EventRepository eventRepository) {
        this.priceRepository = priceRepository;
        this.eventRepository = eventRepository;
    }


    public Price createPrice(Price price) {

        if (price.getPriceAmount() < 0) {
            throw new IllegalArgumentException("Price amount cannot be negative");
        }
        if (price.getEvent() == null || price.getEvent().getId() == null) {
            throw new IllegalArgumentException("Билеты должны быть привязаны к событию!");
        }
        Long eventId = price.getEvent().getId();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Такого события нет"));
        price.setEvent(event);
        validateCapacity(price);

        price.setAvailableQuantity(price.getTotalQuantity());
        price.setActive(true);
        return priceRepository.save(price);
    }


    public void deletePrice(Long priceId) {
        Price currentPrice = priceRepository.findById(priceId)
                .orElseThrow(() -> new IllegalArgumentException("Такого типа билетов не существует"));

        currentPrice.setActive(false);
        currentPrice.setAvailableQuantity(0);

        priceRepository.save(currentPrice);
    }


    public void activatePrice(Long priceId) {
        Price price = priceRepository.findById(priceId)
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));

        if (price.isActive()) {
            return;
        }

        validateCapacity(price);

        price.setActive(true);
        price.setAvailableQuantity(price.getTotalQuantity());

        priceRepository.save(price);
    }


    private void validateCapacity(Price price) {
        Event event = price.getEvent();

        if(event.getVenue() == null){
            throw new IllegalArgumentException("Такой площадки нет");
        }
        int maxCapacity = event.getVenue().getCapacity();
        Integer existingTickets = priceRepository.sumTotalQuantityByEventId(event.getId());
        if(existingTickets == null){
            existingTickets = 0;
        }
        if(existingTickets + price.getTotalQuantity() > maxCapacity){
            throw new IllegalArgumentException("Недостаточно мест, доступно: " + (maxCapacity - existingTickets));
        }
    }
}