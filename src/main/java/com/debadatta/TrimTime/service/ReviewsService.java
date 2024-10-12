package com.debadatta.TrimTime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.debadatta.TrimTime.repo.ReviewsRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReviewsService {

    @Autowired
    private ReviewsRepo reviewsRepo;
    
}
