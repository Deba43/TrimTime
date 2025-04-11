package com.debadatta.TrimTime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debadatta.TrimTime.serv.ReviewsService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Reviews")
public class ReviewsController {

    @Autowired
    private ReviewsService reviewsService;

    //rating
    //reviewVotes
    //imageUpload
    //tags
    //timeStamp
    //anonymousReview
    //serviceFeedback
    
}
