package com.debadatta.TrimTime.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.debadatta.TrimTime.model.Barbers;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class BarbersRepo {
    
    @Autowired
    final private DynamoDBMapper dynamoDBMapper;

    public String createProfile(Barbers barbers) {
         dynamoDBMapper.save(barbers);
         return barbers.getBarber_id();
    }
    
}
