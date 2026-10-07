package com.umc.first_spring.service;

import com.umc.first_spring.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;

    public void createRental(Map<String, Object> body) {
        rentalRepository.save(body);
    }
}