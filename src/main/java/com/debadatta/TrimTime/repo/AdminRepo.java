package com.debadatta.TrimTime.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class AdminRepo {
    
    @Autowired
    final private DynamoDBMapper dynamoDBMapper;
}
