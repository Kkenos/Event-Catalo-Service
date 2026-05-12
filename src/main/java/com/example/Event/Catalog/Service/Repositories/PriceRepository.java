package com.example.Event.Catalog.Service.Repositories;


import com.example.Event.Catalog.Service.DataBase.Price;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface PriceRepository extends JpaRepository<Price,Long> {
    @Query("SELECT SUM(p.totalQuantity) FROM Price p WHERE p.event.id = :eventId")
    Integer sumTotalQuantityByEventId(Long eventId);


}
