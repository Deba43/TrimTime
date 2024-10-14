package com.debadatta.TrimTime.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.debadatta.TrimTime.model.Reviews;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ReviewsRepo {

    @Autowired
    final private DynamoDBMapper dynamoDBMapper;

    public List<Reviews> findByBarberId(String barber_id) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":barber_id", new AttributeValue().withS(barber_id));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("barber_id = :barber_id")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(Reviews.class, scanExpression);
    }

}
