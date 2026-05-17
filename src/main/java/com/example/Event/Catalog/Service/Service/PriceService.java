package com.example.Event.Catalog.Service.Service;

import com.example.Event.Catalog.Service.DataBase.Price;
import com.example.Event.Catalog.Service.DataBase.Venue;
import com.example.Event.Catalog.Service.Repositories.PriceRepository;
import org.springframework.stereotype.Service;

@Service
public class PriceService {

    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }


    public void createPrice(Price price) {

        if (price.getPriceAmount() < 0) {
            throw new IllegalArgumentException("Price amount cannot be negative");
        }


        validateCapacity(price);


        price.setAvailableQuantity(price.getTotalQuantity());
        price.setActive(true);

        priceRepository.save(price);
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
        Long eventId = price.getEvent().getId();


        Integer alreadyAllocated = priceRepository.sumTotalQuantityByEventId(eventId);
        int currentSum = (alreadyAllocated != null) ? alreadyAllocated : 0;

        int venueCapacity = price.getEvent().getVenue().getCapacity();


        if (currentSum + price.getTotalQuantity() > venueCapacity) {
            throw new IllegalArgumentException("Ошибка: вместимость превышена! В зале всего "
                    + venueCapacity + " мест. Свободно: " + (venueCapacity - currentSum));
        }
    }
}