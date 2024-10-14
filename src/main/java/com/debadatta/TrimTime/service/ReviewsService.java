package com.debadatta.TrimTime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.debadatta.TrimTime.model.Reviews;
import com.debadatta.TrimTime.repo.ReviewsRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReviewsService {

    @Autowired
    private ReviewsRepo reviewsRepo;

    public List<Reviews> showMyReviews(String barber_id) {
        return reviewsRepo.findByBarberId(barber_id);
    }

}
